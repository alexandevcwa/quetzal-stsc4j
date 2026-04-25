package com.stsc4j.parser.v1.parser;

import com.stsc4j.lexer.Token;
import com.stsc4j.lexer.TokenType;
import com.stsc4j.parser.v1.ast.Statement;
import com.stsc4j.parser.v1.ast.StatementFuncionParameter;

import java.util.ArrayList;
import java.util.List;

public class ParserFunctionParameter {

    public TokenStream tokenStream;

    public ParserFunctionParameter(TokenStream tokenStream) {
        this.tokenStream = tokenStream;
    }

    public List<Statement> parse() {
        boolean stop = false;
        List<Statement> parameters = new ArrayList<>();

        while (!stop) {
            if (tokenStream.notMatch(TokenType.PRIMITIVE_INTEGER, TokenType.PRIMITIVE_DECIMAL, TokenType.PRIMITIVE_STRING,
                    TokenType.PRIMITIVE_BOOLEAN)) {
                throw new RuntimeException("Se esperaba un tipo de dato primitivo para el parámetro de la función");
            }
            Token type = tokenStream.before();
            Token identified = tokenStream.consume(TokenType.IDENTIFIER, "Se esperaba un identificador para el parámetro de la función");
            parameters.add(new StatementFuncionParameter(type, identified));
            if (tokenStream.matchNotAdvance(TokenType.RIGHT_PARENT)) {
                stop = true;
            } else {
                tokenStream.consume(TokenType.COMMA, "Se esperaba ',' para separar los parámetros de la función");
            }
        }
        return parameters;
    }
}
