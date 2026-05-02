package com.stsc4j.parser.v1.ast;

public class StatementReturn extends Statement {
    public final Expression returnExpression;

    public StatementReturn(Expression returnExpression) {
        this.returnExpression = returnExpression;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}

