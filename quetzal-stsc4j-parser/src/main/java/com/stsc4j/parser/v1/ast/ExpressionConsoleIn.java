package com.stsc4j.parser.v1.ast;

/**
 * Nodo para almacenar mensaje para pedir datos por consola, consola.pedir(mensaje)
 */
public class ExpressionConsoleIn extends Expression{

    public final ExpressionLiteral message;

    public ExpressionConsoleIn(ExpressionLiteral message) {
        this.message = message;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
