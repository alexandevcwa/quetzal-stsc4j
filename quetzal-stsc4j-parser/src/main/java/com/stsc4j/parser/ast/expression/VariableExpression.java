package com.stsc4j.parser.ast.expression;

import com.stsc4j.parser.ast.Visitor;

public class VariableExpression extends Expression {
    private final String name;
    public VariableExpression(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public <R> R accept(Visitor<R> visitor) {
        return visitor.visit(this);
    }
}
