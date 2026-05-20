package com.stsc4j.parser.v1.ast;

/**
 * Expresiones ternarias.
 */
public class ExpressionTernary extends Expression {

    /**
     * Expresión binaria que representa la condición de la expresión ternaria.
     */
    public final ExpressionBinary binary;

    /**
     * Expresión de retorno principal si la condición cumple con la expresión binaria.
     */
    public final Expression left;

    /**
     * Expresión de retorno si la condición no cumple con la expresión binaria.
     */
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
