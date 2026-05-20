package com.stsc4j.parser.v1.ast;

/**
 * Declaración de bucle while.
 */
public class StatementLoopWhile extends Statement {
    /**
     * Condición del bucle while.
     */
    public final Expression condition;

    /**
     * Bloque del ciclo while.
     */
    public final StatementBlock block;

    public StatementLoopWhile(Expression condition, StatementBlock block) {
        this.condition = condition;
        this.block = block;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
