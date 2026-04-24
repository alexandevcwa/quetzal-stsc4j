package com.stsc4j.parser.v1.ast;

import com.stsc4j.lexer.Token;

import java.util.List;

public class ExpressionJsn extends Expression{
    public final Token key;
    public final Expression value;
    public final List<Expression> values;

    public ExpressionJsn(Token key, Expression value) {
        this.key = key;
        this.value = value;
        this.values = null;
    }

    public ExpressionJsn(Token key, List<Expression> values){
        this.key = key;
        this.value = null;
        this.values = values;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
