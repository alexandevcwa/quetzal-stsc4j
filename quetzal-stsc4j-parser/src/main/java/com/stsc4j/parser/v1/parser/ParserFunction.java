package com.stsc4j.parser.v1.parser;

import com.stsc4j.lexer.Token;
import com.stsc4j.lexer.TokenType;
import com.stsc4j.parser.v1.ast.Statement;
import com.stsc4j.parser.v1.ast.StatementBlock;
import com.stsc4j.parser.v1.ast.StatementFunction;

import java.util.List;

public class ParserFunction extends Parser{

    private final TokenStream tokenStream;
    private final ParserPrincipal parserPrincipal;

    public ParserFunction(TokenStream tokenStream, ParserPrincipal parserPrincipal) {
        this.tokenStream = tokenStream;
        this.parserPrincipal = parserPrincipal;
    }


    @Override
    public Statement parseStatement() {
        Token returnValue = null;
        if (tokenStream.notMatch(TokenType.PRIMITIVE_INTEGER, TokenType.PRIMITIVE_DECIMAL, TokenType.PRIMITIVE_STRING,
                TokenType.PRIMITIVE_BOOLEAN, TokenType.PRIMITIVE_VOID)) {
            throw new RuntimeException("Se esperaba un tipo de retorno para la función");
        }
        returnValue = tokenStream.before();
        Token identified = tokenStream.consume(TokenType.IDENTIFIER, "Se esperaba un identificador para la función");
        tokenStream.consume(TokenType.LEFT_PARENT, "Se esperaba '(' después del identificador de la función");

        ParserFunctionParameter pParameter = new ParserFunctionParameter(tokenStream);
        List<Statement> parameters = pParameter.parseStatements();
        tokenStream.consume(TokenType.RIGHT_PARENT, "Se esperaba ')' después de los parámetros de la función");
        tokenStream.consume(TokenType.BRACES_OPEN, "Se esperaba '{' al inicio del bloque de la función");

        // Parseo de bloque
        List<Statement> block = parserPrincipal.parse(TokenType.BRACES_CLOSE);
        tokenStream.consume(TokenType.BRACES_CLOSE, "Se esperaba '}' al final del bloque de la función");
        return new StatementFunction(returnValue, identified, parameters, new StatementBlock(block));
    }
}
