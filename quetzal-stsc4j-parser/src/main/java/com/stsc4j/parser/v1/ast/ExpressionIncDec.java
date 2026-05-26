package com.stsc4j.parser.v1.ast;

import com.stsc4j.lexer.Token;

public class ExpressionIncDec extends Expression {

    /**
     * Variable a incrementar o decrementar.
     */
    public ExpressionVariable identifier;

    /**
     * Operador de incremento o decremento.
     */
    public Token[] operator = new Token[2];

    public ExpressionIncDec(ExpressionVariable identifier, Token[] operator) {
        this.identifier = identifier;
        this.operator[0] = operator[0];
        this.operator[1] = operator[1];
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
