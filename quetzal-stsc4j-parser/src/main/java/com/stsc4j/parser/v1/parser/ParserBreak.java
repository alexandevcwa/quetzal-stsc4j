package com.stsc4j.parser.v1.parser;

import com.stsc4j.lexer.TokenType;
import com.stsc4j.parser.v1.ast.Statement;
import com.stsc4j.parser.v1.ast.StatementBreak;

public class ParserBreak extends Parser{

    private final TokenStream tokenStream;

    public ParserBreak(TokenStream tokenStream) {
        this.tokenStream = tokenStream;
    }

    @Override
    public Statement parseStatement() {
        tokenStream.consume(TokenType.BREAK, "Se esperaba 'romper'");
        return new StatementBreak(tokenStream.before());
    }
}
