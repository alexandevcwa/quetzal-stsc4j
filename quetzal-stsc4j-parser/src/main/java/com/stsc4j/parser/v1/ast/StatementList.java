package com.stsc4j.parser.v1.ast;

import com.stsc4j.lexer.Token;

import java.util.List;

public class StatementList extends Statement{
    public final Token type;
    public final boolean mutable;
    public final Token listName;
    public final List<Expression> expressions;

    public StatementList(Token type, boolean mutable, Token listName, List<Expression> expressions) {
        this.type = type;
        this.mutable = mutable;
        this.listName = listName;
        this.expressions = expressions;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
