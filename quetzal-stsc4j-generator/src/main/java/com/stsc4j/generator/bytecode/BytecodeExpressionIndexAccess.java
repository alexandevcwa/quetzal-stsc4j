package com.stsc4j.generator.bytecode;

import com.stsc4j.generator.BytecodeAbstractGenerator;
import com.stsc4j.generator.BytecodeGenerator;
import com.stsc4j.lexer.TokenType;
import com.stsc4j.parser.v1.ast.ExpressionIndexAccess;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class BytecodeExpressionIndexAccess extends BytecodeAbstractGenerator {

    private final BytecodeGenerator generator;

    public BytecodeExpressionIndexAccess(BytecodeGenerator generator) {
        this.generator = generator;
    }

    @Override
    public String visit(ExpressionIndexAccess expr) {
        MethodVisitor mv = generator.getMv();

        // 1. Cargar la referencia de la lista o mapa en la pila
        String tipoBase = expr.objectList.accept(generator);

        // 2. Cargar el índice o clave que queremos leer
        expr.index.get(0).accept(generator);

        // 🚨 CASO NUEVO: Si es un objeto JSN, leemos usando corchetes como diccionario
        if ("JSN_OBJECT".equals(tipoBase)) {
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/util/LinkedHashMap", "get",
                    "(Ljava/lang/Object;)Ljava/lang/Object;", false);
            return "JSN_OBJECT";
        }

        // CASO ORIGINAL: Arreglos tradicionales de Java
        if ("[I".equals(tipoBase)) {
            mv.visitInsn(Opcodes.IALOAD);
            return TokenType.PRIMITIVE_INTEGER.name();
        } else if ("[F".equals(tipoBase)) {
            mv.visitInsn(Opcodes.FALOAD);
            return TokenType.PRIMITIVE_DECIMAL.name();
        } else if ("[Z".equals(tipoBase)) {
            mv.visitInsn(Opcodes.BALOAD);
            return TokenType.PRIMITIVE_BOOLEAN.name();
        } else {
            mv.visitInsn(Opcodes.AALOAD);
            return TokenType.PRIMITIVE_STRING.name();
        }
    }
}