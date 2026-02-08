package com.stsc4j.lexer;

public class Token {
    final TokenType type;
    final String lexeme;

    public Token(TokenType type, String value) {
        this.type = type;
        this.lexeme = value;
    }

    @Override
    public String toString() {
        return String.format("%-18s -> %s", type, lexeme);
    }
}
