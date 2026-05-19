package com.stsc4j.parser.v1.parser;

import com.stsc4j.lexer.Token;
import com.stsc4j.lexer.TokenType;
import com.stsc4j.parser.v1.ast.Statement;
import com.stsc4j.parser.v1.ast.StatementFunctionParameter;

import java.util.ArrayList;
import java.util.List;

public class ParserFunctionParameter extends Parser {

    public TokenStream tokenStream;

    private ParserFunctionParameter(TokenStream tokenStream) {
        this.tokenStream = tokenStream;
    }

    public static ParserFunctionParameterBuilder builder(TokenStream tokenStream) {
        return new ParserFunctionParameterBuilder(tokenStream);
    }

    public static class ParserFunctionParameterBuilder {
        private final ParserFunctionParameter parser;

        public ParserFunctionParameterBuilder(TokenStream tokenStream) {
            parser = new ParserFunctionParameter(tokenStream);
        }

        public ParserFunctionParameter build() {
            return parser;
        }
    }

    @Override
    public List<Statement>  parseStatements() {
        return parse();
    }

    private List<Statement> parse() {
        boolean stop = false;
        List<Statement> parameters = new ArrayList<>();

        while (!stop) {
            if (tokenStream.matchNotAdvance(TokenType.RIGHT_PARENT)) {
                return parameters;
            }
            if (tokenStream.notMatch(TokenType.PRIMITIVE_INTEGER, TokenType.PRIMITIVE_DECIMAL, TokenType.PRIMITIVE_STRING,
                    TokenType.PRIMITIVE_BOOLEAN, TokenType.LIST)) {
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
