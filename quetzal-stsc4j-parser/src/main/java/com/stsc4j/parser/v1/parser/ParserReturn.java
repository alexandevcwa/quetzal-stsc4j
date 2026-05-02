package com.stsc4j.parser.v1.parser;

import com.stsc4j.lexer.TokenType;
import com.stsc4j.parser.v1.ast.Expression;
import com.stsc4j.parser.v1.ast.Statement;
import com.stsc4j.parser.v1.ast.StatementReturn;

public class ParserReturn extends Parser{
    private final TokenStream tokenStream;
    private final ParserExpressions parserExpressions;

    public ParserReturn(ParserExpressions parserExpressions, TokenStream tokenStream) {
        this.parserExpressions = parserExpressions;
        this.tokenStream = tokenStream;
    }

    @Override
    public Statement parseStatement() {
        tokenStream.consume(TokenType.RETURN, "Se esperaba 'retornar'");
        Expression returnExpression = parserExpressions.parseExpression();
        return new StatementReturn(returnExpression);
    }
}
