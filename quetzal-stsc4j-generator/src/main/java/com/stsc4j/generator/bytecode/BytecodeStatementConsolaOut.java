package com.stsc4j.generator.bytecode;

import com.stsc4j.generator.BytecodeAbstractGenerator;
import com.stsc4j.generator.BytecodeGenerator;
import com.stsc4j.lexer.TokenType;
import com.stsc4j.parser.v1.ast.StatementConsolaOut;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class BytecodeStatementConsolaOut extends BytecodeAbstractGenerator {

    private final BytecodeGenerator generator;

    public BytecodeStatementConsolaOut(BytecodeGenerator generator) {
        this.generator = generator;
    }

    @Override
    public String visit(StatementConsolaOut stmt) {
        MethodVisitor mv = generator.getMv();

        mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");

        String tipoResultado = null;
        if (stmt.expression != null) {
            tipoResultado = stmt.expression.accept(generator);
        }

        if (TokenType.PRIMITIVE_DECIMAL.name().equals(tipoResultado)) {
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(F)V", false);
        } else {
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(I)V", false);
        }

        return null;
    }
}