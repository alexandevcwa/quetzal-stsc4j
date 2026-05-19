package com.stsc4j.generator.bytecode;

import com.stsc4j.generator.BytecodeAbstractGenerator;
import com.stsc4j.generator.BytecodeGenerator;
import com.stsc4j.parser.v1.ast.ExpressionTernary;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class BytecodeExpressionTernary extends BytecodeAbstractGenerator {

    private final BytecodeGenerator generator;

    public BytecodeExpressionTernary(BytecodeGenerator generator) {
        this.generator = generator;
    }

    @Override
    public String visit(ExpressionTernary expr) {
        MethodVisitor mv = generator.getMv();

        // 1. Evaluamos la condición (deja un 1 o un 0 en la pila)
        if (expr.binary != null) {
            expr.binary.accept(generator);
        }

        Label labelFalso = new Label();
        Label labelFin = new Label();

        // 2. Si es 0 (Falso), saltamos a evaluar la parte derecha
        mv.visitJumpInsn(Opcodes.IFEQ, labelFalso);

        // 3. PARTE VERDADERA (Izquierda): Evaluamos y saltamos al final
        String tipoRetorno = null;
        if (expr.left != null) {
            tipoRetorno = expr.left.accept(generator);
        }
        mv.visitJumpInsn(Opcodes.GOTO, labelFin);

        // 4. PARTE FALSA (Derecha)
        mv.visitLabel(labelFalso);
        if (expr.right != null) {
            expr.right.accept(generator);
        }

        // 5. FIN
        mv.visitLabel(labelFin);

        // Devolvemos el tipo de dato que resultó de la expresión (ambos lados deben ser del mismo tipo)
        return tipoRetorno;
    }
}