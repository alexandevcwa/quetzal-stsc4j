package com.stsc4j.parser.declaration;

import com.stsc4j.parser.ast.Expression;
import com.stsc4j.parser.ast.Visitor;

public class VariableExpression extends Expression {

    final String name;

    public VariableExpression(String name) {
        this.name = name;
    }

    @Override
    protected <R> R accept(Visitor<R> visitor) {
        return null;
    }

    public String getName() {
        return name;
    }
}
