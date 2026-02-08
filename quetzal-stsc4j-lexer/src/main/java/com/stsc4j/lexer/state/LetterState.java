package com.stsc4j.lexer.state;

import com.stsc4j.lexer.LexerContext;

public class LetterState implements LexerState {

    @Override
    public void process(char c, LexerContext lexer) {
        if (Character.isLetterOrDigit(c)) {
            lexer.add(c);
        } else {
            lexer.generateTokenType();
            LexerState l = new InitialState();
            lexer.setState(l);
            l.process(c, lexer);
        }
    }

    @Override
    public void finalize(LexerContext lexer) {

    }
}
