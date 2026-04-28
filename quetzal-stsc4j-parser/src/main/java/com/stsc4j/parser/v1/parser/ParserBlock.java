package com.stsc4j.parser.v1.parser;

import com.stsc4j.lexer.TokenType;
import com.stsc4j.parser.v1.ast.Statement;
import com.stsc4j.parser.v1.ast.StatementBlock;

import java.util.ArrayList;
import java.util.List;

public class ParserBlock extends Parser{

    private final ParserPrincipal parserPrincipal;
    private final TokenStream tokenStream;

    public ParserBlock(TokenStream tokenStream, ParserPrincipal parserPrincipal) {
        this.tokenStream = tokenStream;
        this.parserPrincipal = parserPrincipal;
    }

    @Override
    public Statement parseStatement() {
        tokenStream.consume(TokenType.BRACES_OPEN, "Se esperaba { al inicio de bloque.");
        List<Statement> statements = new ArrayList<>();

        while (!tokenStream.show().getType().equals(TokenType.BRACES_CLOSE) && !tokenStream.isAtEnd()) {
            statements.add(parserPrincipal.parseNext());
        }
        tokenStream.consume(TokenType.BRACES_CLOSE, "Se esperaba } al final de bloque.");
        return new StatementBlock(statements);
    }
}
