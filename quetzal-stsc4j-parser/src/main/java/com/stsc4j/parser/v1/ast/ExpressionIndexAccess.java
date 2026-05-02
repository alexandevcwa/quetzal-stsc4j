package com.stsc4j.parser.v1.ast;

import java.util.List;

public class ExpressionIndexAccess extends Expression{

    public Expression objectList;
    public List<Expression> indexList;
    public Expression index;

    @Deprecated
    public ExpressionIndexAccess(Expression objectList, Expression index) {
        this.objectList = objectList;
        this.index = index;
    }

    public ExpressionIndexAccess(Expression objectList, List<Expression> indexList) {
        this.objectList = objectList;
        this.indexList = indexList;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
