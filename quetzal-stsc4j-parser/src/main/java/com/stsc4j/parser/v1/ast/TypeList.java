package com.stsc4j.parser.v1.ast;

public class TypeList extends Type{

    public final Type elementType;

    public TypeList(Type elementType) {
        this.elementType = elementType;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
