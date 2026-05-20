package com.stsc4j.parser.v1.ast;

import java.util.List;

/**
 * Lista de expresiones.
 */
public class ExpressionList extends Expression{

    /**
     * Items de la lista
     */
    public final List<Expression> expressions;

    /**
     * Profundidad de anidamiento de la lista
     */
    public final short depth;

    public ExpressionList(List<Expression> expressions, short depth) {
        this.expressions = expressions;
        this.depth = depth;
    }


    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
