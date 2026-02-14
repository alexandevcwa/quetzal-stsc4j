package com.stsc4j.parser.parser;

import com.stsc4j.lexer.Token;
import com.stsc4j.lexer.TokenType;
import com.stsc4j.parser.exception.ParseException;

import java.util.ArrayList;
import java.util.List;

public class ParserContext {

    private final List<Token> tokens;
    private int current = 0;
    // Lista para acumular errores en lugar de lanzar excepción y morir
    public final List<String> errors = new ArrayList<>();

    public ParserContext(List<Token> tokens) {
        this.tokens = tokens;
    }

    public Token peek() {
        return tokens.get(current);
    }

    public boolean isAtEnd() {
        return peek().getType() == TokenType.EOF;
    }

    public Token advance() {
        if (!isAtEnd()) current++;
        return previous();
    }

    public Token previous() {
        return tokens.get(current - 1);
    }

    public boolean match(TokenType... types) {
        for (TokenType type : types) {
            if (check(type)) {
                advance();
                return true;
            }
        }
        return false;
    }

    public boolean check(TokenType type) {
        if (isAtEnd()) return false;
        return peek().getType() == type;
    }

    public Token consume(TokenType type, String message) {
        if (check(type)) return advance();

        // Error Recovery: Registramos el error pero no detenemos todo abruptamente
        errors.add(message + " en " + peek().getLexeme());
        throw new ParseException(message); // Excepción controlada interna
    }
}
