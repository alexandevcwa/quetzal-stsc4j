package com.stsc4j.parser.v1.parser;

import com.stsc4j.lexer.TokenType;
import com.stsc4j.parser.v1.ast.Expression;
import com.stsc4j.parser.v1.ast.ExpressionPropertyAccess;
import com.stsc4j.parser.v1.ast.Statement;
import com.stsc4j.parser.v1.ast.StatementPropertyAssignation;
import com.stsc4j.parser.v1.exception.ParserException;

public class ParserPropertyAssignation extends Parser {

    private final TokenStream tokenStream;
    private final ParserExpression parserExpression;

    public ParserPropertyAssignation(TokenStream tokenStream, ParserExpression parserExpression) {
        this.tokenStream = tokenStream;
        this.parserExpression = parserExpression;
    }

    @Override
    public Statement parseStatement() {
        Expression expression = parserExpression.parseExpression();
        if (!(expression instanceof ExpressionPropertyAccess)) {
            throw new ParserException("Se esperaba una expresión de acceso a propiedad para la asignación");
        }
        tokenStream.consume(TokenType.EQUAL, "Se esperaba '='");
        Expression rightExpression = parserExpression.parseExpression();
        return new StatementPropertyAssignation((ExpressionPropertyAccess) expression, rightExpression);
    }
}

