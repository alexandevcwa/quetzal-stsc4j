package com.stsc4j.parser.v1.ast;

import com.stsc4j.lexer.Token;

public class ExpressionTernary extends Expression {

    public final ExpressionBinary binary;
    public final Expression left;
    public final Expression right;

    public ExpressionTernary(ExpressionBinary binary, Expression left, Expression right) {
        this.binary = binary;
        this.left = left;
        this.right = right;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
