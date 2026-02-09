package com.stsc4j.lexer.state;

import com.stsc4j.lexer.LexerContext;

public class DigitState extends AbstractState implements LexerState {

    @Override
    public void process(char c, int length, int index, LexerContext lexer) {
        if (isDigitPart(c)) {
            lexer.add(c);
            if (length == index + 1) {
                flushToken(lexer);
            }
        } else {
            flushToken(lexer);
        }
    }

    private boolean isDigitPart(char c) {
        return Character.isDigit(c) || c == '.';
    }

    @Override
    protected void flushToken(LexerContext context) {
        context.setState(new InitialState());
    }

    @Override
    public void finalize(LexerContext lexer) {

    }
}
