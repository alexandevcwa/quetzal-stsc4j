package com.stsc4j.parser.v1.parser;

import com.stsc4j.lexer.TokenType;
import com.stsc4j.parser.v1.ast.Expression;
import com.stsc4j.parser.v1.ast.Statement;
import com.stsc4j.parser.v1.ast.StatementBlock;
import com.stsc4j.parser.v1.ast.StatementIf;

import java.util.ArrayList;
import java.util.List;

public class ParserStatement {
    private final TokenStream tokenStream;
    private final ParserExpressions parserExpressions;
    private final ParserPrincipal parserPrincipal;

    public ParserStatement(TokenStream tokenStream, ParserExpressions parserExpressions, ParserPrincipal parserPrincipal) {
        this.tokenStream = tokenStream;
        this.parserExpressions = parserExpressions;
        this.parserPrincipal = parserPrincipal;
    }

    public Statement parseIf(){
        tokenStream.consume(TokenType.LEFT_PARENT,"Se esperaba '(' después del si.");
        Expression condition = parserExpressions.parseExpression();
        tokenStream.consume(TokenType.RIGHT_PARENT,"Se esperaba ')' después de la condición.");
        Statement thenBranch = parseBlock();
        Statement elseBranch = null;

        if(tokenStream.match(TokenType.ELSE)) {
            if(tokenStream.match(TokenType.IF)) {
                elseBranch = parseIf();
            }else {
                elseBranch = parseBlock();
            }
        }
        return new StatementIf(condition,thenBranch,elseBranch);
    }

    public Statement parseBlock(){
        tokenStream.consume(TokenType.BRACES_OPEN, "Se esperaba { al inicio de bloque.");
        List<Statement> statements = new ArrayList<>();

        while (!tokenStream.show().getType().equals(TokenType.BRACES_CLOSE) && !tokenStream.isAtEnd()){
            statements.add(parserPrincipal.parseNext());
        }
        tokenStream.consume(TokenType.BRACES_CLOSE, "Se esperaba } al final de bloque.");
        return new StatementBlock(statements);
    }
}
