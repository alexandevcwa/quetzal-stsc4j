package com.stsc4j.generator;

import com.stsc4j.parser.v1.ast.*;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.io.FileOutputStream;
import java.util.List;

public class BytecodeGenerator implements Visitor<Void> {

    private final EnvironmentJVM envJVM = new EnvironmentJVM();
    private ClassWriter cw;
    private MethodVisitor mv;

    // Método principal que arranca la compilación a Bytecode
    public void compile(List<Statement> statements, String nombreClaseSalida) {
        // 1. Configuramos ASM para que calcule la memoria de la pila automáticamente (COMPUTE_FRAMES)
        cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);

        // 2. Definimos el encabezado: public class [Nombre]
        cw.visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC, nombreClaseSalida, null, "java/lang/Object", null);

        // 3. Creamos el método principal: public static void main(String[] args)
        mv = cw.visitMethod(Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC, "main", "([Ljava/lang/String;)V", null, null);
        mv.visitCode();

        // 4. Visitamos el AST. ¡Aquí se empujan las instrucciones!
        for (Statement stmt : statements) {
            stmt.accept(this);
        }

        // 5. Cerramos el método main (RETURN es obligatorio en bytecode, aunque sea void)
        mv.visitInsn(Opcodes.RETURN);
        mv.visitMaxs(0, 0);
        mv.visitEnd();
        cw.visitEnd();

        // 6. ¡Escribimos el archivo .class físico!
        try (FileOutputStream fos = new FileOutputStream(nombreClaseSalida + ".class")) {
            fos.write(cw.toByteArray());
            System.out.println("¡Compilación exitosa! Archivo generado: " + nombreClaseSalida + ".class");
        } catch (Exception e) {
            System.err.println("Error al generar el .class: " + e.getMessage());
        }
    }

    // ==========================================
    // IMPLEMENTACIÓN DE VISITORS
    // ==========================================

    @Override
    public Void visit(ExpressionLiteral expressionLiteral) {
        String tipo = expressionLiteral.token.getType().toString();
        String lexema = expressionLiteral.token.getLexeme();

        // Si es un número entero en Quetzal
        if (tipo.contains("INTEGER") || tipo.contains("ENTERO")) {
            int valor = Integer.parseInt(lexema);
            // Si el número cabe en un byte, empujamos con BIPUSH.
            // SIPUSH es para números más grandes. LDC es para números gigantes.
            // Para simplificar, ASM nos da un método de ayuda que elige el mejor por nosotros:
            mv.visitLdcInsn(valor);
        }
        // Si es una cadena (texto)
        else if (tipo.contains("STRING") || tipo.contains("CADENA")) {
            // Le quitamos las comillas que traiga del Lexer
            String textoLimpio = lexema.replace("\"", "");
            // LDC (Load Constant) empuja el texto a la pila de la JVM
            mv.visitLdcInsn(textoLimpio);
        }

        return null;
    }

    // ==========================================
    // MÉTODOS VACÍOS (Para cumplir con la interfaz Visitor por ahora)
    // ==========================================
    @Override
    public Void visit(StatementVariable statementVariable) {
        // 1. Obtenemos el nombre de la variable (ej. "a")
        String nombreVar = statementVariable.name.getLexeme();

        // 2. Le pedimos a nuestro gestor de memoria que le asigne un casillero
        envJVM.registrarVariable(nombreVar);
        int indiceMemoria = envJVM.obtenerIndice(nombreVar);

        // 3. Visitamos la expresión que está a la derecha del '=' (ej. 10 + 5)
        // Esto hará que los números se sumen y el resultado (15) quede flotando en la cima de la pila
        if (statementVariable.initialValue != null) {
            statementVariable.initialValue.accept(this);
        }

        // 4. ¡La magia final! Le decimos a la JVM: "Toma el número que está en la
        // cima de la pila y guárdalo en el casillero de memoria correspondiente"
        // ISTORE = Integer Store
        mv.visitVarInsn(Opcodes.ISTORE, indiceMemoria);

        return null;
    }
    @Override public Void visit(StatementBlock stmt) { return null; }
    @Override public Void visit(StatementIf stmt) { return null; }

    @Override
    public Void visit(StatementExpression stmtExpr) {
        if (stmtExpr.expression != null) {
            stmtExpr.expression.accept(this);
        }
        return null;
    }
    @Override public Void visit(StatementList stmt) { return null; }
    @Override public Void visit(TypeList stmt) { return null; }
    @Override public Void visit(TypePrimitive stmt) { return null; }
    @Override public Void visit(StatementJsn stmt) { return null; }
    @Override public Void visit(StatementFunctionParameter stmt) { return null; }
    @Override public Void visit(StatementFunction stmt) { return null; }
    @Override public Void visit(StatementReturn stmt) { return null; }
    @Override public Void visit(StatementLoopWhile stmt) { return null; }
    @Override public Void visit(StatementLoopDoWhile stmt) { return null; }
    @Override public Void visit(StatementLoopFor stmt) { return null; }

    @Override
    public Void visit(StatementLoopForEach statementLoopForEach) {
        return null;
    }

    @Override
    public Void visit(StatementIncDec statementIncDec) {
        return null;
    }

    @Override
    public Void visit(StatementMatrixAssignation statementMatrixAssignation) {
        return null;
    }

    @Override
    public Void visit(ExpressionNull expressionNull) {
        return null;
    }

    @Override
    public Void visit(StatementTryCatchFinally statementTryCatchFinally) {
        return null;
    }

    @Override
    public Void visit(StatementConsolaOut statementConsolaOut) {

        mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");

        // 2. Evaluamos la expresión que el usuario quiere imprimir (ej: "a", o "12 + 15")
        // Al llamar a accept(), el BytecodeGenerator viajará por el árbol y dejará el resultado final en la cima de la pila.
        if (statementConsolaOut.expression != null) {
            statementConsolaOut.expression.accept(this);
        }

        // 3. INVOKEVIRTUAL: Llamamos al método nativo "println" para que imprima lo que quedó en la pila.
        // NOTA: "(I)V" significa que recibe un Entero (Integer) y no devuelve nada (Void).
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(I)V", false);

        return null;
    }

    @Override
    public Void visit(StatementContinue statementContinue) {
        return null;
    }

    @Override
    public Void visit(StatementBreak statementBreak) {
        return null;
    }

    @Override
    public Void visit(ExpressionVariable expressionVariable) {
        String nombreVar = expressionVariable.token.getLexeme();
        int indiceMemoria = envJVM.obtenerIndice(nombreVar);

        // ILOAD = Integer Load. Saca el valor del casillero y lo empuja a la pila.
        mv.visitVarInsn(Opcodes.ILOAD, indiceMemoria);
        return null;
    }

    @Override
    public Void visit(ExpressionBinary expressionBinary) {
        // 1. Visitamos el lado izquierdo. Esto hará que el 12 se empuje a la pila.
        if (expressionBinary.left != null) {
            expressionBinary.left.accept(this);
        }

        // 2. Visitamos el lado derecho. Esto hará que el 15 se empuje a la pila.
        if (expressionBinary.right != null) {
            expressionBinary.right.accept(this);
        }

        // 3. Revisamos qué símbolo usó el programador en Quetzal y le damos la
        // instrucción matemática nativa a la JVM
        String operador = expressionBinary.operator.getLexeme();
        switch (operador) {
            case "+":
                mv.visitInsn(Opcodes.IADD); // Integer ADD (Suma)
                break;
            case "-":
                mv.visitInsn(Opcodes.ISUB); // Integer SUBtract (Resta)
                break;
            case "*":
                mv.visitInsn(Opcodes.IMUL); // Integer MULtiply (Multiplicación)
                break;
            case "/":
                mv.visitInsn(Opcodes.IDIV); // Integer DIVide (División)
                break;
        }

        return null;
    }
    @Override public Void visit(ExpressionTernary expr) { return null; }

    @Override
    public Void visit(ExpressionMethodCall expressionMethodCall) {
        // Extraemos quién es el objeto (ej. "consola") y el método ("mostrar")
        String nombreObjeto = "";
        if (expressionMethodCall.object instanceof ExpressionVariable) {
            nombreObjeto = ((ExpressionVariable) expressionMethodCall.object).token.getLexeme();
        }
        String nombreMetodo = expressionMethodCall.methodName.getLexeme();

        // Verificamos si es nuestra función nativa de imprimir
        if (nombreObjeto.equals("consola") && nombreMetodo.equals("mostrar")) {

            // 1. Preparamos el canal de salida estándar de la JVM (System.out)
            mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");

            // 2. Evaluamos el argumento (la variable 'a' o un número)
            if (expressionMethodCall.args != null && !expressionMethodCall.args.isEmpty()) {
                // Al hacer accept, el visit(ExpressionVariable) de arriba empuja el número a la pila
                expressionMethodCall.args.get(0).accept(this);
            }

            // 3. Llamamos al método println de Java
            // La firma "(I)V" significa que recibe un Integer (I) y devuelve Void (V).
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(I)V", false);
        }

        return null;
    }
    @Override public Void visit(ExpressionIndexAccess expr) { return null; }
    @Override public Void visit(ExpressionList expr) { return null; }
    @Override public Void visit(ExpressionJsnBlock expr) { return null; }
    @Override public Void visit(ExpressionJsn expr) { return null; }
    @Override public Void visit(ExpressionPropertyAccess expr) { return null; }
    @Override public Void visit(ExpressionIncDec expr) { return null; }

    @Override
    public Void visit(ExpressionForEachVar expressionForEachVar) {
        return null;
    }
}