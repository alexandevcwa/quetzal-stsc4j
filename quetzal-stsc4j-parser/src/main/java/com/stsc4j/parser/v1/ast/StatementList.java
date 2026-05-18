package com.stsc4j.parser.v1.ast;

import com.stsc4j.lexer.Token;

public class StatementList extends Statement {

    /**
     * Tipo de dato de la lista
     */
    public final TypeList type;
    /**
     * Indica si la lista es mutable o no.
     */
    public final boolean mutable;
    /**
     * Nombre de la lista.
     */
    public final Token listName;
    /**
     * Lista de expresiones.
     */
    public final ExpressionList expressionList;
    /**
     * Profundidad de anidamiento de la lista.
     */
    public final short depth;

    public StatementList(TypeList type, boolean mutable, Token listName, ExpressionList expressionList, short depth) {
        this.type = type;
        this.mutable = mutable;
        this.listName = listName;
        this.expressionList = expressionList;
        this.depth = depth;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
