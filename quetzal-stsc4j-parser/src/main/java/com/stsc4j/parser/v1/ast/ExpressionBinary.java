package com.stsc4j.parser.v1.ast;

import com.stsc4j.lexer.Token;

/**
 * Expresiones binarias
 */
public class ExpressionBinary extends Expression {

    /**
     * Expresión izquierda.
     */
    public final Expression left;

    /**
     * Operadores binarios. Puede haber más de uno por la precedencia de operadores.
     */
    public final Token[] operators;

    /**
     * Expresión derecha.
     */
    public final Expression right;

    public ExpressionBinary(Expression left, Token[] operators, Expression right) {
        this.left = left;
        this.right = right;
        this.operators = operators;
    }


    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
