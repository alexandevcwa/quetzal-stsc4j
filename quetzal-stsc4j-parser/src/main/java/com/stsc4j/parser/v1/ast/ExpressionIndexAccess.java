package com.stsc4j.parser.v1.ast;

import java.util.List;

/**
 * Acceso a un indice de una lista.
 */
public class ExpressionIndexAccess extends Expression{

    /**
     * Objeto a acceder.
     */
    public Expression objectList;

    /**
     * Lista de indices.
     */
    public List<Expression> index;

    public ExpressionIndexAccess(Expression objectList, List<Expression> index) {
        this.objectList = objectList;
        this.index = index;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
