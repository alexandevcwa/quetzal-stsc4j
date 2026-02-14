package com.stsc4j.parser.ast.statement;

import com.stsc4j.parser.ast.Visitor;
import com.stsc4j.parser.ast.expression.Expression;

/**
 * Representa una expresión usada como sentencia.
 * Por ejemplo: llamada a función, asignación, etc.
 */
public class ExpressionStatement extends Statement {

    private final Expression expression;

    public ExpressionStatement(Expression expression) {
        this.expression = expression;
    }

    @Override
    public <R> R accept(Visitor<R> visitor) {
        return visitor.visit(this);
    }

    public Expression getExpression() {
        return expression;
    }
}

