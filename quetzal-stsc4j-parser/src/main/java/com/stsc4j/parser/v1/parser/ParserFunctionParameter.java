package com.stsc4j.parser.v1.parser;

import com.stsc4j.lexer.Token;
import com.stsc4j.lexer.TokenType;
import com.stsc4j.parser.v1.ast.Statement;
import com.stsc4j.parser.v1.ast.StatementFunctionParameter;

import java.util.ArrayList;
import java.util.List;

public class ParserFunctionParameter extends Parser {

    public TokenStream tokenStream;

    public ParserFunctionParameter(TokenStream tokenStream) {
        this.tokenStream = tokenStream;
    }

    @Override
    public List<Statement> parseStatements() {
        return parse();
    }

    private List<Statement> parse() {
        boolean stop = false;
        List<Statement> parameters = new ArrayList<>();

        while (!stop) {
            if (tokenStream.notMatch(TokenType.PRIMITIVE_INTEGER, TokenType.PRIMITIVE_DECIMAL, TokenType.PRIMITIVE_STRING,
                    TokenType.PRIMITIVE_BOOLEAN)) {
                throw new RuntimeException("Se esperaba un tipo de dato primitivo para el parámetro de la función");
            }
            Token type = tokenStream.before();

            boolean isMutable = tokenStream.match(TokenType.MUTABLE_VARIABLE);

            Token identified = tokenStream.consume(TokenType.IDENTIFIER, "Se esperaba un identificador para el parámetro de la función");
            parameters.add(new StatementFunctionParameter(type, isMutable, identified));
            if (tokenStream.matchNotAdvance(TokenType.RIGHT_PARENT)) {
                stop = true;
            } else {
                tokenStream.consume(TokenType.COMMA, "Se esperaba ',' para separar los parámetros de la función");
            }
        }
        return parameters;
    }
}
