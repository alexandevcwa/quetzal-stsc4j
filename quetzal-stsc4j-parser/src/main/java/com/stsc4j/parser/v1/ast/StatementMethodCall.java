package com.stsc4j.parser.v1.ast;

/**
 * Llamadas a métodos como sentencias.
 * Ejemplo: `miFuncion("Hola, mundo!");`
 */
public class StatementMethodCall extends Statement{

    /**
     * Llamada a método.
     */
    public final ExpressionMethodCall methodCall;

    public StatementMethodCall(ExpressionMethodCall methodCall) {
        this.methodCall = methodCall;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
