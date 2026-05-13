package com.stsc4j.parser.v1.parser;

import com.stsc4j.lexer.TokenType;
import com.stsc4j.parser.v1.ast.Expression;
import com.stsc4j.parser.v1.ast.Statement;
import com.stsc4j.parser.v1.ast.StatementBlock;
import com.stsc4j.parser.v1.ast.StatementLoopWhile;

public class ParserLoopWhile extends Parser {

    private final TokenStream tokenStream;
    private final ParserExpression parserExpression;
    private final ParserBlock parserBlock;

    public ParserLoopWhile(TokenStream context, ParserExpression parserExpression, ParserBlock parserBlock) {
        this.tokenStream = context;
        this.parserExpression = parserExpression;
        this.parserBlock = parserBlock;
    }

    @Override
    public Statement parseStatement() {
        tokenStream.consume(TokenType.LOOP_WHILE, "Se esperaba 'mientras'");
        tokenStream.consume(TokenType.LEFT_PARENT, "Se esperaba '('");
        Expression expression = parserExpression.parseExpression();
        tokenStream.consume(TokenType.RIGHT_PARENT, "Se esperaba ')'");
        Statement block = parserBlock.parseStatement();
        return new StatementLoopWhile(expression, (StatementBlock) block);
    }
}
