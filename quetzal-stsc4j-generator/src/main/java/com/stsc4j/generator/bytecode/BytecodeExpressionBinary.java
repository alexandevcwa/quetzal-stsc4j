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

        // Armamos el operador extrayendo el lexema
        StringBuilder sb = new StringBuilder();
        if (expr.operators != null) {
            for (com.stsc4j.lexer.Token t : expr.operators) {
                sb.append(t.getLexeme());
            }
        }
        String operador = sb.toString();

        String tipoDecimal = TokenType.PRIMITIVE_DECIMAL.name();
        boolean izqEsDecimal = tipoDecimal.equals(tipoIzq) || (tipoIzq != null && tipoIzq.contains("FLOAT"));
        boolean derEsDecimal = tipoDecimal.equals(tipoDer) || (tipoDer != null && tipoDer.contains("FLOAT"));

        // Textos
        boolean izqEsTexto = tipoIzq != null && (tipoIzq.contains("STRING") || tipoIzq.contains("TEXTO") || tipoIzq.contains("texto"));
        boolean derEsTexto = tipoDer != null && (tipoDer.contains("STRING") || tipoDer.contains("TEXTO") || tipoDer.contains("texto"));

        // =========================================================
        // 1. CONCATENACIÓN DE TEXTOS (Sobrecarga del operador +)
        // =========================================================
        if (operador.equals("+") && (izqEsTexto || derEsTexto)) {

            // Si la izquierda es texto pero la derecha es número, convertimos la derecha a texto
            if (izqEsTexto && !derEsTexto) {
                String descriptor = derEsDecimal ? "(F)Ljava/lang/String;" : "(I)Ljava/lang/String;";
                mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/String", "valueOf", descriptor, false);
            }
            // Si la derecha es texto pero la izquierda es número, las intercambiamos, convertimos y devolvemos
            else if (!izqEsTexto && derEsTexto) {
                mv.visitInsn(Opcodes.SWAP);
                String descriptor = izqEsDecimal ? "(F)Ljava/lang/String;" : "(I)Ljava/lang/String;";
                mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/String", "valueOf", descriptor, false);
                mv.visitInsn(Opcodes.SWAP);
            }

            // Unimos los dos textos con String.concat()
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/String", "concat", "(Ljava/lang/String;)Ljava/lang/String;", false);

            return TokenType.PRIMITIVE_STRING.name();
        }

        // =========================================================
        // 2. VERIFICAMOS SI ES UNA OPERACIÓN RELACIONAL
        // =========================================================
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

            mv.visitInsn(Opcodes.ICONST_0);
            mv.visitJumpInsn(Opcodes.GOTO, labelFin);

            mv.visitLabel(labelVerdadero);
            mv.visitInsn(Opcodes.ICONST_1);

            mv.visitLabel(labelFin);

            return TokenType.PRIMITIVE_BOOLEAN.name();
        }

        // =========================================================
        // 3. SI NO ES RELACIONAL NI TEXTO, ENTONCES ES ARITMÉTICA
        // =========================================================
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