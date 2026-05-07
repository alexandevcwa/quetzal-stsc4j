package com.stsc4j.parser.v1.ast;

public class StatementExpression extends Statement {
    public final Expression expression;

    public StatementExpression(Expression expression) {
        this.expression = expression;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
