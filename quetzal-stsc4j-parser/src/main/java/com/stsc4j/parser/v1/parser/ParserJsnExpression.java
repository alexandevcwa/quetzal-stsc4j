package com.stsc4j.parser.v1.parser;

import com.stsc4j.lexer.Token;
import com.stsc4j.lexer.TokenType;
import com.stsc4j.parser.v1.ast.Expression;
import com.stsc4j.parser.v1.ast.ExpressionJsn;
import com.stsc4j.parser.v1.ast.ExpressionJsnBlock;

import java.util.ArrayList;
import java.util.List;

public class ParserJsnExpression {

    private final ParserExpressions parserExpressions;
    private final TokenStream tokenStream;

    public ParserJsnExpression(TokenStream tokenStream, ParserExpressions parserExpressions) {
        this.tokenStream = tokenStream;
        this.parserExpressions = parserExpressions;
    }

    public ExpressionJsnBlock parserJsnExpression() {
        return (ExpressionJsnBlock) parseJsnBlock();
    }

    private Expression parseJsnBlock() {
        tokenStream.consume(TokenType.BRACES_OPEN, "Se esperaba { para el bloque de JSN");
        boolean stop = false;
        List<ExpressionJsn> expressions = new ArrayList<>();
        while (!stop) {
            expressions.add(parseJsnExpression());
            boolean isComma = tokenStream.match(TokenType.COMMA);
            if (!isComma) {
                tokenStream.consume(TokenType.BRACES_CLOSE, "Se esperaba } para cerrar el bloque de JSN");
                stop = true;
            }
        }
        return new ExpressionJsnBlock(expressions);
    }

    private ExpressionJsn parseJsnExpression() {
        Token key = tokenStream.consume(TokenType.IDENTIFIER, "Se esperava un identificador para la llave del JSN");
        tokenStream.consume(TokenType.DOUBLE_DOT, "Se esperaba ':' para el operador de acceso a la llave del JSN");

        Expression value = null;
        // Parsear literales
        if (tokenStream.matchButNotAdvance(TokenType.LIT_DECIMAL, TokenType.LIT_INTEGER,
                TokenType.LIT_STRING, TokenType.LIT_TRUE, TokenType.LIT_FALSE)) {
            value = parserExpressions.parseExpression();
        }
        // Parsear objetos JSN anidados
        else if (tokenStream.matchButNotAdvance(TokenType.BRACES_OPEN)) {
            value = parseJsnBlock();
        }
        // Parsear listas
        else if (tokenStream.match(TokenType.BRACKETS_OPEN)) {
            value = parseJsnExpressionList(key);
        } else {
            throw new RuntimeException("Se esperaba un valor para la llave del JSN");
        }
        return new ExpressionJsn(key, value);
    }

    private Expression parseJsnExpressionList(Token key) {
        boolean stop = false;
        List<Expression> expressions = new ArrayList<>();

        while (!stop) {
            if (tokenStream.matchButNotAdvance(TokenType.BRACES_OPEN)) {
                expressions.add(parseJsnBlock());
            } else if (tokenStream.matchButNotAdvance(TokenType.LIT_DECIMAL, TokenType.LIT_INTEGER,
                    TokenType.LIT_STRING, TokenType.LIT_TRUE, TokenType.LIT_FALSE)) {
                expressions.add(parserExpressions.parseExpression());
            }
            boolean isComma = tokenStream.match(TokenType.COMMA);
            if (!isComma) {
                tokenStream.consume(TokenType.BRACKETS_CLOSE, "Se esperaba ] para cerrar la lista de JSN");
                stop = true;
            }
        }
        return new ExpressionJsn(key, expressions);
    }
}
