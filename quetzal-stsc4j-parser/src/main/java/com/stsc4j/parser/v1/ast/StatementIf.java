package com.stsc4j.parser.v1.ast;

/**
 * Declaración de sentencia if-else.
 */
public class StatementIf extends Statement {
    /**
     * Condición de la sentencia if-else.
     */
    public final Expression condition;
    /**
     * Bloque si se cumple la condición.
     */
    public final Statement thenStatement;
    /**
     * Bloque si no se cumple la sentencia if-else.
     */
    public final Statement elseStatement;

     public StatementIf(Expression condition, Statement thenStatement, Statement elseStatement) {
         this.condition = condition;
         this.thenStatement = thenStatement;
         this.elseStatement = elseStatement;
     }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
