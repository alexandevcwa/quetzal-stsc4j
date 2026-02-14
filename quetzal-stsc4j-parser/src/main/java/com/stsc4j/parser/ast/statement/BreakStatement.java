package com.stsc4j.parser.ast.statement;

import com.stsc4j.parser.ast.Visitor;

/**
 * Representa una sentencia break.
 * Sintaxis: romper
 */
public class BreakStatement extends Statement {

    @Override
    public <R> R accept(Visitor<R> visitor) {
        return visitor.visit(this);
    }
}

