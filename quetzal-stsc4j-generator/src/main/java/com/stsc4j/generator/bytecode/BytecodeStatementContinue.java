package com.stsc4j.generator.bytecode;

import com.stsc4j.generator.BytecodeAbstractGenerator;
import com.stsc4j.generator.BytecodeGenerator;
import com.stsc4j.parser.v1.ast.StatementContinue;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class BytecodeStatementContinue extends BytecodeAbstractGenerator {

    private final BytecodeGenerator generator;

    public BytecodeStatementContinue(BytecodeGenerator generator) {
        this.generator = generator;
    }

    @Override
    public String visit(StatementContinue stmt) {
        MethodVisitor mv = generator.getMv();

        // Le pedimos al orquestador la etiqueta de reinicio del ciclo actual
        Label labelReinicio = generator.obtenerLabelContinue();

        // ¡Saltamos a la siguiente iteración!
        mv.visitJumpInsn(Opcodes.GOTO, labelReinicio);

        return null;
    }
}