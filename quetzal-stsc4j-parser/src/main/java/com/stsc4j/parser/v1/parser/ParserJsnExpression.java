package com.stsc4j.parser.v1.parser;

import com.stsc4j.lexer.Token;
import com.stsc4j.lexer.TokenType;
import com.stsc4j.parser.v1.ast.Expression;
import com.stsc4j.parser.v1.ast.ExpressionJsn;
import com.stsc4j.parser.v1.ast.ExpressionJsnBlock;

import java.util.ArrayList;
import java.util.List;

public class ParserJsnExpression extends Parser{

    private final ParserExpression parserExpression;
    private final TokenStream tokenStream;

    private ParserJsnExpression(TokenStream tokenStream, ParserExpression parserExpression) {
        this.tokenStream = tokenStream;
        this.parserExpression = parserExpression;
    }

    public static ParserJsnExpressionBuilder builder(TokenStream tokenStream, ParserExpression parserExpression) {
        return new ParserJsnExpressionBuilder(tokenStream, parserExpression);
    }

    public static class ParserJsnExpressionBuilder {
        private final ParserJsnExpression parserJsnExpression;

        public ParserJsnExpressionBuilder(TokenStream tokenStream, ParserExpression parserExpression) {
            this.parserJsnExpression = new ParserJsnExpression(tokenStream, parserExpression);
        }

        public ParserJsnExpression build() {
            return parserJsnExpression;
        }
    }

    @Override
    public Expression parseExpression() {
        return parseJsnBlock();
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
        if (tokenStream.matchNotAdvance(TokenType.LIT_DECIMAL, TokenType.LIT_INTEGER,
                TokenType.LIT_STRING, TokenType.LIT_TRUE, TokenType.LIT_FALSE)) {
            value = parserExpression.parseExpression();
        }
        // Parsear objetos JSN anidados
        else if (tokenStream.matchNotAdvance(TokenType.BRACES_OPEN)) {
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
            if (tokenStream.matchNotAdvance(TokenType.BRACES_OPEN)) {
                expressions.add(parseJsnBlock());
            } else if (tokenStream.matchNotAdvance(TokenType.LIT_DECIMAL, TokenType.LIT_INTEGER,
                    TokenType.LIT_STRING, TokenType.LIT_TRUE, TokenType.LIT_FALSE)) {
                expressions.add(parserExpression.parseExpression());
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
