package com.stsc4j.parser.v1.ast;

public class StatementIncDec extends Statement{
    public final ExpressionIncDec expression;

    public StatementIncDec(ExpressionIncDec expression) {
        this.expression = expression;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
