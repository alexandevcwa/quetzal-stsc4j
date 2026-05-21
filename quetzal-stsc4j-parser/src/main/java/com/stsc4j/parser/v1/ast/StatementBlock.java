package com.stsc4j.parser.v1.ast;

import java.util.List;

/**
 * Bloque de sentencias.
 */
public class StatementBlock extends Statement {

    /**
     * Sentencias del bloque.
     */
    public final List<Statement> statements;

    public StatementBlock(List<Statement> statements) {
        this.statements = statements;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
