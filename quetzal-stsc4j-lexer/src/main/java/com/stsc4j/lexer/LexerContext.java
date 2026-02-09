package com.stsc4j.lexer;

import com.stsc4j.lexer.state.InitialState;
import com.stsc4j.lexer.state.LexerState;
import com.stsc4j.lexer.strategy.Analyzer;
import com.stsc4j.lexer.strategy.ClassifierIdentifier;
import com.stsc4j.lexer.strategy.ClassifierKeywords;
import com.stsc4j.lexer.strategy.ClassifierSymbols;

import java.util.ArrayList;
import java.util.List;

public class LexerContext {

    private LexerState state;
    private StringBuffer buffer = new StringBuffer();
    private List<Token> tokens = new ArrayList<>();

    private Analyzer analyzer = new Analyzer(List.of(
            new ClassifierSymbols(),
            new ClassifierKeywords(),
            new ClassifierIdentifier())
    );

    public LexerContext() {
        this.state = new InitialState();
    }

    public void setState(LexerState newState) {
        this.state = newState;
    }

    public LexerState getState() {
        return state;
    }

    public String getBuffer() {
        return buffer.toString();
    }

    public void cleanToken() {
        tokens.clear();
    }

    public void process(String input) {

        char[] chars = input.toCharArray();
        int index = 0;
        int length = chars.length;
        do {
            state.process(chars[index], length, index, this);
            index++;
        } while (length > index);

        if (buffer.length() > 0) {
            state.finalize(this);
        }
    }

    public void add(char c) {
        buffer.append(c);
    }

    public void generateToken(TokenType type) {
        tokens.add(new Token(type, buffer.toString()));
        buffer.setLength(0);
        state = new InitialState();
    }

    public List<Token> getTokens() {
        return tokens;
    }

}
