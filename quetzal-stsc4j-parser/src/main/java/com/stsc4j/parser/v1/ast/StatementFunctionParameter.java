package com.stsc4j.parser.v1.ast;

import com.stsc4j.lexer.Token;

/**
 * Parámetro de función
 */
public class StatementFunctionParameter extends Statement {
    /**
     * Tipo de dato del parametro
     */
    public Token type;

    /**
     * Mutabilidad del parámetro
     */
    public boolean mutable;

    /**
     * Nombre del parámetro
     */
    public Token identified;

    public StatementFunctionParameter(Token type, boolean mutable, Token identified) {
        this.type = type;
        this.mutable = mutable;
        this.identified = identified;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
