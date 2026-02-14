package com.stsc4j.parser.parser;

import com.stsc4j.lexer.TokenType;
import com.stsc4j.parser.ast.expression.*;
import com.stsc4j.parser.exception.ParseException;

import java.util.ArrayList;
import java.util.List;

/**
 * Parser de expresiones con precedencia de operadores.
 * Jerarquía de precedencia (de menor a mayor):
 * 1. OR (||)
 * 2. AND (&&)
 * 3. Igualdad (==, !=)
 * 4. Comparación (<, >, <=, >=)
 * 5. Suma/Resta (+, -)
 * 6. Multiplicación/División (*, /, %)
 * 7. Unarios (!, -)
 * 8. Primarios (literales, variables, paréntesis)
 */
public class ExpressionParser {
    private final ParserContext context;

    public ExpressionParser(ParserContext context) {
        this.context = context;
    }

    /**
     * Punto de entrada para parsear expresiones.
     */
    public Expression parseExpression() {
        return parseOr();
    }

    /**
     * Parsea expresiones OR (||)
     */
    private Expression parseOr() {
        Expression left = parseAnd();
        while (context.match(TokenType.OR)) {
            String operator = context.previous().getLexeme();
            Expression right = parseAnd();
            left = new BinaryExpression(left, right, operator);
        }
        return left;
    }

    /**
     * Parsea expresiones AND (&&)
     */
    private Expression parseAnd() {
        Expression left = parseEquality();
        while (context.match(TokenType.AND)) {
            String operator = context.previous().getLexeme();
            Expression right = parseEquality();
            left = new BinaryExpression(left, right, operator);
        }
        return left;
    }

    /**
     * Parsea expresiones de igualdad (==, !=)
     */
    private Expression parseEquality() {
        Expression left = parseComparison();
        // TODO: Agregar TokenType.EQUAL_EQUAL y TokenType.NOT_EQUAL cuando estén disponibles
        return left;
    }

    /**
     * Parsea expresiones de comparación (<, >, <=, >=)
     */
    private Expression parseComparison() {
        Expression left = parseTerm();
        while (context.match(TokenType.LESS_THAN, TokenType.GREATER_THAN)) {
            String operator = context.previous().getLexeme();
            Expression right = parseTerm();
            left = new BinaryExpression(left, right, operator);
        }
        return left;
    }

    /**
     * Parsea expresiones de suma y resta (+, -)
     */
    private Expression parseTerm() {
        Expression left = parseFactor();
        while (context.match(TokenType.PLUS, TokenType.MINUS)) {
            String operator = context.previous().getLexeme();
            Expression right = parseFactor();
            left = new BinaryExpression(left, right, operator);
        }
        return left;
    }

    /**
     * Parsea expresiones de multiplicación, división y módulo (*, /, %)
     */
    private Expression parseFactor() {
        Expression left = parseUnary();
        while (context.match(TokenType.MULTIPLY, TokenType.DIVIDE, TokenType.MODULE)) {
            String operator = context.previous().getLexeme();
            Expression right = parseUnary();
            left = new BinaryExpression(left, right, operator);
        }
        return left;
    }

    /**
     * Parsea expresiones unarias (!, -)
     */
    private Expression parseUnary() {
        if (context.match(TokenType.EXCLAMATION, TokenType.MINUS)) {
            String operator = context.previous().getLexeme();
            Expression right = parseUnary();
            return new UnaryExpression(operator, right);
        }
        return parsePrimary();
    }

    /**
     * Parsea expresiones primarias (literales, variables, paréntesis)
     */
    private Expression parsePrimary() {
        // Literales numéricos
        if (context.match(TokenType.LIT_LONG)) {
            return new LiteralExpression(Long.parseLong(context.previous().getLexeme()));
        }
        if (context.match(TokenType.LIT_INT)) {
            return new LiteralExpression(Integer.parseInt(context.previous().getLexeme()));
        }
        if (context.match(TokenType.LIT_SHORT)) {
            return new LiteralExpression(Short.parseShort(context.previous().getLexeme()));
        }
        if (context.match(TokenType.LIT_DOUBLE)) {
            return new LiteralExpression(Double.parseDouble(context.previous().getLexeme()));
        }
        if (context.match(TokenType.LIT_FLOAT)) {
            return new LiteralExpression(Float.parseFloat(context.previous().getLexeme()));
        }

        // Literal string
        if (context.match(TokenType.LIT_STRING)) {
            return new LiteralExpression(context.previous().getLexeme());
        }

        // Literales booleanos
        if (context.match(TokenType.LIT_TRUE)) {
            return new LiteralExpression(true);
        }
        if (context.match(TokenType.LIT_FALSE)) {
            return new LiteralExpression(false);
        }

        // Literal nulo
        if (context.match(TokenType.NULL)) {
            return new LiteralExpression(null);
        }

        // Identificador (variable)
        if (context.match(TokenType.IDENTIFIER)) {
            return new VariableExpression(context.previous().getLexeme());
        }

        // Expresión entre paréntesis
        if (context.match(TokenType.LEFT_PARENT)) {
            Expression expr = parseExpression();
            context.consume(TokenType.RIGHT_PARENT, "Se esperaba ')' después de la expresión");
            return expr;
        }

        // Literal de lista [1, 2, 3]
        if (context.match(TokenType.LEFT_BRACKET)) {
            return parseListLiteral();
        }

        throw new ParseException("Se esperaba una expresión en: " + context.peek().getLexeme());
    }

    /**
     * Parsea un literal de lista: [expr1, expr2, ...]
     */
    private Expression parseListLiteral() {
        List<Expression> elements = new ArrayList<>();

        // Lista vacía []
        if (!context.check(TokenType.RIGHT_BRACKET)) {
            do {
                elements.add(parseExpression());
            } while (context.match(TokenType.COMMA));
        }

        context.consume(TokenType.RIGHT_BRACKET, "Se esperaba ']' al final de la lista");
        return new ListExpression(elements);
    }
}
