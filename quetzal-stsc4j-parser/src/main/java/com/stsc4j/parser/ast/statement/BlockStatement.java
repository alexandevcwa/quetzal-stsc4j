package com.stsc4j.parser.ast.statement;

import com.stsc4j.parser.ast.Visitor;

import java.util.List;

/**
 * Representa un bloque de código delimitado por llaves.
 * Sintaxis: { sentencia1; sentencia2; ... }
 */
public class BlockStatement extends Statement {

    private final List<Statement> statements;

    public BlockStatement(List<Statement> statements) {
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

