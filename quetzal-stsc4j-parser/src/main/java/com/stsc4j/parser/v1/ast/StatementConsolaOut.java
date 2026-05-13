package com.stsc4j.parser.v1.ast;

import com.stsc4j.lexer.Token;

public class StatementConsolaOut extends Statement{
    public final Token functionName;
    public final Expression expression;

    public StatementConsolaOut(Token functionName, Expression expression) {
        this.functionName = functionName;
        this.expression = expression;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
