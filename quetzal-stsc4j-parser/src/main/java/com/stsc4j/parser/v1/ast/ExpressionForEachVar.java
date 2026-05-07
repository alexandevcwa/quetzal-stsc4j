package com.stsc4j.parser.v1.ast;

import com.stsc4j.lexer.Token;

public class ExpressionForEachVar extends Expression{

    public final Token type;
    public final boolean mutable = true;
    public final ExpressionVariable variable;

    public ExpressionForEachVar(Token type, ExpressionVariable variable) {
        this.type = type;
        this.variable = variable;
    }


    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
