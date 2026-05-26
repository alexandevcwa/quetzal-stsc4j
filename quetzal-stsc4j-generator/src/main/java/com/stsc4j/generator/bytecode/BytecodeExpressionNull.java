package com.stsc4j.generator.bytecode;

import com.stsc4j.generator.BytecodeAbstractGenerator;
import com.stsc4j.generator.BytecodeGenerator;
import com.stsc4j.lexer.TokenType;
import com.stsc4j.parser.v1.ast.ExpressionNull;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class BytecodeExpressionNull extends BytecodeAbstractGenerator {

    private final BytecodeGenerator generator;

    public BytecodeExpressionNull(BytecodeGenerator generator) {
        this.generator = generator;
    }

    @Override
    public String visit(ExpressionNull expr) {
        MethodVisitor mv = generator.getMv();

        // ACONST_NULL empuja una referencia nula a la cima de la pila
        mv.visitInsn(Opcodes.ACONST_NULL);

        // Como Quetzal aún no tiene objetos complejos (hasta que hagamos JSN),
        // lo más seguro es devolverlo como tipo TEXTO/STRING para que pueda ser asignado a variables.
        return TokenType.PRIMITIVE_STRING.name();
    }
}