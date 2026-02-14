package com.stsc4j.parser.ast.expression;


import com.stsc4j.parser.ast.Visitor;

public class LiteralExpression extends Expression{

    private final Object value;

    public LiteralExpression(Object value) {
        this.value = value;
    }

    @Override
    public <R> R accept(Visitor<R> visitor) {
        return visitor.visit(this);
    }

    public Object getValue() {
        return value;
    }
}
