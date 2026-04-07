package com.stsc4j.parser.v1.ast;

import com.stsc4j.lexer.Token;

import java.util.List;

public class ExpressionMethodCall extends Expression {

    public final Expression object;
    public final Token methodName;
    public final List<Expression> args;

    public ExpressionMethodCall(Expression object, Token methodName, List<Expression> args) {
        this.object = object;
        this.methodName = methodName;
        this.args = args;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
