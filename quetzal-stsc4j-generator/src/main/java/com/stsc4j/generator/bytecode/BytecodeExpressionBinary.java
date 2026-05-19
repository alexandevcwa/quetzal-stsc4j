package com.stsc4j.generator.bytecode;

import com.stsc4j.generator.BytecodeAbstractGenerator;
import com.stsc4j.generator.BytecodeGenerator;
import com.stsc4j.lexer.TokenType;
import com.stsc4j.parser.v1.ast.ExpressionBinary;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class BytecodeExpressionBinary extends BytecodeAbstractGenerator {

    private final BytecodeGenerator generator;

    public BytecodeExpressionBinary(BytecodeGenerator generator) {
        this.generator = generator;
    }

    @Override
    public String visit(ExpressionBinary expr) {
        MethodVisitor mv = generator.getMv();

        String tipoIzq = expr.left != null ? expr.left.accept(generator) : null;
        String tipoDer = expr.right != null ? expr.right.accept(generator) : null;
        String operador = expr.operator.getLexeme();

        String tipoDecimal = TokenType.PRIMITIVE_DECIMAL.name();
        boolean izqEsDecimal = tipoDecimal.equals(tipoIzq);
        boolean derEsDecimal = tipoDecimal.equals(tipoDer);

        // 1. VERIFICAMOS SI ES UNA OPERACIÓN RELACIONAL
        boolean esRelacional = operador.equals("<") || operador.equals(">") ||
                operador.equals("<=") || operador.equals(">=") ||
                operador.equals("==") || operador.equals("!=");

        if (esRelacional) {
            Label labelVerdadero = new Label();
            Label labelFin = new Label();

            if (!izqEsDecimal && !derEsDecimal) {
                // COMPARACIÓN DE ENTEROS
                int opcode = 0;
                switch (operador) {
                    case "<": opcode = Opcodes.IF_ICMPLT; break;
                    case ">": opcode = Opcodes.IF_ICMPGT; break;
                    case "<=": opcode = Opcodes.IF_ICMPLE; break;
                    case ">=": opcode = Opcodes.IF_ICMPGE; break;
                    case "==": opcode = Opcodes.IF_ICMPEQ; break;
                    case "!=": opcode = Opcodes.IF_ICMPNE; break;
                }
                mv.visitJumpInsn(opcode, labelVerdadero);
            } else {
                // COMPARACIÓN DE DECIMALES (Con Coerción)
                if (!izqEsDecimal && derEsDecimal) {
                    mv.visitInsn(Opcodes.SWAP); mv.visitInsn(Opcodes.I2F); mv.visitInsn(Opcodes.SWAP);
                } else if (izqEsDecimal && !derEsDecimal) {
                    mv.visitInsn(Opcodes.I2F);
                }

                // FCMPG compara dos floats y deja 1, -1, o 0 en la pila
                mv.visitInsn(Opcodes.FCMPG);

                int opcode = 0;
                switch (operador) {
                    case "<": opcode = Opcodes.IFLT; break;
                    case ">": opcode = Opcodes.IFGT; break;
                    case "<=": opcode = Opcodes.IFLE; break;
                    case ">=": opcode = Opcodes.IFGE; break;
                    case "==": opcode = Opcodes.IFEQ; break;
                    case "!=": opcode = Opcodes.IFNE; break;
                }
                mv.visitJumpInsn(opcode, labelVerdadero);
            }

            // Si NO saltó, significa que la condición es FALSA. Empujamos 0 y vamos al final.
            mv.visitInsn(Opcodes.ICONST_0);
            mv.visitJumpInsn(Opcodes.GOTO, labelFin);

            // Si SÍ saltó, significa que la condición es VERDADERA. Empujamos 1.
            mv.visitLabel(labelVerdadero);
            mv.visitInsn(Opcodes.ICONST_1);

            // Etiqueta de salida para que el flujo continúe normalmente
            mv.visitLabel(labelFin);

            return TokenType.PRIMITIVE_BOOLEAN.name();
        }

        // 2. SI NO ES RELACIONAL, ENTONCES ES ARITMÉTICA (El código que ya tenías)
        else {
            if (!izqEsDecimal && !derEsDecimal) {
                switch (operador) {
                    case "+": mv.visitInsn(Opcodes.IADD); break;
                    case "-": mv.visitInsn(Opcodes.ISUB); break;
                    case "*": mv.visitInsn(Opcodes.IMUL); break;
                    case "/": mv.visitInsn(Opcodes.IDIV); break;
                }
                return TokenType.PRIMITIVE_INTEGER.name();
            } else {
                if (!izqEsDecimal && derEsDecimal) {
                    mv.visitInsn(Opcodes.SWAP);
                    mv.visitInsn(Opcodes.I2F);
                    mv.visitInsn(Opcodes.SWAP);
                }
                else if (izqEsDecimal && !derEsDecimal) {
                    mv.visitInsn(Opcodes.I2F);
                }

                switch (operador) {
                    case "+": mv.visitInsn(Opcodes.FADD); break;
                    case "-": mv.visitInsn(Opcodes.FSUB); break;
                    case "*": mv.visitInsn(Opcodes.FMUL); break;
                    case "/": mv.visitInsn(Opcodes.FDIV); break;
                }
                return TokenType.PRIMITIVE_DECIMAL.name();
            }
        }
    }
}