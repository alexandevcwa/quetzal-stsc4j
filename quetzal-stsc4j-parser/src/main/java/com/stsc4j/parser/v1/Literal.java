package com.stsc4j.parser.v1;

// Nodo para literales de Quetzal (números, texto, verdadero, falso, nulo)
public class Literal extends  Expr{

    final Object value;

    Literal(Object value) {
        this.value = value;
    }

    @Override
    <R> R accept(Visitor<R> visitor) {
        return visitor.visitLiteralExpr(this);
    }
}
