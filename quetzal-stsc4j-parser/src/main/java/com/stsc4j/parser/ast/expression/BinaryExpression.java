package com.stsc4j.parser.ast.expression;


import com.stsc4j.parser.ast.Visitor;

public class BinaryExpression extends Expression {

    private final Expression left, right;
    private final String operator;

    public BinaryExpression(Expression left, Expression right, String operator) {
        this.left = left;
        this.right = right;
        this.operator = operator;
    }


    public Expression getLeft() {
        return left;
    }

    public Expression getRight() {
        return right;
    }

    public String getOperator() {
        return operator;
    }

    @Override
    public <R> R accept(Visitor<R> visitor) {
        return visitor.visit(this);
    }
}
