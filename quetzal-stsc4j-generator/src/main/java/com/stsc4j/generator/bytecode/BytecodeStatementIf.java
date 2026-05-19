package com.stsc4j.generator.bytecode;

import com.stsc4j.generator.BytecodeAbstractGenerator;
import com.stsc4j.generator.BytecodeGenerator;
import com.stsc4j.parser.v1.ast.StatementIf;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class BytecodeStatementIf extends BytecodeAbstractGenerator {

    private final BytecodeGenerator generator;

    public BytecodeStatementIf(BytecodeGenerator generator) {
        this.generator = generator;
    }

    @Override
    public String visit(StatementIf stmt) {
        MethodVisitor mv = generator.getMv();

        // 1. Evaluamos la condición (deja un 1 o un 0 en la pila)
        stmt.condition.accept(generator);

        Label labelElse = new Label();
        Label labelFin = new Label();

        // 2. SALTO CONDICIONAL: Si es falso (0), salta al ELSE
        mv.visitJumpInsn(Opcodes.IFEQ, labelElse);

        // 3. BLOQUE TRUE
        if (stmt.thenStatement != null) {
            stmt.thenStatement.accept(generator);
        }

        // Saltamos obligatoriamente al final para esquivar el else
        mv.visitJumpInsn(Opcodes.GOTO, labelFin);

        // 4. MARCADOR DEL ELSE
        mv.visitLabel(labelElse);

        if (stmt.elseStatement != null) {
            stmt.elseStatement.accept(generator);
        }

        // 5. MARCADOR DEL FIN
        mv.visitLabel(labelFin);

        return null;
    }
}