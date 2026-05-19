package com.stsc4j.generator.bytecode;

import com.stsc4j.generator.BytecodeAbstractGenerator;
import com.stsc4j.generator.BytecodeGenerator;
import com.stsc4j.parser.v1.ast.StatementBreak;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class BytecodeStatementBreak extends BytecodeAbstractGenerator {

    private final BytecodeGenerator generator;

    public BytecodeStatementBreak(BytecodeGenerator generator) {
        this.generator = generator;
    }

    @Override
    public String visit(StatementBreak stmt) {
        MethodVisitor mv = generator.getMv();

        // Le pedimos al orquestador la etiqueta final del ciclo actual
        Label labelFin = generator.obtenerLabelBreak();

        // Sale
        mv.visitJumpInsn(Opcodes.GOTO, labelFin);

        return null;
    }
}