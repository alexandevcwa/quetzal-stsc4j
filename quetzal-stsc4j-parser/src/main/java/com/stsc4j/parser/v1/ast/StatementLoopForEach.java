package com.stsc4j.parser.v1.ast;

/**
 * Ciclo for-each.
 */
public class StatementLoopForEach extends Statement{
    /**
     * Expresión inicial de variable del for-each
     */
    public final ExpressionForEachVar declaration;

    /**
     * Variable que representa la lista a iterar en el for-each
     */
    public final ExpressionVariable listVariable;

    /**
     * Bloque del ciclo for-each
     */
    public final StatementBlock block;

    public StatementLoopForEach(ExpressionForEachVar declaration, ExpressionVariable listVariable, StatementBlock block) {
        this.declaration = declaration;
        this.listVariable = listVariable;
        this.block = block;
    }


    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
