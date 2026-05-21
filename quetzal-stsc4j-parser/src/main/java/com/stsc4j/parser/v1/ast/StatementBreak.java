package com.stsc4j.parser.v1.ast;

import com.stsc4j.lexer.Token;

/**
 * Sentencia de salto.
 */
public class StatementBreak extends Statement{

    /**
     * Token identificador de la sentencia de salto.
     */
    public final Token token;

    public StatementBreak(Token token) {
        this.token = token;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
