package com.stsc4j.parser.declaration;

import com.stsc4j.parser.ast.Expression;
import com.stsc4j.parser.ast.Visitor;

public class LiteralExpression extends Expression {

    final Object value;

    public LiteralExpression(Object value) {
        this.value = value;
    }

    @Override
    protected <R> R accept(Visitor<R> visitor) {
        return visitor.visit(this);
    }

    public Object getValue() {
        return value;
    }
}
