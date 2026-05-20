package com.stsc4j.parser.v1.ast;

import com.stsc4j.lexer.Token;

/**
 * Expresiones literales.
 */
public class ExpressionLiteral extends Expression{
    /**
     * Token que representa el valor.
     */
    public final Token token;

    /**
     * Valor del literal.
     */
    public final Object value;

    public ExpressionLiteral(Token token, Object value) {
        this.token = token;
        this.value = value;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
