package com.stsc4j.parser.v1.parser;

import com.stsc4j.lexer.Token;
import com.stsc4j.lexer.TokenType;
import com.stsc4j.parser.v1.ast.Expression;
import com.stsc4j.parser.v1.ast.Statement;
import com.stsc4j.parser.v1.ast.StatementList;

import java.util.ArrayList;
import java.util.List;

public class ParserArrayExpression {

    private final TokenStream tokenStream;
    private final ParserExpressions parserExpressions;

    public ParserArrayExpression(TokenStream tokenStream, ParserExpressions parserExpressions) {
        this.tokenStream = tokenStream;
        this.parserExpressions = parserExpressions;
    }

    public Statement parseArrayExpression() {
        Token type = null;
        Token identified = null;

        if (tokenStream.match(TokenType.LESS_THAN)) {
            if (tokenStream.match(TokenType.PRIMITIVE_INTEGER, TokenType.PRIMITIVE_DECIMAL, TokenType.PRIMITIVE_STRING,
                    TokenType.PRIMITIVE_BOOLEAN)) {
                type = tokenStream.before();
            } else {
                throw new RuntimeException("Se esperaba un tipo primitivo para la declaración de la lista.");
            }
        } else {
            throw new RuntimeException("Se esperaba '<' después de la palabra reservada 'lista'.");
        }

        if (tokenStream.notMatch(TokenType.GREATER_THAN)) {
            throw new RuntimeException("Se esperaba '>' después de especificar el tipo de 'lista'");
        }

        boolean isMutable = tokenStream.match(TokenType.MUTABLE_VARIABLE);

        if (tokenStream.match(TokenType.IDENTIFIER)) {
            identified = tokenStream.before();
        } else {
            throw new RuntimeException("Se esperaba un identificador para la declaración de la lista.");
        }

        if (tokenStream.notMatch(TokenType.EQUAL)) {
            throw new RuntimeException("Se esperaba '=' después del identificador de la lista.");
        }

        if (tokenStream.notMatch(TokenType.BRACKETS_OPEN)) {
            throw new RuntimeException("Se esperaba '[' para iniciar la declaración de la lista.");
        }

        boolean stop = false;
        List<Expression> elements = new ArrayList<>();
        while (!stop) {
            Expression element = parserExpressions.parseExpression();
            elements.add(element);

            if (tokenStream.match(TokenType.COMMA)) {
                continue;
            } else if (tokenStream.match(TokenType.BRACKETS_CLOSE)) {
                stop = true;
            } else {
                stop = true;
                throw new RuntimeException("Se esperaba ',' o ']' después de cada elemento de la lista.");
            }
        }

        return new StatementList(type, isMutable, identified, elements);
    }

}
