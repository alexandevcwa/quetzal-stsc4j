package com.stsc4j.parser.v1.ast;

/**
 * Sentencia de retorno.
 */
public class StatementReturn extends Statement {

    /**
     * Expresión a retornar. Puede ser null si no se retorna ningún valor.
     */
    public final Expression returnExpression;

    public StatementReturn(Expression returnExpression) {
        this.returnExpression = returnExpression;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}

