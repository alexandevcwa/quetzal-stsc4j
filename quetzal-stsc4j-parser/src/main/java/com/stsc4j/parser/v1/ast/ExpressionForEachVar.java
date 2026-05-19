package com.stsc4j.parser.v1.ast;

import com.stsc4j.lexer.Token;

public class ExpressionForEachVar extends Expression{

    /**
     * Tipo de dato de la variable del foreach
     */
    public final Token type;

    /**
     * Indica si la variable es mutable o no.
     */
    public final boolean mutable = true;

    /**
     * Nombre de la variable del foreach
     */
    public final Token variable;

    public ExpressionForEachVar(Token type, Token variable) {
        this.type = type;
        this.variable = variable;
    }


    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
