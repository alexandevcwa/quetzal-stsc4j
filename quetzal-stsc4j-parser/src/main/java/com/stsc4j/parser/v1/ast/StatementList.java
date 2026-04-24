package com.stsc4j.parser.v1.ast;

import com.stsc4j.lexer.Token;

import java.util.List;

public class StatementList extends Statement {
    public final TypeList type;
    public final boolean mutable;
    public final Token listName;
    public final ExpressionList expressionList;

    public StatementList(TypeList type, boolean mutable, Token listName, ExpressionList expressionList) {
        this.type = type;
        this.mutable = mutable;
        this.listName = listName;
        this.expressionList = expressionList;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
