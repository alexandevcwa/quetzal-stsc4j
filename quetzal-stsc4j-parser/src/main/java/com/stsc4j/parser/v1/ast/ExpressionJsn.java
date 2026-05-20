package com.stsc4j.parser.v1.ast;

import com.stsc4j.lexer.Token;

import java.util.List;

/**
 * Expresión de tipo JSON, que puede ser un par clave-valor o un par clave-lista, etc.
 */
public class ExpressionJsn extends Expression{
    /**
     * Token que representa la clave del JSON.
     */
    public final Token key;

    /**
     * Valor del JSON, puede ser un Expression o una Lista de Expression.
     */
    public final Expression value;

    /**
     * Lista de valores del JSON.
     */
    public final List<Expression> values;

    public ExpressionJsn(Token key, Expression value) {
        this.key = key;
        this.value = value;
        this.values = null;
    }

    public ExpressionJsn(Token key, List<Expression> values){
        this.key = key;
        this.value = null;
        this.values = values;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
