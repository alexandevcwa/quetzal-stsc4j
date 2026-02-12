package com.stsc4j.parser.declaration;

import com.stsc4j.parser.ast.Expression;
import com.stsc4j.parser.ast.Visitor;

import java.util.List;

public class ListExpression extends Expression {

    final List<Expression> elements;

    public ListExpression(List<Expression> elements) {
        this.elements = elements;
    }

    @Override
    protected <R> R accept(Visitor<R> visitor) {
        return null;
    }
}
