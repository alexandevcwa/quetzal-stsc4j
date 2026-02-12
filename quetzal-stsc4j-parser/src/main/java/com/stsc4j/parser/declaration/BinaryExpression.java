package com.stsc4j.parser.declaration;

import com.stsc4j.parser.ast.Expression;
import com.stsc4j.parser.ast.Visitor;

public class BinaryExpression extends Expression {

    final Expression left, right;
    final String operator;

    public BinaryExpression(Expression left, Expression right, String operator) {
        this.left = left;
        this.right = right;
        this.operator = operator;
    }


    @Override
    protected <R> R accept(Visitor<R> visitor) {
        return null;
    }
}
