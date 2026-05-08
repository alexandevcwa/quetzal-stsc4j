package com.stsc4j.parser.v1.parser;

import com.stsc4j.lexer.TokenType;
import com.stsc4j.parser.v1.ast.Expression;
import com.stsc4j.parser.v1.ast.ExpressionIndexAccess;
import com.stsc4j.parser.v1.ast.Statement;
import com.stsc4j.parser.v1.ast.StatementMatrixAssignation;
import com.stsc4j.parser.v1.exception.ParserException;

public class ParserMatrixAssignation extends Parser {

    private final TokenStream tokenStream;
    private final ParserExpressions parserExpressions;

    public ParserMatrixAssignation(TokenStream tokenStream, ParserExpressions parserExpressions) {
        this.tokenStream = tokenStream;
        this.parserExpressions = parserExpressions;
    }

    @Override
    public Statement parseStatement() {
        Expression expression = parserExpressions.parseExpression();
        if (!(expression instanceof ExpressionIndexAccess)) {
            throw new ParserException("Expected an index access expression for matrix assignation");
        }
        tokenStream.consume(TokenType.EQUAL, "Se esperaba '='");
        Expression rightExpression = parserExpressions.parseExpression();
        return new StatementMatrixAssignation((ExpressionIndexAccess) expression, rightExpression);
    }
}
