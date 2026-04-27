package com.stsc4j.parser.v1.parser;

import com.stsc4j.lexer.TokenType;
import com.stsc4j.parser.v1.ast.Expression;
import com.stsc4j.parser.v1.ast.Statement;
import com.stsc4j.parser.v1.ast.StatementExpression;

import java.util.ArrayList;
import java.util.List;

public class ParserPrincipal {
    private final TokenStream tokenStream;
    private final ParserExpressions parserExpressions;
    private final ParserListExpression parserListExpression;
    private final ParserDeclaration parserDeclaration;
    private final ParserStatement parserStatement;

    public ParserPrincipal(TokenStream stream) {
        this.tokenStream = stream;

        this.parserExpressions = new ParserExpressions(stream);

        this.parserListExpression = new ParserListExpression(stream, parserExpressions);

        this.parserDeclaration = new ParserDeclaration(stream, parserExpressions);

        this.parserStatement = new ParserStatement(stream,
                parserExpressions,
                this,
                new ParserJsnExpression(stream, parserExpressions));
    }

    public List<Statement> parse() {
        List<Statement> ast = new ArrayList<>();
        while (!tokenStream.isAtEnd()) {
            ast.add(parseNext());
        }
        return ast;
    }

    public List<Statement> parse(TokenType type){
        List<Statement> ast = new ArrayList<>();
        boolean stop = false;
        while (!stop){
            ast.add(parseNext());
            if (tokenStream.matchNotAdvance(type)){
                stop = true;
            }
        }
        return ast;
    }

    public Statement parseNext() {
//        // Parser de Llamadas a Métodos y Funciones
//        if (tokenStream.match(TokenType.IDENTIFIER) &&
//                (tokenStream.matchAndBack(TokenType.DOT) || tokenStream.matchAndBack(TokenType.LEFT_PARENT))) {
//            Expression expr = parserExpressions.parseExpression();
//            return new StatementExpression(expr);
//        }


        // Parser (Funciones)
        if (tokenStream.match(TokenType.PRIMITIVE_INTEGER, TokenType.PRIMITIVE_DECIMAL, TokenType.PRIMITIVE_STRING,
                TokenType.PRIMITIVE_BOOLEAN, TokenType.PRIMITIVE_VOID)) {
            if (tokenStream.match(TokenType.IDENTIFIER)) {
                if (tokenStream.match(TokenType.LEFT_PARENT)) {
                    tokenStream.back(3);
                    // Llamar a parser
                    return parserStatement.parseFunction();
                } else {
                    tokenStream.back(2);
                }
            } else {
                tokenStream.back(1);
            }
        }


        // Parser (Variables)
        if (tokenStream.matchNotAdvance(TokenType.PRIMITIVE_INTEGER, TokenType.PRIMITIVE_DECIMAL, TokenType.PRIMITIVE_STRING,
                TokenType.PRIMITIVE_BOOLEAN, TokenType.IDENTIFIER)) {
            return parserDeclaration.parseStatement();
        }

        // Listas
        if (tokenStream.match(TokenType.LIST)) {
            return parserListExpression.parseListExpression();
        }

        // Parser If
        if (tokenStream.match(TokenType.IF)) {
            return parserStatement.parseIf();
        }

        // Parser Return
        if (tokenStream.match(TokenType.RETURN)) {
            tokenStream.back();
            return parserStatement.parseReturn();
        }

        // Parser JSN
        if (tokenStream.matchNotAdvance(TokenType.JSN)) {
            return parserStatement.parseJsn();
        }
        throw new RuntimeException("Unrecognized token...");
    }

    private Statement parseStatementExpression() {
        Expression expression = parserExpressions.parseExpression();
        return new StatementExpression(expression);
    }
}
