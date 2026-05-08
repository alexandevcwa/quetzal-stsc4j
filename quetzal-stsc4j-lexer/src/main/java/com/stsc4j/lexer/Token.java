package com.stsc4j.lexer;

public class Token {
    public final TokenType type;
    public final String lexeme;
    public final int line;

    public Token(TokenType type, String value, int line) {
        this.type = type;
        this.lexeme = value;
        this.line = line;
    }

    public TokenType getType() {
        return type;
    }

    public String getLexeme() {
        return lexeme;
    }

    @Override
    public String toString() {
        return String.format("%-18s -> %s", type, lexeme);
    }
}
