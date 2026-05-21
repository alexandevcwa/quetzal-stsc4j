package com.stsc4j.parser.v1.ast;

import com.stsc4j.lexer.Token;

/**
 * Declaración de variable.
 */
public class StatementVariable extends Statement {
    /**
     * Tipo de dato de la variable.
     */
    public final Token type;
    /**
     * Indica si la variable es mutable o no.
     */
    public final boolean mutable;
    /**
     * Nombre de la variable.
     */
    public final Token name;
    /**
     * Tokens de asignación
     */
    public final Token[] assignation;
    /**
     * Valor inicial de la variable.
     */
    public final Expression initialValue;

    public StatementVariable(Token type, boolean mutable, Token name, Token[] assignation, Expression initialValue) {
        this.type = type;
        this.mutable = mutable;
        this.name = name;
        this.assignation = assignation;
        this.initialValue = initialValue;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
