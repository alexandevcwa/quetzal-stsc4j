package com.stsc4j.parser.v1.ast;

import java.util.List;

/**
 * Un bloque de expresiones.
 */
public class ExpressionJsnBlock extends Expression{

    /**
     * Lista de expresiones.
     */
    public final List<ExpressionJsn> expressions;

    public ExpressionJsnBlock(List<ExpressionJsn> expressions) {
        this.expressions = expressions;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
