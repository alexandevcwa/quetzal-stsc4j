package com.stsc4j.parser.v1.parser;

import com.stsc4j.lexer.Token;
import com.stsc4j.lexer.TokenType;
import com.stsc4j.parser.v1.ast.*;
import com.stsc4j.parser.v1.parser.expression.ParseGenericExpression;
import com.stsc4j.parser.v1.parser.expression.ParseTernaryExpression;

import java.util.ArrayList;
import java.util.List;

public class ParserExpressions implements ParseGenericExpression, ParseTernaryExpression {

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
        Expression expression = parseMethodCall();
        while (tokenStream.match(TokenType.MULTIPLY, TokenType.DIVIDE)) {
            Token operator = tokenStream.before();
            Expression right = parseMethodCall();
            expression = new ExpressionBinary(expression, operator, right);
        }
        return expression;
    }

    /**
     * Analiza y procesa una lista de argumentos a partir de la secuencia de tokens actual.
     * Los argumentos son interpretados como expresiones y se agregan a una lista en el
     * orden en que se presentan en la entrada.
     *
     * @return Una lista de objetos {@code Expression} que representan las expresiones
     * extraídas como argumentos. Si no hay argumentos presentes, retorna una lista vacía.
     */
    private List<Expression> parseArgs() {
        List<Expression> args = new ArrayList<>();
        if (!tokenStream.matchButNotAdvance(TokenType.RIGHT_PARENT)) {
            do {
                args.add(parseExpression());
            } while (tokenStream.match(TokenType.COMMA));
        }
        return args;
    }

    /**
     * Analiza y construye una representación de llamada a método basada en la secuencia
     * de tokens actual. Procesa llamadas a métodos siguiendo la estructura de acceso
     * mediante el operador '.' y maneja argumentos cuando están disponibles.
     * <p>
     * Durante el análisis:
     * - Identifica el objeto inicial al que se aplicarán las llamadas.
     * - Busca llamadas a métodos adicionales conectadas por '.'.
     * - Procesa los argumentos de las llamadas a métodos cuando se encuentran paréntesis
     * que los rodean.
     *
     * @return Una instancia de {@code Expression} que modela una llamada a método, incluyendo
     * el objeto, el nombre del método y los argumentos, si existen.
     */
    private Expression parseMethodCall() {
        Expression expression = primaryParser();
        while (tokenStream.match(TokenType.DOT)) {
            Token methodName = tokenStream.consume(TokenType.IDENTIFIER, "Se esperaba el nombre del método después del '.'");
            if (tokenStream.match(TokenType.LEFT_PARENT)) {
                List<Expression> args = parseArgs();
                tokenStream.consume(TokenType.RIGHT_PARENT, "Se esperaba ')' después de los argumentos.");
                expression = new ExpressionMethodCall(expression, methodName, args);
            }
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


    @Override
    public Expression parseGenericExpression() {
        return parseEqualExpression();
    }

    @Override
    public Expression parseTernaryExpression(ExpressionBinary expression) {
        Expression left = primaryParser();
        tokenStream.match(TokenType.DOUBLE_DOT, "Se esperaba ':' después de la expresión del medio en el operador ternario.");
        Expression right = primaryParser();
        return new ExpressionTernary(expression, left, right);
    }
}
