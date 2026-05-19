package com.stsc4j.generator.bytecode;

import com.stsc4j.generator.BytecodeAbstractGenerator;
import com.stsc4j.generator.BytecodeGenerator;
import com.stsc4j.lexer.TokenType;
import com.stsc4j.parser.v1.ast.StatementReturn;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class BytecodeStatementReturn extends BytecodeAbstractGenerator {

    private final BytecodeGenerator generator;

    public BytecodeStatementReturn(BytecodeGenerator generator) {
        this.generator = generator;
    }

    @Override
    public String visit(StatementReturn stmt) {
        MethodVisitor mv = generator.getMv();

        if (stmt.returnExpression != null) {
            // Evaluamos la expresión, esto empuja el resultado a la pila
            String tipoRetorno = stmt.returnExpression.accept(generator);

            // Elegimos la instrucción de retorno correcta
            if (TokenType.PRIMITIVE_INTEGER.name().equals(tipoRetorno) || TokenType.PRIMITIVE_BOOLEAN.name().equals(tipoRetorno)) {
                mv.visitInsn(Opcodes.IRETURN);
            } else if (TokenType.PRIMITIVE_DECIMAL.name().equals(tipoRetorno)) {
                mv.visitInsn(Opcodes.FRETURN);
            } else if (TokenType.PRIMITIVE_STRING.name().equals(tipoRetorno)) {
                mv.visitInsn(Opcodes.ARETURN); // ARETURN es para Objetos y Strings
            } else {
                mv.visitInsn(Opcodes.RETURN);
            }
        } else {
            // Retorno vacío (return;)
            mv.visitInsn(Opcodes.RETURN);
        }

        return null;
    }
}