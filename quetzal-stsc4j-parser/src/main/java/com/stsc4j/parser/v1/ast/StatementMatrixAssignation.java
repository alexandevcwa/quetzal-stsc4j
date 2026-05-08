package com.stsc4j.parser.v1.ast;

public class StatementMatrixAssignation extends Statement {

    public final ExpressionIndexAccess matrix;

    public final Expression expression;

    public StatementMatrixAssignation(ExpressionIndexAccess matrix, Expression expression) {
        this.matrix = matrix;
        this.expression = expression;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
