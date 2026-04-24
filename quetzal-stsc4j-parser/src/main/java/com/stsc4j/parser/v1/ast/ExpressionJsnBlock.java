package com.stsc4j.parser.v1.ast;

import java.util.List;

public class ExpressionJsnBlock extends Expression{

    public final List<ExpressionJsn> expressions;

    public ExpressionJsnBlock(List<ExpressionJsn> expressions) {
        this.expressions = expressions;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
