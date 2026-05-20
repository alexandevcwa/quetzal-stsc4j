package com.stsc4j.generator.bytecode;

import com.stsc4j.generator.BytecodeAbstractGenerator;
import com.stsc4j.generator.BytecodeGenerator;
import com.stsc4j.lexer.TokenType;
import com.stsc4j.parser.v1.ast.Expression;
import com.stsc4j.parser.v1.ast.ExpressionList;
import com.stsc4j.parser.v1.ast.ExpressionLiteral;
import com.stsc4j.parser.v1.ast.ExpressionVariable;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class BytecodeExpressionList extends BytecodeAbstractGenerator {

    private final BytecodeGenerator generator;

    public BytecodeExpressionList(BytecodeGenerator generator) {
        this.generator = generator;
    }

    @Override
    public String visit(ExpressionList expr) {
        MethodVisitor mv = generator.getMv();
        int size = expr.expressions.size();

        // 1. Si está vacía, creamos un arreglo de Objetos genérico por seguridad
        if (size == 0) {
            pushInt(mv, size);
            mv.visitTypeInsn(Opcodes.ANEWARRAY, "java/lang/Object");
            return "[Ljava/lang/Object;";
        }

        // 2. Deducimos el tipo de la lista basándonos en el primer elemento
        String tipoElemento = deducirTipo(expr.expressions.get(0));
        String tipoArreglo;

        // 3. Empujar el tamaño y crear el arreglo del tipo correcto
        pushInt(mv, size);
        if (TokenType.PRIMITIVE_INTEGER.name().equals(tipoElemento)) {
            mv.visitIntInsn(Opcodes.NEWARRAY, Opcodes.T_INT);
            tipoArreglo = "[I";
        } else if (TokenType.PRIMITIVE_DECIMAL.name().equals(tipoElemento)) {
            mv.visitIntInsn(Opcodes.NEWARRAY, Opcodes.T_FLOAT);
            tipoArreglo = "[F";
        } else if (TokenType.PRIMITIVE_BOOLEAN.name().equals(tipoElemento)) {
            mv.visitIntInsn(Opcodes.NEWARRAY, Opcodes.T_BOOLEAN);
            tipoArreglo = "[Z";
        } else {
            mv.visitTypeInsn(Opcodes.ANEWARRAY, "java/lang/String");
            tipoArreglo = "[Ljava/lang/String;";
        }

        // 4. Llenar el arreglo elemento por elemento
        for (int i = 0; i < size; i++) {
            mv.visitInsn(Opcodes.DUP); // Duplicar la referencia del arreglo para no perderla
            pushInt(mv, i);            // Índice donde vamos a guardar (ej. 0, 1, 2...)

            // Evaluar la expresión del elemento
            String tipoReal = expr.expressions.get(i).accept(generator);

            // Coerción automática (si la lista es de decimales pero nos dan un entero)
            if (TokenType.PRIMITIVE_DECIMAL.name().equals(tipoElemento) && TokenType.PRIMITIVE_INTEGER.name().equals(tipoReal)) {
                mv.visitInsn(Opcodes.I2F);
            }

            // Instrucción de guardado correcta según el tipo
            if (tipoArreglo.equals("[I")) mv.visitInsn(Opcodes.IASTORE);
            else if (tipoArreglo.equals("[F")) mv.visitInsn(Opcodes.FASTORE);
            else if (tipoArreglo.equals("[Z")) mv.visitInsn(Opcodes.BASTORE);
            else mv.visitInsn(Opcodes.AASTORE);
        }

        return tipoArreglo; // Retorna el tipo real ("[I", "[F", etc.) para que el ASTORE de variables sepa qué hacer
    }

    private void pushInt(MethodVisitor mv, int value) {
        if (value >= -1 && value <= 5) mv.visitInsn(Opcodes.ICONST_0 + value);
        else if (value >= Byte.MIN_VALUE && value <= Byte.MAX_VALUE) mv.visitIntInsn(Opcodes.BIPUSH, value);
        else if (value >= Short.MIN_VALUE && value <= Short.MAX_VALUE) mv.visitIntInsn(Opcodes.SIPUSH, value);
        else mv.visitLdcInsn(value);
    }

    private String deducirTipo(Expression expr) {
        if (expr instanceof ExpressionLiteral) {
            return ((ExpressionLiteral) expr).token.getType().name();
        } else if (expr instanceof ExpressionVariable) {
            return generator.getTiposVariables().get(((ExpressionVariable) expr).token.getLexeme());
        }
        return TokenType.PRIMITIVE_STRING.name(); // Default seguro
    }
}