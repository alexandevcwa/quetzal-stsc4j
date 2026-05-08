package com.stsc4j.parser.v1.ast;

import com.stsc4j.lexer.Token;

public class ExpressionVariable extends Expression{

    public final Token token;

    public ExpressionVariable(Token token) {
        this.token = token;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
