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
    private final ParserArrayExpression parserArrayExpression;
    private final ParserDeclaration parserDeclaration;
    private final ParserStatement parserStatement;

    public ParserPrincipal(TokenStream stream){
        this.tokenStream = stream;
        this.parserExpressions = new ParserExpressions(stream);
        this.parserArrayExpression = new ParserArrayExpression(stream,parserExpressions);
        this.parserDeclaration = new ParserDeclaration(stream,parserExpressions);
        this.parserStatement = new ParserStatement(stream,parserExpressions,this);
    }

    public List<Statement> parse(){
        List<Statement> ast = new ArrayList<>();
        while (!tokenStream.isAtEnd()){
            ast.add(parseNext());
        }
        return ast;
    }

    public Statement parseNext(){
        // Parser Variables
        if(tokenStream.match(TokenType.PRIMITIVE_INTEGER,TokenType.PRIMITIVE_DECIMAL,TokenType.PRIMITIVE_STRING,
                TokenType.PRIMITIVE_BOOLEAN, TokenType.IDENTIFIER)){
            return parserDeclaration.parseVarDeclaration();
        }

        if(tokenStream.match(TokenType.LIST)){
            return parserArrayExpression.parseArrayExpression();
        }

        // Parser If
        if(tokenStream.match(TokenType.IF)){
            return parserStatement.parseIf();
        }
        throw new RuntimeException("Unrecognized token...");
    }

    private Statement parseStatementExpression(){
        Expression expression =  parserExpressions.parseExpression();
        return new StatementExpression(expression);
    }
}
