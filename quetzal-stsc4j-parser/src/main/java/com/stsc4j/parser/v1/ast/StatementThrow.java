package com.stsc4j.parser.v1.ast;

import com.stsc4j.lexer.Token;

public class StatementThrow extends Statement{

    public final ExpressionLiteral message;

    public StatementThrow(ExpressionLiteral message) {
        this.message = message;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
