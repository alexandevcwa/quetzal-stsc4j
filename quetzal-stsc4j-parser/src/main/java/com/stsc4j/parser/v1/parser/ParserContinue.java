package com.stsc4j.parser.v1.parser;

import com.stsc4j.lexer.TokenType;
import com.stsc4j.parser.v1.ast.Statement;
import com.stsc4j.parser.v1.ast.StatementContinue;

public class ParserContinue  extends Parser{

    private final TokenStream tokenStream;

    public ParserContinue(TokenStream tokenStream) {
        this.tokenStream = tokenStream;
    }

    @Override
    public Statement parseStatement() {
        tokenStream.consume(TokenType.CONTINUE, "Se esperaba 'continuar'");
        return new StatementContinue(tokenStream.before());
    }
}
