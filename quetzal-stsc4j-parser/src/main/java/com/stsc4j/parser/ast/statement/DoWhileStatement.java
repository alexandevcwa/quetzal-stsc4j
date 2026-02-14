package com.stsc4j.parser.ast.statement;

import com.stsc4j.parser.ast.Visitor;
import com.stsc4j.parser.ast.expression.Expression;

import java.util.List;

/**
 * Representa un bucle do-while.
 * Sintaxis: hacer { ... } mientras (condición)
 */
public class DoWhileStatement extends Statement {

    private final List<Statement> body;
    private final Expression condition;

    public DoWhileStatement(List<Statement> body, Expression condition) {
        this.body = body;
        this.condition = condition;
    }

    @Override
    public <R> R accept(Visitor<R> visitor) {
        return visitor.visit(this);
    }

    public List<Statement> getBody() {
        return body;
    }

    public Expression getCondition() {
        return condition;
    }
}

