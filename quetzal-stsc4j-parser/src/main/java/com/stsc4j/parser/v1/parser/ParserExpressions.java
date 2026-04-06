package com.stsc4j.parser.v1.parser;

import com.stsc4j.lexer.Token;
import com.stsc4j.lexer.TokenType;
import com.stsc4j.parser.v1.ast.Expression;
import com.stsc4j.parser.v1.ast.ExpressionBinary;
import com.stsc4j.parser.v1.ast.ExpressionLiteral;
import com.stsc4j.parser.v1.ast.ExpressionVariable;

public class ParserExpressions {

    private final TokenStream tokenStream;

    public ParserExpressions(TokenStream tokenStream) {
        this.tokenStream = tokenStream;
    }

    // Punto de entrada para cualquier expresión
    public Expression parseExpression() {
        return parseEqualExpression();
    }

    // Maneja ==, !=
    private Expression parseEqualExpression() {
        Expression expression = parseRelationalExpression();
        while (tokenStream.match(TokenType.EQUAL, TokenType.EXCLAMATION)) {
            Token operator = tokenStream.show();
            Expression right = parseRelationalExpression();
            expression = new ExpressionBinary(expression, operator, right);
        }
        return expression;
    }

    // Maneja >, <, >=, <=
    private Expression parseRelationalExpression() {
        Expression expression = parseAddAndSubtractExpression();
        while (tokenStream.match(TokenType.GREATER_THAN, TokenType.LESS_THAN, TokenType.EQUAL)) {
            Token operator = tokenStream.before();
            Token secondaryOperator = null;
            if (tokenStream.currentEquals(TokenType.EQUAL)) {
                secondaryOperator = tokenStream.advance();
            }
            Expression right = parseAddAndSubtractExpression();
            if (secondaryOperator != null) {
                final Token[] operators = {operator, secondaryOperator};
                expression = new ExpressionBinary(expression, operators, right);
            } else {
                expression = new ExpressionBinary(expression, operator, right);
            }
        }
        return expression;
    }

    // Maneja + y -
    private Expression parseAddAndSubtractExpression() {
        Expression expression = parseMultiplyAndDivideExpression();
        while (tokenStream.match(TokenType.PLUS, TokenType.MINUS)) {
            Token operator = tokenStream.before();
            Expression right = parseMultiplyAndDivideExpression();
            expression = new ExpressionBinary(expression, operator, right);
        }
        return expression;
    }

    // Maneja * y /
    private Expression parseMultiplyAndDivideExpression() {
        Expression expression = primaryParser();
        while (tokenStream.match(TokenType.MULTIPLY, TokenType.DIVIDE)) {
            Token operator = tokenStream.before();
            Expression right = primaryParser();
            expression = new ExpressionBinary(expression, operator, right);
        }
        return expression;
    }

    private Expression primaryParser() {
        if (tokenStream.match(TokenType.LIT_INTEGER)) {
            Token token = tokenStream.before();
            return new ExpressionLiteral(token, token.getLexeme());
        }
        if (tokenStream.match(TokenType.LIT_DECIMAL)) {
            Token token = tokenStream.before();
            return new ExpressionLiteral(token, token.getLexeme());
        }
        if (tokenStream.match(TokenType.LIT_STRING)) {
            Token token = tokenStream.before();
            return new ExpressionLiteral(token, token.getLexeme());
        }
        if (tokenStream.match(TokenType.LIT_TRUE) || tokenStream.match(TokenType.LIT_FALSE)) {
            Token token = tokenStream.before();
            return new ExpressionLiteral(token, token.getLexeme());
        }
        if (tokenStream.match(TokenType.IDENTIFIER)) {
            return new ExpressionVariable(tokenStream.before());
        }
        if (tokenStream.match(TokenType.LEFT_PARENT)) {
            Expression expression = parseExpression();
            tokenStream.consume(TokenType.RIGHT_PARENT, "Se esperaba ')' después de la expresión.");
            return expression;
        }
        throw new ParserException("Se esperaba una expresión.");
    }
}
