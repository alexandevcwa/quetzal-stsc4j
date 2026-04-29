package com.stsc4j.parser.v1.ast;

public class StatementLoopDoWhile extends StatementLoopWhile {

    public StatementLoopDoWhile(Expression condition, StatementBlock block) {
        super(condition, block);
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
