package com.stsc4j.parser.v1.ast;

import com.stsc4j.lexer.Token;

/**
 * Sentencia de salida de datos por consola.
 */
public class StatementConsolaOut extends Statement{
    /**
     * Nombre de la función predefinida que imprime por consola.
     */
    public final Token functionName;

    /**
     * Expresión de salida por consola.
     */
    public final Expression expression;

    public StatementConsolaOut(Token functionName, Expression expression) {
        this.functionName = functionName;
        this.expression = expression;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
