package com.stsc4j.generator.bytecode;

import com.stsc4j.generator.BytecodeAbstractGenerator;
import com.stsc4j.generator.BytecodeGenerator;
import com.stsc4j.parser.v1.ast.StatementLoopWhile;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class BytecodeStatementLoopWhile extends BytecodeAbstractGenerator {

    private final BytecodeGenerator generator;

    public BytecodeStatementLoopWhile(BytecodeGenerator generator) {
        this.generator = generator;
    }

    @Override
    public String visit(StatementLoopWhile stmt) {
        MethodVisitor mv = generator.getMv();

        Label labelInicio = new Label();
        Label labelFin = new Label();

        // 1. REGISTRO EN LA PILA: continue va al inicio, break va al fin
        generator.registrarCiclo(labelInicio, labelFin);

        // 2. MARCADOR DE INICIO
        mv.visitLabel(labelInicio);

        // 3. LA CONDICIÓN
        if (stmt.condition != null) {
            stmt.condition.accept(generator);
        }

        // SALTO DE ESCAPE: Si es falso (0), saltamos al fin
        mv.visitJumpInsn(Opcodes.IFEQ, labelFin);

        // 4. EL BLOQUE
        if (stmt.block != null) {
            stmt.block.accept(generator);
        }

        // 5. REINICIO OBLIGATORIO
        mv.visitJumpInsn(Opcodes.GOTO, labelInicio);

        // 6. MARCADOR DE FIN
        mv.visitLabel(labelFin);

        // 7. LIMPIEZA DE LA PILA: Salimos del contexto de este ciclo
        generator.salirCiclo();

        return null;
    }
}