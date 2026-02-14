package com.stsc4j.parser.ast.expression;

import com.stsc4j.parser.ast.Visitor;

import java.util.List;

/**
 * Representa una expresión de lista.
 * Ejemplo: [1, 2, 3]
 */
public class ListExpression extends Expression {

    private final List<Expression> elements;

    public ListExpression(List<Expression> elements) {
        this.elements = elements;
    }

    @Override
    public <R> R accept(Visitor<R> visitor) {
        return visitor.visit(this);
    }

    public List<Expression> getElements() {
        return elements;
    }
}

