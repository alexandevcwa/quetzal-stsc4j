package com.stsc4j.parser.ast.statement;

import com.stsc4j.parser.ast.Visitor;
import com.stsc4j.parser.ast.expression.Expression;

/**
 * Representa una sentencia return.
 * Sintaxis: retornar expresión
 */
public class ReturnStatement extends Statement {

    private final Expression value;

    public ReturnStatement(Expression value) {
        this.value = value;
    }

    @Override
    public <R> R accept(Visitor<R> visitor) {
        return visitor.visit(this);
    }

    public Expression getValue() {
        return value;
    }
}

