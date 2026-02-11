package com.stsc4j.parser.declaration;

import com.stsc4j.parser.ast.Expression;
import com.stsc4j.parser.ast.Statement;
import com.stsc4j.parser.ast.Visitor;

public class ReturnStatement extends Statement {

    final Expression expression;

    public ReturnStatement(Expression expression) {
        this.expression = expression;
    }

    @Override
    protected <R> R accept(Visitor<R> visitor) {
        return visitor.visit(this);
    }
}
