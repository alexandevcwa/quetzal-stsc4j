package com.stsc4j.parser.v1.parser;

import com.stsc4j.lexer.TokenType;
import com.stsc4j.parser.v1.ast.Expression;
import com.stsc4j.parser.v1.ast.Statement;
import com.stsc4j.parser.v1.ast.StatementIf;

public class ParserIf extends Parser {

    private final TokenStream tokenStream;
    private final ParserBlock parserBlock;
    private final ParserExpressions parserExpressions;

    public ParserIf(TokenStream tokenStream,ParserExpressions parserExpressions, ParserBlock parserBlock) {
        this.tokenStream = tokenStream;
        this.parserExpressions = parserExpressions;
        this.parserBlock = parserBlock;
    }

    @Override
    public Statement parseStatement() {
        tokenStream.consume(TokenType.IF, "Se esperaba 'si'.");
        tokenStream.consume(TokenType.LEFT_PARENT, "Se esperaba '(' después del si.");
        Expression condition = parserExpressions.parseExpression();
        tokenStream.consume(TokenType.RIGHT_PARENT, "Se esperaba ')' después de la condición.");
        Statement thenBranch = parserBlock.parseStatement();
        Statement elseBranch = null;

        if (tokenStream.match(TokenType.ELSE)) {
            if (tokenStream.match(TokenType.IF)) {
                elseBranch = parseStatement();
            } else {
                elseBranch = parserBlock.parseStatement();
            }
        }
        return new StatementIf(condition, thenBranch, elseBranch);
    }
}
