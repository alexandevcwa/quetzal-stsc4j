package com.stsc4j.parser.v1.ast;

import com.stsc4j.lexer.Token;

public class StatementVariable extends Statement {
    public final Token typo;
    public final boolean mutable;
    public final Token name;
    public final Expression initialValue;

    public StatementVariable(Token typo, boolean mutable, Token name, Expression initialValue) {
        this.typo = typo;
        this.mutable = mutable;
        this.name = name;
        this.initialValue = initialValue;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
