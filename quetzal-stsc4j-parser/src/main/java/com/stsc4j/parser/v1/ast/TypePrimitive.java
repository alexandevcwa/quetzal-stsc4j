package com.stsc4j.parser.v1.ast;

import com.stsc4j.lexer.Token;

/**
 * Tipo de dato que almacena una lista
 */
public class TypePrimitive extends Type{

    public final Token primitiveType;

    public TypePrimitive(Token primitiveType) {
        this.primitiveType = primitiveType;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
