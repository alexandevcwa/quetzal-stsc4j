package com.stsc4j.parser.v1.ast;

/**
 * Declaración de incremento/decremento i++, i--.
 */
public class StatementIncDec extends Statement{

    /**
     * Expresión de incremento/decremento.
     */
    public final ExpressionIncDec expression;

    public StatementIncDec(ExpressionIncDec expression) {
        this.expression = expression;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
