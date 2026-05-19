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

        // 3. Coerción automática (int -> float)
        if (tipoEsperado.equals(TokenType.PRIMITIVE_DECIMAL.name()) &&
                TokenType.PRIMITIVE_INTEGER.name().equals(tipoValor)) {
            mv.visitInsn(Opcodes.I2F);
        }

        // 4. GUARDADO EN MEMORIA (CORREGIDO)
        // Usamos ASTORE para Objetos (String, JSN, Arreglos)
        // Usamos FSTORE para Decimales
        // Usamos ISTORE para Enteros y Booleanos

        if (tipoEsperado.equals(TokenType.PRIMITIVE_DECIMAL.name())) {
            mv.visitVarInsn(Opcodes.FSTORE, indiceMemoria);
        }
        else if (tipoEsperado.equals(TokenType.PRIMITIVE_STRING.name()) ||
                tipoEsperado.equals("JSN_OBJECT") ||
                tipoEsperado.startsWith("[")) {
            // ASTORE es la clave para que el VerifyError desaparezca
            mv.visitVarInsn(Opcodes.ASTORE, indiceMemoria);
        }
        else {
            // Enteros y Booleanos
            mv.visitVarInsn(Opcodes.ISTORE, indiceMemoria);
        }

        return null;
    }
}