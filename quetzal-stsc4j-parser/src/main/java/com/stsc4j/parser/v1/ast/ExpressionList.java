package com.stsc4j.parser.v1.ast;

import java.util.List;

public class ExpressionList extends Expression{

    public List<Expression> expressions;

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
