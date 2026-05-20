package com.stsc4j.parser.v1.ast;

import com.stsc4j.lexer.Token;

/**
 * Expresión nula.
 */
public class ExpressionNull extends Expression{

    /**
     * Token identificador de la expresión nula
     */
    public final Token token;

    public ExpressionNull(Token token) {
        this.token = token;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
