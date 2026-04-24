package com.stsc4j.parser.v1.ast;

import com.stsc4j.lexer.Token;

public class StatementJsn extends Statement{

    public final Token identifier;
    public final boolean mutable;
    public final ExpressionJsnBlock block;

    public StatementJsn(Token identifier, boolean mutable, ExpressionJsnBlock block) {
        this.identifier = identifier;
        this.mutable = mutable;
        this.block = block;
    }


    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
