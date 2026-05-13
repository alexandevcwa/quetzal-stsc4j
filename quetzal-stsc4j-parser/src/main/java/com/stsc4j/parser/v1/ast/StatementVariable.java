package com.stsc4j.parser.v1.ast;

import com.stsc4j.lexer.Token;

public class StatementVariable extends Statement {
    public final Token type;
    public final boolean mutable;
    public final Token name;
    public final Token[] assignation;
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
