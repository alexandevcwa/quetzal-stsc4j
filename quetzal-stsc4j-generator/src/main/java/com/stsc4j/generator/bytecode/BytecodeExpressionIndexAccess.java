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

        // 1. Cargar la referencia de la lista en la pila (esto hará un ALOAD por debajo)
        String tipoArray = expr.objectList.accept(generator);

        // 2. Cargar el índice que queremos leer (ej. el 0)
        expr.indexList.get(0).accept(generator);

        // 3. Extraer el valor (Array Load) dependiendo de la firma secreta del arreglo
        if ("[I".equals(tipoArray)) {
            mv.visitInsn(Opcodes.IALOAD);
            return TokenType.PRIMITIVE_INTEGER.name();
        } else if ("[F".equals(tipoArray)) {
            mv.visitInsn(Opcodes.FALOAD);
            return TokenType.PRIMITIVE_DECIMAL.name();
        } else if ("[Z".equals(tipoArray)) {
            mv.visitInsn(Opcodes.BALOAD);
            return TokenType.PRIMITIVE_BOOLEAN.name();
        } else {
            mv.visitInsn(Opcodes.AALOAD);
            return TokenType.PRIMITIVE_STRING.name();
        }
    }
}