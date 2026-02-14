package com.stsc4j.parser.ast.statement;

import com.stsc4j.parser.ast.Visitor;
import com.stsc4j.parser.ast.expression.Expression;

import java.util.List;

/**
 * Representa un bucle for.
 * Sintaxis: para (inicialización; condición; incremento) { ... }
 */
public class ForStatement extends Statement {

    private final Statement initializer;
    private final Expression condition;
    private final Expression increment;
    private final List<Statement> body;

    public ForStatement(Statement initializer, Expression condition, Expression increment, List<Statement> body) {
        this.initializer = initializer;
        this.condition = condition;
        this.increment = increment;
        this.body = body;
    }

    @Override
    public <R> R accept(Visitor<R> visitor) {
        return visitor.visit(this);
    }

    public Statement getInitializer() {
        return initializer;
    }

    public Expression getCondition() {
        return condition;
    }

    public Expression getIncrement() {
        return increment;
    }

    public List<Statement> getBody() {
        return body;
    }
}

