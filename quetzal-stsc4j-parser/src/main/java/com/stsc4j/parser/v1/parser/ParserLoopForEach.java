package com.stsc4j.parser.v1.parser;

import com.stsc4j.lexer.Token;
import com.stsc4j.lexer.TokenType;
import com.stsc4j.parser.v1.ast.*;
import com.stsc4j.parser.v1.exception.ParserException;

public class ParserLoopForEach extends Parser {

    private final TokenStream tokenStream;
    private final ParserExpression parserExpression;
    private final ParserBlock parserBlock;

    public ParserLoopForEach(TokenStream tokenStream, ParserExpression parserExpression, ParserBlock parserBlock) {
        this.tokenStream = tokenStream;
        this.parserExpression = parserExpression;
        this.parserBlock = parserBlock;
    }

    @Override
    public Statement parseStatement() {
        tokenStream.consume(TokenType.LOOP_FOR, "Se esperaba 'para' en la declaración del ciclo");
        tokenStream.consume(TokenType.LEFT_PARENT, "Se esperaba '(' en la declaración de ciclo 'para'");

        // Obtener tipo de dato
        if (tokenStream.notMatch(TokenType.PRIMITIVE_BOOLEAN, TokenType.PRIMITIVE_INTEGER, TokenType.PRIMITIVE_DECIMAL, TokenType.PRIMITIVE_STRING)) {
            throw new ParserException("Se esperaba una declaración de variable con un tipo primitivo en la declaración del ciclo 'para'");
        }
        Token type = tokenStream.before();

        tokenStream.consume(TokenType.MUTABLE_VARIABLE, " Se esperaba 'var' en la declaración de variable del ciclo 'para'");

        Expression expression = parserExpression.parseExpression();
        if (!(expression instanceof ExpressionVariable)) {
            throw new ParserException("Se esperaba una declaración de variable con un nombre válido en la declaración del ciclo 'para'");
        }
        ExpressionVariable declaration = (ExpressionVariable) expression;
        ExpressionForEachVar forEachVar = new ExpressionForEachVar(type, declaration);

        // Validación de palabras reservadas
        if (tokenStream.notMatch(TokenType.LOOP_EACH_1, TokenType.LOOP_EACH_2)) {
            throw new ParserException("Se esperaba 'en' o 'cada' en la declaración del ciclo 'para'");
        }

        // Variable que contiene la lista de elementos a iterar
        Expression listVariable = parserExpression.parseExpression();
        if (!(listVariable instanceof ExpressionVariable)) {
            throw new ParserException("Se esperaba una variable que contenga una lista en la declaración del ciclo 'para'");
        }

        tokenStream.consume(TokenType.RIGHT_PARENT, "Se esperaba ')' al final de la declaración del ciclo 'para'");

        // Bloque de declaraciones
        Statement block = parserBlock.parseStatement();
        return new StatementLoopForEach(forEachVar, (ExpressionVariable) listVariable, (StatementBlock) block);
    }
}
