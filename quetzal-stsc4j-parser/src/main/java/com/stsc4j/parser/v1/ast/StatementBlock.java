package com.stsc4j.parser.v1.ast;

import java.util.List;

public class StatementBlock extends Statement {
    public final List<Statement> statements;

    public StatementBlock(List<Statement> statements) {
        this.statements = statements;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
