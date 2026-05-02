package com.stsc4j.parser.v1.ast;

import com.stsc4j.lexer.Token;

/**
 * Representa el acceso a una propiedad de un objeto JSN.
 * Ejemplo: persona.nombre, persona.datos_personales.fecha_nacimiento
 */
public class ExpressionPropertyAccess extends Expression {

    public final Expression object;
    public final Token propertyName;

    public ExpressionPropertyAccess(Expression object, Token propertyName) {
        this.object = object;
        this.propertyName = propertyName;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}

