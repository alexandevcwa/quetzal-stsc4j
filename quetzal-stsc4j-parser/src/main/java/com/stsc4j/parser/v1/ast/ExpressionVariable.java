package com.stsc4j.parser.v1.ast;

import com.stsc4j.lexer.Token;

public class ExpressionVariable extends Expression {

    /**
     * Token que representa la variable.
     */
    public final Token token;

    /**
     * Indica si la variable es negada, es decir, si se encuentra precedida por un operador de negación lógico (!).
     */
    public final boolean negation;

    public ExpressionVariable(Token token, boolean negation) {
        this.token = token;
        this.negation = negation;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
