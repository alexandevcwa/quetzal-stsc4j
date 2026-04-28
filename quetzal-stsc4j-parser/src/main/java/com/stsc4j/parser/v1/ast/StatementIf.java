package com.stsc4j.parser.v1.ast;

public class StatementIf extends Statement {
    public final Expression condition;
    public final Statement thenStatement;
    public final Statement elseStatement;

     public StatementIf(Expression condition, Statement thenStatement, Statement elseStatement) {
         this.condition = condition;
         this.thenStatement = thenStatement;
         this.elseStatement = elseStatement;
     }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
