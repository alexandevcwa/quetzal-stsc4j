package com.stsc4j.parser.v1.parser;

import com.stsc4j.lexer.Token;
import com.stsc4j.lexer.TokenType;
import com.stsc4j.parser.v1.ast.Statement;
import com.stsc4j.parser.v1.ast.StatementBlock;
import com.stsc4j.parser.v1.ast.StatementFunction;

import java.util.List;

public class ParserFunction extends Parser {

    private final TokenStream tokenStream;
    private final ParserPrincipal parserPrincipal;

    public ParserFunction(TokenStream tokenStream, ParserPrincipal parserPrincipal) {
        this.tokenStream = tokenStream;
        this.parserPrincipal = parserPrincipal;
    }


    @Override
    public Statement parseStatement() {
        // Obtener el tipo de dato de retorno
        Token returnValue = null;
        if (tokenStream.notMatch(TokenType.PRIMITIVE_INTEGER, TokenType.PRIMITIVE_DECIMAL, TokenType.PRIMITIVE_STRING,
                TokenType.PRIMITIVE_BOOLEAN, TokenType.PRIMITIVE_VOID, TokenType.LIST)) {
            throw new RuntimeException("Se esperaba un tipo de retorno para la función");
        }
        returnValue = tokenStream.before();

        // Determinar si el tipo de dato es una lista
        Token listType;
        if (returnValue.getType() == TokenType.LIST) {
            boolean isStrong = tokenStream.match(TokenType.LESS_THAN);
            if (isStrong) {
                if (tokenStream.match(TokenType.PRIMITIVE_INTEGER, TokenType.PRIMITIVE_DECIMAL, TokenType.PRIMITIVE_STRING,
                        TokenType.PRIMITIVE_BOOLEAN)) {
                    listType = tokenStream.before();
                    tokenStream.consume(TokenType.GREATER_THAN, "Se esperaba '>' después del tipo de dato de la lista");
                } else {
                    throw new RuntimeException("Se esperaba un tipo de dato primitivo para la lista");
                }
            }
        }

        // Determinar el nombre de la función
        Token identified = tokenStream.consume(TokenType.IDENTIFIER, "Se esperaba un identificador para la función");

        // Determinar parámetros de la función
        tokenStream.consume(TokenType.LEFT_PARENT, "Se esperaba '(' después del identificador de la función");
        ParserFunctionParameter pParameter = new ParserFunctionParameter(tokenStream);
        List<Statement> parameters = pParameter.parseStatements();
        tokenStream.consume(TokenType.RIGHT_PARENT, "Se esperaba ')' después de los parámetros de la función");
        tokenStream.consume(TokenType.BRACES_OPEN, "Se esperaba '{' al inicio del bloque de la función");

        // Determinar bloque de declaración de la función
        List<Statement> block = parserPrincipal.parse(TokenType.BRACES_CLOSE);
        tokenStream.consume(TokenType.BRACES_CLOSE, "Se esperaba '}' al final del bloque de la función");
        return new StatementFunction(returnValue, identified, parameters, new StatementBlock(block));
    }
}
