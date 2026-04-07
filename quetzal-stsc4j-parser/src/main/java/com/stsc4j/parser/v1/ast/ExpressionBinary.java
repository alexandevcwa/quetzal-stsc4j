package com.stsc4j.parser.v1.ast;

import com.stsc4j.lexer.Token;

public class ExpressionBinary extends Expression {

    public final Expression left;
    public final Token operator;
    public final Token[] operators;
    public final Expression right;

    public ExpressionBinary(Expression left, Token operator, Expression right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
        this.operators = null;
    }

    public ExpressionBinary(Expression left, Token[] operators, Expression right){
        this.left = left;
        this.operator = null;
        this.right = right;
        this.operators = operators;
    }


    @Override
    public <T> T accept(Visitor<T> visitor) {
        return  visitor.visit(this);
    }

    @Override
    public String toString() {
        return "ExpressionBinary{" +
                "left=" + left +
                ", operator=" + operator +
                ", right=" + right +
                '}';
    }
}
