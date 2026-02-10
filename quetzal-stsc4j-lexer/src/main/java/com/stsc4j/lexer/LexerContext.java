package com.stsc4j.lexer;

import com.stsc4j.lexer.state.InitialState;
import com.stsc4j.lexer.state.LexerState;

import java.util.ArrayList;
import java.util.List;

public class LexerContext {

    private LexerState state;

    private final StringBuilder buffer = new StringBuilder();
    private final List<Token> tokens = new ArrayList<>();

    public LexerContext() {
        this.state = new InitialState();
    }

    public void process(String input) {
        if (input == null || input.isEmpty()) return;

        char[] chars = input.toCharArray();
        int length = chars.length;
        boolean omit = false;

        for (int i = 0; i < length; i++) {
            char currentChar = chars[i];

            // System.out.println("Index: " + i + " / Char: " + currentChar + " / State: " + state);

            // Detección de inicio de comentario
            if (i + 1 < length) {
                if (isCommentStart(currentChar, chars[i + 1])) {
                    omit = true;
                }
            }

            // Fin de comentario
            if (currentChar == '\n') {
                omit = false;
            }

            if (!omit) {
                state.process(currentChar, length, i, this);
            }
        }

        if (buffer.length() > 0) {
            state.finalize(this);
        }
    }

    /**
     * Verifica si el par de caracteres inicia un comentario.
     */
    private boolean isCommentStart(char currentChar, char nextChar) {
        return currentChar == '/' && (nextChar == '*' || nextChar == '/');
    }

    public void add(char c) {
        buffer.append(c);
        // System.out.println(buffer + " / " + c);
    }

    public void generateToken(TokenType type) {
        // System.out.println("Token: " + type + " / Value: " + buffer);

        // Crear token y reiniciar
        tokens.add(new Token(type, buffer.toString()));
        buffer.setLength(0);

        // Reiniciar estado
        this.state = new InitialState();
    }

    public void cleanToken() {
        tokens.clear();
    }

    // Getters y Setters
    public void setState(LexerState newState) {
        this.state = newState;
    }

    public LexerState getState() {
        return state;
    }

    public String getBuffer() {
        return buffer.toString();
    }

    public List<Token> getTokens() {
        return tokens;
    }
}