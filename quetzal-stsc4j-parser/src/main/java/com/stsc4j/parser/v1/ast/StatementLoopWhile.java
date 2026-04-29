package com.stsc4j.parser.v1.ast;

public class StatementLoopWhile extends Statement {
    public final Expression condition;
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
