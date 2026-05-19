package com.stsc4j.parser.v1.ast;

public class StatementMethodCall extends Statement{

    public final ExpressionMethodCall methodCall;

    public StatementMethodCall(ExpressionMethodCall methodCall) {
        this.methodCall = methodCall;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
