package com.stsc4j.parser.ast.expression;

import com.stsc4j.parser.ast.Visitor;

/**
 * Representa una expresión unaria.
 * Ejemplos: !verdadero, -5
 */
public class UnaryExpression extends Expression {

    private final String operator;
    private final Expression operand;

    public UnaryExpression(String operator, Expression operand) {
        this.operator = operator;
        this.operand = operand;
    }

    @Override
    public <R> R accept(Visitor<R> visitor) {
        return visitor.visit(this);
    }

    public String getOperator() {
        return operator;
    }

    public Expression getOperand() {
        return operand;
    }
}

