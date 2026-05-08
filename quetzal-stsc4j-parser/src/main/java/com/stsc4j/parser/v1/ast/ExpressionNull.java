package com.stsc4j.parser.v1.ast;

import com.stsc4j.lexer.Token;

public class ExpressionNull extends Expression{
    public final Token token;

    public ExpressionNull(Token token) {
        this.token = token;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
