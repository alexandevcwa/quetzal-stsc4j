package com.stsc4j.parser.v1.ast;

import com.stsc4j.lexer.Token;

import java.util.List;

/**
 * Expressión para llamadas a métodos predefinidos en quetzal.
 */
public class ExpressionMethodCallPredefined extends ExpressionMethodCall{

    public ExpressionMethodCallPredefined(Expression object, Token methodName, List<Expression> args) {
        super(object, methodName, args);
    }
}
