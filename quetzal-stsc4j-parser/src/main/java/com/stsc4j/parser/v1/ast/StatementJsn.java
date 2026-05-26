package com.stsc4j.parser.v1.ast;

import com.stsc4j.lexer.Token;

/**
 * Declaración de JSN
 */
public class StatementJsn extends Statement{

    /**
     * Identificador de la variable JSN.
     */
    public final Token identifier;

    /**
     * Indica si la variable es mutable o no.
     */
    public final boolean mutable;

    /**
     * Bloque clave/valor JSN
     */
    public final ExpressionJsnBlock block;

    public StatementJsn(Token identifier, boolean mutable, ExpressionJsnBlock block) {
        this.identifier = identifier;
        this.mutable = mutable;
        this.block = block;
    }


    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
