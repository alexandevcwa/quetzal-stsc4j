package com.stsc4j.parser.v1.ast;

/**
 * Tipo de dato para listas de elementos dimensionales y multidimensionales.
 */
public class TypeList extends Type{

    /**
     * Tipo de dato de los elementos de la lista.
     */
    public final Type elementType;

    public TypeList(Type elementType) {
        this.elementType = elementType;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
