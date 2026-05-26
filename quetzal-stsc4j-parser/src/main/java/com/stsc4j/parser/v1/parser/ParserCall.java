package com.stsc4j.parser.v1.parser;

import com.stsc4j.lexer.TokenType;
import com.stsc4j.parser.v1.ast.*;
import com.stsc4j.parser.v1.exception.ParserException;

public class ParserCall extends Parser {

    private final TokenStream tokenStream;
    private final ParserExpression parserExpression;

    public ParserCall(TokenStream tokenStream, ParserExpression parserExpression) {
        this.tokenStream = tokenStream;
        this.parserExpression = parserExpression;
    }

    @Override
    public Statement parseStatement() {
        Expression expression = parserExpression.parseExpression();
        if (expression instanceof ExpressionPropertyAccess) {
            return parseExpressionPropertyAccess(expression);
        } else if (expression instanceof ExpressionMethodCall) {
            return parseExpressionMethodCall(expression);
        } else {
            throw new ParserException("Se esperaba una llamada a método o acceso a propiedad");
        }
    }

    private Statement parseExpressionPropertyAccess(Expression expression) {
        tokenStream.consume(TokenType.EQUAL, "Se esperaba '=' despues de la llamada a una propiedad");
        Expression rightExpression = parserExpression.parseExpression();
        return new StatementPropertyAssignation((ExpressionPropertyAccess) expression, rightExpression);
    }

    private Statement parseExpressionMethodCall(Expression expression) {
        return new StatementMethodCall((ExpressionMethodCall) expression);
    }
}

