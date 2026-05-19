package com.stsc4j.generator.bytecode;

import com.stsc4j.generator.BytecodeAbstractGenerator;
import com.stsc4j.generator.BytecodeGenerator;
import com.stsc4j.parser.v1.ast.StatementLoopDoWhile;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class BytecodeStatementLoopDoWhile extends BytecodeAbstractGenerator {

    private final BytecodeGenerator generator;

    public BytecodeStatementLoopDoWhile(BytecodeGenerator generator) {
        this.generator = generator;
    }

    @Override
    public String visit(StatementLoopDoWhile stmt) {
        MethodVisitor mv = generator.getMv();

        Label labelInicio = new Label();
        Label labelCondicion = new Label();
        Label labelFin = new Label();

        // 1. REGISTRO EN LA PILA: continue salta a la evaluación de condición, break al fin
        generator.registrarCiclo(labelCondicion, labelFin);

        // 2. MARCADOR DE INICIO DEL BLOQUE
        mv.visitLabel(labelInicio);

        // 3. EJECUCIÓN DEL BLOQUE
        if (stmt.block != null) {
            stmt.block.accept(generator);
        }

        // 4. MARCADOR DE EVALUACIÓN (Aquí aterriza el continue)
        mv.visitLabel(labelCondicion);

        // 5. EVALUACIÓN DE LA CONDICIÓN
        if (stmt.condition != null) {
            stmt.condition.accept(generator);
        }

        // 6. SALTO DE REGRESO: Si no es 0 (Verdadero), vuelve a iniciar
        mv.visitJumpInsn(Opcodes.IFNE, labelInicio);

        // 7. MARCADOR DE FIN (Aquí aterriza el break)
        mv.visitLabel(labelFin);

        // 8. LIMPIEZA DE LA PILA
        generator.salirCiclo();

        return null;
    }
}