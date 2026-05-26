package com.stsc4j.generator.bytecode;

import com.stsc4j.generator.BytecodeAbstractGenerator;
import com.stsc4j.generator.BytecodeGenerator;
import com.stsc4j.parser.v1.ast.StatementLoopFor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class BytecodeStatementLoopFor extends BytecodeAbstractGenerator {

    private final BytecodeGenerator generator;

    public BytecodeStatementLoopFor(BytecodeGenerator generator) {
        this.generator = generator;
    }

    @Override
    public String visit(StatementLoopFor stmt) {
        MethodVisitor mv = generator.getMv();

        // 1. LA DECLARACIÓN: Ocurre una sola vez fuera del bucle
        if (stmt.declaration != null) {
            stmt.declaration.accept(generator);
        }

        Label labelInicio = new Label();
        Label labelIncremento = new Label();
        Label labelFin = new Label();

        // 2. REGISTRO EN LA PILA: continue va a la sección de incremento, break al fin
        generator.registrarCiclo(labelIncremento, labelFin);

        // 3. MARCADOR DE INICIO (Evaluación de condición)
        mv.visitLabel(labelInicio);

        // 4. LA CONDICIÓN
        if (stmt.condition != null) {
            stmt.condition.accept(generator);
        }

        // SALTO DE ESCAPE: Si es falso (0), terminamos el ciclo
        mv.visitJumpInsn(Opcodes.IFEQ, labelFin);

        // 5. EL BLOQUE DE CÓDIGO INTERNO
        if (stmt.block != null) {
            stmt.block.accept(generator);
        }

        // 6. MARCADOR DE INCREMENTO (Aquí aterriza el continue)
        mv.visitLabel(labelIncremento);

        // 7. EL INCREMENTO
        if (stmt.increment != null) {
            stmt.increment.accept(generator);
        }

        // 8. REGRESO AL INICIO: Volvemos a evaluar la condición
        mv.visitJumpInsn(Opcodes.GOTO, labelInicio);

        // 9. MARCADOR DE FIN
        mv.visitLabel(labelFin);

        // 10. LIMPIEZA DE LA PILA
        generator.salirCiclo();

        return null;
    }
}