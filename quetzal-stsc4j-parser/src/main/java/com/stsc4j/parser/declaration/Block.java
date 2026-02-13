package com.stsc4j.parser.declaration;

import com.stsc4j.parser.ast.Statement;
import com.stsc4j.parser.ast.Visitor;

import java.util.List;

public class Block extends Statement {

    final List<Statement> statements;

    public Block(List<Statement> statements) {
        this.statements = statements;
    }

    @Override
    public <R> R accept(Visitor<R> visitor) {
        return visitor.visit(this);
    }

    public List<Statement> getStatements() {
        return statements;
    }
}
