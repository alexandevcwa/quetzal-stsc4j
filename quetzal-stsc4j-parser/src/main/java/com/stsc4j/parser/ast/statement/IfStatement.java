package com.stsc4j.parser.ast.statement;

import com.stsc4j.parser.ast.Visitor;
import com.stsc4j.parser.ast.expression.Expression;

import java.util.List;

/**
 * Representa una sentencia condicional if-else.
 * Sintaxis: si (condición) { ... } sino { ... }
 */
public class IfStatement extends Statement {

    private final Expression condition;
    private final List<Statement> thenBranch;
    private final List<Statement> elseBranch;

    public IfStatement(Expression condition, List<Statement> thenBranch, List<Statement> elseBranch) {
        this.condition = condition;
        this.thenBranch = thenBranch;
        this.elseBranch = elseBranch;
    }

    @Override
    public <R> R accept(Visitor<R> visitor) {
        return visitor.visit(this);
    }

    public Expression getCondition() {
        return condition;
    }

    public List<Statement> getThenBranch() {
        return thenBranch;
    }

    public List<Statement> getElseBranch() {
        return elseBranch;
    }
}

