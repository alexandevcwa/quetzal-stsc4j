package com.stsc4j.parser.v1.parser;

import com.stsc4j.lexer.TokenType;
import com.stsc4j.parser.v1.ast.*;
import com.stsc4j.parser.v1.exception.ParserException;

public class ParserTryCatchFinally extends Parser{

    private final ParserBlock parserBlock;
    private final ParserExpression parserExpression;
    private final TokenStream tokenStream;

    public ParserTryCatchFinally(ParserBlock parserBlock, ParserExpression parserExpression, TokenStream tokenStream) {
        this.parserBlock = parserBlock;
        this.parserExpression = parserExpression;
        this.tokenStream = tokenStream;
    }

    @Override
    public Statement parseStatement() {
        // Try
        tokenStream.consume(TokenType.TRY, "Se esperaba 'intentar'");
        StatementBlock blockTry = (StatementBlock) parserBlock.parseStatement();
        // Catch
        tokenStream.consume(TokenType.CATCH, "Se esperaba 'capturar'");
        tokenStream.consume(TokenType.LEFT_PARENT, "Se esperaba '(' para el bloque de captura");
        tokenStream.consume(TokenType.EXCEPTION, "Se experaba 'excepcion' en el bloque de captura");
        Expression exceptionType = parserExpression.parseExpression();
        if(!(exceptionType instanceof ExpressionVariable)){
            throw new ParserException("Se esperaba un nombre de variable para la excepción en el bloque de captura");
        }
        tokenStream.consume(TokenType.RIGHT_PARENT, "Se esperaba ')' para el bloque de captura");
        StatementBlock blockCatch = (StatementBlock) parserBlock.parseStatement();

        // Finally
        if (tokenStream.match(TokenType.FINALLY)){
            StatementBlock blockFinally = (StatementBlock) parserBlock.parseStatement();
            return new StatementTryCatchFinally(blockTry, (ExpressionVariable) exceptionType, blockCatch, blockFinally);
        }
        return new StatementTryCatchFinally(blockTry, (ExpressionVariable) exceptionType, blockCatch);
    }
}
