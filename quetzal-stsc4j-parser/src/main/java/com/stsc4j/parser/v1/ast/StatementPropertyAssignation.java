package com.stsc4j.parser.v1.ast;

/**
 * Representa la asignación a una propiedad de un objeto.
 * Ejemplo: persona.nombre = "Juan", objeto.propiedad = 123
 */
public class StatementPropertyAssignation extends Statement {

    /**
     * Propiedad a la que se asigna el valor, como ejemplo persona.nombre
     */
    public final ExpressionPropertyAccess property;

    /**
     * Valor que se asigna a la propiedad, como ejemplo "Juan" o 123
     */
    public final Expression expression;

    public StatementPropertyAssignation(ExpressionPropertyAccess property, Expression expression) {
        this.property = property;
        this.expression = expression;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}

