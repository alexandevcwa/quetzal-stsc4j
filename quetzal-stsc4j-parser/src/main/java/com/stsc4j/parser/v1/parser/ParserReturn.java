package com.stsc4j.parser.v1.parser;

import com.stsc4j.lexer.TokenType;
import com.stsc4j.parser.v1.ast.Expression;
import com.stsc4j.parser.v1.ast.Statement;
import com.stsc4j.parser.v1.ast.StatementReturn;

public class ParserReturn extends Parser{
    private final TokenStream tokenStream;
    private final ParserExpression parserExpression;

    public ParserReturn(ParserExpression parserExpression, TokenStream tokenStream) {
        this.parserExpression = parserExpression;
        this.tokenStream = tokenStream;
    }

    @Override
    public Statement parseStatement() {
        tokenStream.consume(TokenType.RETURN, "Se esperaba 'retornar'");
        Expression returnExpression = parserExpression.parseExpression();
        return new StatementReturn(returnExpression);
    }
}
