package com.stsc4j.generator;

import com.stsc4j.parser.v1.ast.*;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// 1. CAMBIO CLAVE: Ahora devolvemos String para recordar los tipos (igual que el Semántico)
public class BytecodeGenerator implements Visitor<String> {

    private final EnvironmentJVM envJVM = new EnvironmentJVM();
    private ClassWriter cw;
    private MethodVisitor mv;

    // 2. Diccionario para recordar qué tipo tiene cada variable en la JVM
    private final Map<String, String> tiposVariables = new HashMap<>();

    public void compile(List<Statement> statements, String nombreClaseSalida) {
        cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        cw.visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC, nombreClaseSalida, null, "java/lang/Object", null);

        mv = cw.visitMethod(Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC, "main", "([Ljava/lang/String;)V", null, null);
        mv.visitCode();

        for (Statement stmt : statements) {
            stmt.accept(this);
        }

        mv.visitInsn(Opcodes.RETURN);
        mv.visitMaxs(0, 0);
        mv.visitEnd();
        cw.visitEnd();

        try (FileOutputStream fos = new FileOutputStream(nombreClaseSalida + ".class")) {
            fos.write(cw.toByteArray());
            System.out.println("¡Compilación exitosa! Archivo generado: " + nombreClaseSalida + ".class");
        } catch (Exception e) {
            System.err.println("Error al generar el .class: " + e.getMessage());
        }
    }

    // ==========================================
    // EXPRESIONES
    // ==========================================

    @Override
    public String visit(ExpressionLiteral expressionLiteral) {
        String tipo = expressionLiteral.token.getType().toString();
        String lexema = expressionLiteral.token.getLexeme();

        if (tipo.contains("INTEGER") || tipo.contains("ENTERO")) {
            mv.visitLdcInsn(Integer.parseInt(lexema));
            return "entero"; // Avisamos que empujamos un entero
        }
        else if (tipo.contains("DECIMAL")) {
            mv.visitLdcInsn(Float.parseFloat(lexema));
            return "decimal"; // Avisamos que empujamos un decimal
        }
        else if (tipo.contains("STRING") || tipo.contains("CADENA")) {
            mv.visitLdcInsn(lexema.replace("\"", ""));
            return "cadena";
        }
        return null;
    }

    @Override
    public String visit(ExpressionVariable expressionVariable) {
        String nombreVar = expressionVariable.token.getLexeme();
        int indiceMemoria = envJVM.obtenerIndice(nombreVar);
        String tipo = tiposVariables.get(nombreVar); // Le preguntamos al diccionario qué tipo era

        if (tipo.equals("decimal") || tipo.equals("número")) {
            mv.visitVarInsn(Opcodes.FLOAD, indiceMemoria); // FLOAD = Saca un Float
            return "decimal";
        } else {
            mv.visitVarInsn(Opcodes.ILOAD, indiceMemoria); // ILOAD = Saca un Entero
            return "entero";
        }
    }

    @Override
    public String visit(ExpressionBinary expressionBinary) {
        // Obtenemos los tipos de ambos lados
        String tipoIzq = expressionBinary.left != null ? expressionBinary.left.accept(this) : null;
        String tipoDer = expressionBinary.right != null ? expressionBinary.right.accept(this) : null;
        String operador = expressionBinary.operator.getLexeme();

        boolean izqEsDecimal = tipoIzq.equals("decimal") || tipoIzq.equals("número");
        boolean derEsDecimal = tipoDer.equals("decimal") || tipoDer.equals("número");

        if (!izqEsDecimal && !derEsDecimal) {
            // AMBOS SON ENTEROS: Matemáticas normales (IADD, ISUB...)
            switch (operador) {
                case "+": mv.visitInsn(Opcodes.IADD); break;
                case "-": mv.visitInsn(Opcodes.ISUB); break;
                case "*": mv.visitInsn(Opcodes.IMUL); break;
                case "/": mv.visitInsn(Opcodes.IDIV); break;
            }
            return "entero";
        } else {
            // HAY DECIMALES MEZCLADOS: Magia de conversión (Coerción a Float)

            if (!izqEsDecimal && derEsDecimal) {
                // Pila actual: [Entero, Decimal]. Hay que convertir el de abajo.
                mv.visitInsn(Opcodes.SWAP); // Volteamos la pila: [Decimal, Entero]
                mv.visitInsn(Opcodes.I2F);  // Convertimos el tope a float: [Decimal, Decimal]
                mv.visitInsn(Opcodes.SWAP); // Regresamos al orden original para no arruinar restas y divisiones
            }
            else if (izqEsDecimal && !derEsDecimal) {
                // Pila actual: [Decimal, Entero]. El entero está arriba, súper fácil.
                mv.visitInsn(Opcodes.I2F);  // Lo convertimos directo: [Decimal, Decimal]
            }

            // Ahora que la pila tiene dos Floats, usamos las instrucciones F (FADD, FSUB...)
            switch (operador) {
                case "+": mv.visitInsn(Opcodes.FADD); break;
                case "-": mv.visitInsn(Opcodes.FSUB); break;
                case "*": mv.visitInsn(Opcodes.FMUL); break;
                case "/": mv.visitInsn(Opcodes.FDIV); break;
            }
            return "decimal";
        }
    }

    // ==========================================
    // SENTENCIAS
    // ==========================================

    @Override
    public String visit(StatementVariable statementVariable) {
        String nombreVar = statementVariable.name.getLexeme();
        String tipoEsperado = statementVariable.typo.getLexeme();

        // Si guardan "número", lo tratamos como decimal en la JVM
        if (tipoEsperado.equals("número")) tipoEsperado = "decimal";

        envJVM.registrarVariable(nombreVar);
        int indiceMemoria = envJVM.obtenerIndice(nombreVar);
        tiposVariables.put(nombreVar, tipoEsperado); // Guardamos el tipo para recordarlo luego

        String tipoValor = null;
        if (statementVariable.initialValue != null) {
            tipoValor = statementVariable.initialValue.accept(this);
        }

        // COERCIÓN: Si la variable es decimal, pero el valor evaluado fue entero (ej. número b = 15)
        if (tipoEsperado.equals("decimal") && (tipoValor != null && tipoValor.equals("entero"))) {
            mv.visitInsn(Opcodes.I2F); // Convertimos el 15 entero a 15.0 flotante en la pila
        }

        // Elegimos la caja correcta para guardar (FSTORE o ISTORE)
        if (tipoEsperado.equals("decimal")) {
            mv.visitVarInsn(Opcodes.FSTORE, indiceMemoria);
        } else {
            mv.visitVarInsn(Opcodes.ISTORE, indiceMemoria);
        }

        return null;
    }

    @Override
    public String visit(StatementConsolaOut statementConsolaOut) {
        mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");

        String tipoResultado = null;
        if (statementConsolaOut.expression != null) {
            tipoResultado = statementConsolaOut.expression.accept(this);
        }

        // Dinámicamente elegimos la firma de imprimir de Java
        if (tipoResultado != null && (tipoResultado.equals("decimal") || tipoResultado.equals("número"))) {
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(F)V", false); // Imprime Float
        } else {
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(I)V", false); // Imprime Integer
        }

        return null;
    }

    // ==========================================
    // MÉTODOS VACÍOS RESTANTES (Ajustados a String)
    // ==========================================
    @Override public String visit(StatementBlock stmt) { return null; }
    @Override public String visit(StatementIf stmt) { return null; }
    @Override public String visit(StatementExpression stmtExpr) { if (stmtExpr.expression != null) stmtExpr.expression.accept(this); return null; }
    @Override public String visit(StatementList stmt) { return null; }
    @Override public String visit(TypeList stmt) { return null; }
    @Override public String visit(TypePrimitive stmt) { return null; }
    @Override public String visit(StatementJsn stmt) { return null; }
    @Override public String visit(StatementFunctionParameter stmt) { return null; }
    @Override public String visit(StatementFunction stmt) { return null; }
    @Override public String visit(StatementReturn stmt) { return null; }
    @Override public String visit(StatementLoopWhile stmt) { return null; }
    @Override public String visit(StatementLoopDoWhile stmt) { return null; }
    @Override public String visit(StatementLoopFor stmt) { return null; }
    @Override public String visit(StatementLoopForEach stmt) { return null; }
    @Override public String visit(StatementIncDec stmt) { return null; }
    @Override public String visit(StatementMatrixAssignation stmt) { return null; }
    @Override public String visit(ExpressionNull expr) { return null; }
    @Override public String visit(StatementTryCatchFinally stmt) { return null; }
    @Override public String visit(StatementContinue stmt) { return null; }
    @Override public String visit(StatementBreak stmt) { return null; }
    @Override public String visit(ExpressionTernary expr) { return null; }
    @Override public String visit(ExpressionMethodCall expr) { return null; }
    @Override public String visit(ExpressionIndexAccess expr) { return null; }
    @Override public String visit(ExpressionList expr) { return null; }
    @Override public String visit(ExpressionJsnBlock expr) { return null; }
    @Override public String visit(ExpressionJsn expr) { return null; }
    @Override public String visit(ExpressionPropertyAccess expr) { return null; }
    @Override public String visit(ExpressionIncDec expr) { return null; }
    @Override public String visit(ExpressionForEachVar expr) { return null; }
}