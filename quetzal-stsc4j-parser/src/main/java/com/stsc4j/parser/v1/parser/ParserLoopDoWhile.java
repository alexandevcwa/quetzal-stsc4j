package com.stsc4j.parser.v1.parser;


import com.stsc4j.lexer.TokenType;
import com.stsc4j.parser.v1.ast.Expression;
import com.stsc4j.parser.v1.ast.Statement;
import com.stsc4j.parser.v1.ast.StatementBlock;
import com.stsc4j.parser.v1.ast.StatementLoopDoWhile;

public class ParserLoopDoWhile extends Parser {

    private final TokenStream tokenStream;
    private final ParserBlock parserBlock;
    private final ParserExpression parserExpression;

    public ParserLoopDoWhile(TokenStream tokenStream, ParserExpression parserExpression, ParserBlock parserBlock) {
        this.tokenStream = tokenStream;
        this.parserExpression = parserExpression;
        this.parserBlock = parserBlock;
    }

    @Override
    public Statement parseStatement() {
        tokenStream.consume(TokenType.LOOP_DO, "Se esperaba 'hacer'");
        Statement block = parserBlock.parseStatement();
        tokenStream.consume(TokenType.LOOP_WHILE, "Se esperaba 'mientras'");
        tokenStream.consume(TokenType.LEFT_PARENT, "Se esperaba apertura de parentesis '(' en la declaración de mientras");
        Expression expression = parserExpression.parseExpression();
        tokenStream.consume(TokenType.RIGHT_PARENT, "Se esperaba cierre de parentesis ')' en la declaración de mientras");
        return new StatementLoopDoWhile(expression, (StatementBlock) block);
    }
}
