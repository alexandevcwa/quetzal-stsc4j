package com.stsc4j.parser.v1.ast;

public class ExpressionIndexAccess extends Expression{

    public Expression objectList;
    public Expression index;

    public ExpressionIndexAccess(Expression objectList, Expression index) {
        this.objectList = objectList;
        this.index = index;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
