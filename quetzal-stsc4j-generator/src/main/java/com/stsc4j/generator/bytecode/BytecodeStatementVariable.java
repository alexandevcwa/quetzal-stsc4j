package com.stsc4j.generator.bytecode;

import com.stsc4j.generator.BytecodeAbstractGenerator;
import com.stsc4j.generator.BytecodeGenerator;
import com.stsc4j.lexer.TokenType; // Importamos tus tokens
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

        // 1. Obtenemos el nombre OFICIAL del token (ej. "PRIMITIVE_DECIMAL") en lugar del lexema ("número")
        String tipoEsperado = stmt.type.getType().name();

        generator.getEnvJVM().registrarVariable(nombreVar);
        int indiceMemoria = generator.getEnvJVM().obtenerIndice(nombreVar);
        generator.getTiposVariables().put(nombreVar, tipoEsperado);

        String tipoValor = null;
        if (stmt.initialValue != null) {
            tipoValor = stmt.initialValue.accept(generator);
        }

        // 2. Comparamos usando los nombres de los Enums
        String tipoDecimal = TokenType.PRIMITIVE_DECIMAL.name();
        String tipoEntero = TokenType.PRIMITIVE_INTEGER.name();

        // COERCIÓN: Si espera decimal, pero el valor evaluado fue entero
        if (tipoEsperado.equals(tipoDecimal) && tipoEntero.equals(tipoValor)) {
            mv.visitInsn(Opcodes.I2F);
        }

        // 3. Guardamos en la memoria según el TokenType
        if (tipoEsperado.equals(tipoDecimal)) {
            mv.visitVarInsn(Opcodes.FSTORE, indiceMemoria);
        } else {
            mv.visitVarInsn(Opcodes.ISTORE, indiceMemoria);
        }

        return null;
    }
}