package com.stsc4j.parser.v1.ast;

/**
 * Declaración de lanzamiento de excepciones.
 */
public class StatementThrow extends Statement{

    /**
     * Mensaje de salida de la excepción.
     */
    public final ExpressionLiteral message;

    public StatementThrow(ExpressionLiteral message) {
        this.message = message;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
