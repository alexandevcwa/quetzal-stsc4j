package com.stsc4j.lexer.state;

import com.stsc4j.lexer.LexerContext;

public class InitialState implements LexerState {

    @Override
    public void process(char c, LexerContext lexer) {
        if (Character.isWhitespace(c)) {
            return;
        }

        if (Character.isLetter(c)) {
            lexer.setState(new LetterState());
            lexer.add(c);
        }

        if(Character.isDigit(c)){

        }
    }

    @Override
    public void finalize(LexerContext lexer) {

    }
}
