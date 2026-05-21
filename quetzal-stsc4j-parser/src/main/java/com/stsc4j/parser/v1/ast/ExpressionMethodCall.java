package com.stsc4j.parser.v1.ast;

import com.stsc4j.lexer.Token;

import java.util.List;

/**
 * Llamadas a métodos de objetos.
 * Ejemplo: `objeto.metodo(arg1, arg2)`
 */
public class ExpressionMethodCall extends Expression {

    /**
     * Objeto al que se le llama el método.
     */
    public final Expression object;

    /**
     * Nombre del métido a llamar.
     */
    public final Token methodName;

    /**
     * Parámetros que se le pasan al método
     */
    public final List<Expression> args;

    public ExpressionMethodCall(Expression object, Token methodName, List<Expression> args) {
        this.object = object;
        this.methodName = methodName;
        this.args = args;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
