package com.stsc4j.parser.v1.ast;

/**
 * Declaración de asignación a una posición de una matriz, por ejemplo: a[0][1] = 5;
 */
public class StatementMatrixAssignation extends Statement {

    /**
     * Matriz a la que se asigna el valor.
     */
    public final ExpressionIndexAccess matrix;

    /**
     * Valor que se asigna a la matriz.
     */
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
