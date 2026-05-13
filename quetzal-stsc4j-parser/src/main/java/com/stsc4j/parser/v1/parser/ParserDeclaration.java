package com.stsc4j.parser.v1.parser;

import com.stsc4j.lexer.Token;
import com.stsc4j.lexer.TokenType;
import com.stsc4j.parser.v1.exception.ParserException;
import com.stsc4j.parser.v1.ast.*;

public class ParserDeclaration extends Parser {
    private final TokenStream tokenStream;
    private final ParserExpression parserExpression;

    public ParserDeclaration(TokenStream tokenStream, ParserExpression parserExpression) {
        this.tokenStream = tokenStream;
        this.parserExpression = parserExpression;
    }


    @Override
    public Statement parseStatement() {
        if (tokenStream.notMatch(TokenType.PRIMITIVE_INTEGER, TokenType.PRIMITIVE_DECIMAL, TokenType.PRIMITIVE_STRING,
                TokenType.PRIMITIVE_BOOLEAN, TokenType.IDENTIFIER)) {
            System.out.println(tokenStream.before().getLexeme());
            throw new ParserException("Se esperaba una declaración de variable con 'var' o una asignación a una variable ya declarada.");
        }

        // Tipo de dato
        Token type = tokenStream.before();

        // Variable mutable o inmutable
        boolean isMutable = false;

        // Nombre de la variable
        Token name = null;

        // Valor inicial de la variable
        Expression initialValue;

        Token[] assignation = null;

        // Asignar nuevo valor a una variable ya declarada
        if (type.getType().equals(TokenType.IDENTIFIER)) {
            name = type;
            if (tokenStream.match(TokenType.EQUAL)) {
                assignation = new Token[]{tokenStream.before()};
            } else {
                assignation = obtainAssignation();
            }
        } else {
            // Nueva variable declarada
            isMutable = tokenStream.match(TokenType.MUTABLE_VARIABLE);
            name = tokenStream.consume(TokenType.IDENTIFIER, "Se esperaba el nombre de la variable.");
            tokenStream.match(TokenType.EQUAL, "Se esperaba '=' después del nombre de la variable o luego de 'var'.");
        }

        initialValue = parserExpression.parseExpression();

        Expression ternary = null;
        // Verificar operador ternario
        if (tokenStream.matchNotAdvance(TokenType.QUESTION)) {
            tokenStream.advance();
            ternary = parserExpression.parseTernaryExpression((ExpressionBinary) initialValue);
        }

        return new StatementVariable(type, isMutable, name,assignation,
                ternary != null ? ternary : initialValue
        );
    }

    private Token[] obtainAssignation() {
        if (tokenStream.match(TokenType.PLUS, TokenType.MINUS, TokenType.MULTIPLY, TokenType.DIVIDE, TokenType.MODULE)) {
            Token fistOperator = tokenStream.before();
            Token secondOperator = tokenStream.consume(TokenType.EQUAL, "Se esperaba '=' después del operador aritmetico para la asignación.");
            return new Token[]{fistOperator, secondOperator};
        } else {
            throw new ParserException("Se esperaba un operador aritmetico.");
        }
    }
}
