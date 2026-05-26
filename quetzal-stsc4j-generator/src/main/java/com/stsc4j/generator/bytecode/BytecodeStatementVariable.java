package com.stsc4j.generator.bytecode;

import com.stsc4j.generator.BytecodeAbstractGenerator;
import com.stsc4j.generator.BytecodeGenerator;
import com.stsc4j.lexer.TokenType;
import com.stsc4j.parser.v1.ast.StatementVariable;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class BytecodeStatementVariable extends BytecodeAbstractGenerator {

    private final BytecodeGenerator generator;

    public BytecodeStatementVariable(BytecodeGenerator generator) {
        this.generator = generator;
    }

    @Override
    public String visit(StatementVariable stmt) {
        MethodVisitor mv = generator.getMv();
        String nombreVar = stmt.name.getLexeme();

        // 1. Obtenemos el nombre del tipo esperado
        String tipoEsperado = stmt.type.getType().name();

        generator.getEnvJVM().registrarVariable(nombreVar);
        int indiceMemoria = generator.getEnvJVM().obtenerIndice(nombreVar);
        generator.getTiposVariables().put(nombreVar, tipoEsperado);

        // 2. Evaluamos el valor inicial
        String tipoValor = null;
        if (stmt.initialValue != null) {
            tipoValor = stmt.initialValue.accept(generator);
        }

        // ==========================================
        // 3. COERCIÓN AUTOMÁTICA (Magia del Compilador)
        // ==========================================

        // A. Si espera DECIMAL pero recibe ENTERO (ej: numero x = 10) -> Convertir a Float
        if (tipoEsperado.equals(TokenType.PRIMITIVE_DECIMAL.name()) &&
                TokenType.PRIMITIVE_INTEGER.name().equals(tipoValor)) {
            mv.visitInsn(Opcodes.I2F);
        }

        // B. Si espera ENTERO pero recibe TEXTO de la consola -> Parsear a Int
        else if (tipoEsperado.equals(TokenType.PRIMITIVE_INTEGER.name()) &&
                (TokenType.PRIMITIVE_STRING.name().equals(tipoValor) || "texto".equals(tipoValor))) {
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Integer", "parseInt", "(Ljava/lang/String;)I", false);
        }

        // C. Si espera DECIMAL pero recibe TEXTO -> Parsear a Float
        else if (tipoEsperado.equals(TokenType.PRIMITIVE_DECIMAL.name()) &&
                (TokenType.PRIMITIVE_STRING.name().equals(tipoValor) || "texto".equals(tipoValor))) {
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Float", "parseFloat", "(Ljava/lang/String;)F", false);
        }

        // ==========================================
        // 4. GUARDADO EN MEMORIA
        // ==========================================

        if (tipoEsperado.equals(TokenType.PRIMITIVE_DECIMAL.name())) {
            mv.visitVarInsn(Opcodes.FSTORE, indiceMemoria);
        }
        else if (tipoEsperado.equals(TokenType.PRIMITIVE_STRING.name()) ||
                tipoEsperado.equals("JSN_OBJECT") ||
                tipoEsperado.startsWith("[")) {
            // Objetos (Textos, JSN, Arreglos)
            mv.visitVarInsn(Opcodes.ASTORE, indiceMemoria);
        }
        else {
            // Enteros y Booleanos
            mv.visitVarInsn(Opcodes.ISTORE, indiceMemoria);
        }

        return null;
    }
}