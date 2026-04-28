package com.stsc4j.parser.v1.parser;

import com.stsc4j.lexer.Token;
import com.stsc4j.lexer.TokenType;
import com.stsc4j.parser.v1.ast.*;
import com.stsc4j.parser.v1.exception.ParserException;

import java.util.ArrayList;
import java.util.List;

public class ParserExpressions extends Parser {

    private final TokenStream tokenStream;

    public ParserExpressions(TokenStream tokenStream) {
        this.tokenStream = tokenStream;
    }

    // Punto de entrada para cualquier expresión
    @Override
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
        if (!tokenStream.matchNotAdvance(TokenType.RIGHT_PARENT)) {
            do {
                args.add(parseExpression());
            } while (tokenStream.match(TokenType.COMMA));
        }
        return args;
    }

    // Manejar llamadas a métodos, con y sin parámetros, y encadenamiento de llamadas 'obj.method1().method2()'
    private Expression parseMethodCall() {
        Expression expression = parseIndexAccess();
        Token before = tokenStream.before();
        // Controlar funciones como mifuncion()
        if (tokenStream.match(TokenType.LEFT_PARENT)) {
            List<Expression> args = parseArgs();
            tokenStream.consume(TokenType.RIGHT_PARENT, "Se esperaba ')' después de los argumentos.");
            expression = new ExpressionMethodCall(expression, before, args);
        } else {
            // Controlar funciones como mifuncion.method1()
            while (tokenStream.match(TokenType.DOT)) {
                Token methodName = tokenStream.consume(TokenType.IDENTIFIER, "Se esperaba el nombre del método después del '.'");
                if (tokenStream.match(TokenType.LEFT_PARENT)) {
                    List<Expression> args = parseArgs();
                    tokenStream.consume(TokenType.RIGHT_PARENT, "Se esperaba ')' después de los argumentos.");
                    expression = new ExpressionMethodCall(expression, methodName, args);
                }
            }
        }
        return expression;
    }

    // Manejar expresiones de acceso a índices 'lista[1]'
    private Expression parseIndexAccess() {
        Expression expression = primaryParser();
        if (tokenStream.match(TokenType.BRACKETS_OPEN)) {
            Expression idx = parseExpression();
            tokenStream.consume(TokenType.BRACKETS_CLOSE, "Se esperaba ']' después del índice.");
            expression = new ExpressionIndexAccess(expression, idx);
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

    /**
     * Analiza y construye una expresión ternaria a partir de una expresión binaria inicial
     * y las expresiones adicionales necesarias para completar la estructura ternaria.
     * <p>
     * La expresión ternaria tiene la forma: <condición> ? <expresión1> : <expresión2>.
     * Este método se encarga de procesar los componentes de la expresión y validarlos.
     *
     * @param expression La expresión binaria que representa la condición de la expresión ternaria.
     * @return Una instancia de {@code ExpressionTernary} que modela la expresión ternaria completa
     * construida a partir de la condición y las expresiones adicionales.
     * @throws RuntimeException Si no se encuentra el separador ':' o si las expresiones no son válidas.
     */
    public Expression parseTernaryExpression(ExpressionBinary expression) {
        Expression left = primaryParser();
        tokenStream.match(TokenType.DOUBLE_DOT, "Se esperaba ':' después de la expresión del medio en el operador ternario.");
        Expression right = primaryParser();
        return new ExpressionTernary(expression, left, right);
    }
}
