package com.stsc4j.parser.v1.ast;

import com.stsc4j.lexer.Token;

public class ExpressionLiteral extends Expression{
    public final Token token;
    public final Object value;

    public ExpressionLiteral(Token token, Object value) {
        this.token = token;
        this.value = value;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return "ExpressionLiteral{" +
                "token=" + token +
                ", value=" + value +
                '}';
    }
}
