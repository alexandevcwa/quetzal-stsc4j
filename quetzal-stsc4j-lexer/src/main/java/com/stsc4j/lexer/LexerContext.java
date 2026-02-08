package com.stsc4j.lexer;

import com.stsc4j.lexer.state.InitialState;
import com.stsc4j.lexer.state.LexerState;
import com.stsc4j.lexer.strategy.Analyzer;
import com.stsc4j.lexer.strategy.ClassifierIdentifier;
import com.stsc4j.lexer.strategy.ClassifierKeywords;
import com.stsc4j.lexer.strategy.ClassifierOperators;

import java.util.ArrayList;
import java.util.List;

public class LexerContext {

    private LexerState state;
    private StringBuffer buffer = new StringBuffer();
    private List<Token> tokens = new ArrayList<>();

    private Analyzer analyzer = new Analyzer(List.of(
            new ClassifierOperators(),
            new ClassifierKeywords(),
            new ClassifierIdentifier())
    );

    public LexerContext() {
        this.state = new InitialState();
    }

    public void setState(LexerState newState) {
        this.state = newState;
    }

    public void process(String input) {
        for (char c : input.toCharArray()) {
            state.process(c, this);
        }
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

    public void generateTokenType() {
        final String lexeme = buffer.toString();
        TokenType tokenType = analyzer.analyze(lexeme);
        generateToken(tokenType);
    }


    public List<Token> getTokens() {
        return tokens;
    }

}
