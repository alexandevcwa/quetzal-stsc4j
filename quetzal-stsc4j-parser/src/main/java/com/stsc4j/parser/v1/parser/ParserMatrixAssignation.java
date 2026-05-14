package com.stsc4j.parser.v1.parser;

import com.stsc4j.lexer.TokenType;
import com.stsc4j.parser.v1.ast.Expression;
import com.stsc4j.parser.v1.ast.ExpressionIndexAccess;
import com.stsc4j.parser.v1.ast.Statement;
import com.stsc4j.parser.v1.ast.StatementMatrixAssignation;
import com.stsc4j.parser.v1.exception.ParserException;

public class ParserMatrixAssignation extends Parser {

    private final TokenStream tokenStream;
    private final ParserExpression parserExpression;

    public ParserMatrixAssignation(TokenStream tokenStream, ParserExpression parserExpression) {
        this.tokenStream = tokenStream;
        this.parserExpression = parserExpression;
    }

    @Override
    public Statement parseStatement() {
        Expression expression = parserExpression.parseExpression();
        if (!(expression instanceof ExpressionIndexAccess)) {
            throw new ParserException("Expected an index access expression for matrix assignation");
        }
        tokenStream.consume(TokenType.EQUAL, "Se esperaba '='");
        Expression rightExpression = parserExpression.parseExpression();
        return new StatementMatrixAssignation((ExpressionIndexAccess) expression, rightExpression);
    }
}
