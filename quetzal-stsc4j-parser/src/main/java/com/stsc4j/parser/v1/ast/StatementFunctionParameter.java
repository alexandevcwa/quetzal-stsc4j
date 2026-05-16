package com.stsc4j.parser.v1.ast;

import com.stsc4j.lexer.Token;

public class StatementFunctionParameter extends Statement {
    public Token type;
    public boolean mutable;
    public Token identified;

    public StatementFunctionParameter(Token type, boolean mutable, Token identified) {
        this.type = type;
        this.mutable = mutable;
        this.identified = identified;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
