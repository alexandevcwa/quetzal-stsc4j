package com.stsc4j.generator.bytecode;

import com.stsc4j.generator.BytecodeAbstractGenerator;
import com.stsc4j.generator.BytecodeGenerator;
import com.stsc4j.lexer.TokenType;
import com.stsc4j.parser.v1.ast.ExpressionLiteral;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes; // ¡Asegúrate de que esto esté importado!

public class BytecodeExpressionLiteral extends BytecodeAbstractGenerator {

    private final BytecodeGenerator generator;

    public BytecodeExpressionLiteral(BytecodeGenerator generator) {
        this.generator = generator;
    }

    @Override
    public String visit(ExpressionLiteral expr) {
        MethodVisitor mv = generator.getMv();
        TokenType tipo = expr.token.getType();
        String lexema = expr.token.getLexeme();

        // Evaluamos directamente contra el Enum
        if (tipo == TokenType.LIT_INTEGER) {
            mv.visitLdcInsn(Integer.parseInt(lexema));
            return TokenType.PRIMITIVE_INTEGER.name();
        }
        else if (tipo == TokenType.LIT_DECIMAL) {
            mv.visitLdcInsn(Float.parseFloat(lexema));
            return TokenType.PRIMITIVE_DECIMAL.name();
        }
        else if (tipo == TokenType.LIT_STRING) {
            mv.visitLdcInsn(lexema.replace("\"", ""));
            return TokenType.PRIMITIVE_STRING.name();
        }
        // --- NUEVO: SOPORTE PARA BOOLEANOS ---
        else if (tipo == TokenType.LIT_TRUE) {
            mv.visitInsn(Opcodes.ICONST_1); // Empuja un 1 (True) a la pila
            return TokenType.PRIMITIVE_BOOLEAN.name();
        }
        else if (tipo == TokenType.LIT_FALSE) {
            mv.visitInsn(Opcodes.ICONST_0); // Empuja un 0 (False) a la pila
            return TokenType.PRIMITIVE_BOOLEAN.name();
        }

        return null;
    }
}