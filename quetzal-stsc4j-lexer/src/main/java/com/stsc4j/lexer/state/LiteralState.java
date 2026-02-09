package com.stsc4j.lexer.state;

import com.stsc4j.lexer.LexerContext;
import com.stsc4j.lexer.TokenType;
import com.stsc4j.lexer.strategy.*;

import java.util.List;

public class LiteralState extends AbstractState implements LexerState {

    private final Analyzer ANALYZER = new Analyzer(List.of(
            new ClassifierIntegerLiterals(),
            new ClassifierNumericLiterals(),
            new ClassifierBoolLiterals(),
            new ClassifierStringLiterals()
    ));

    @Override
    public void process(char c, int length, int index, LexerContext lexer) {
        if (isPartOfNumber(c)) {
            lexer.add(c);
            if (length == index + 1) {
                flushToken(lexer);
            }
        } else {
            flushToken(lexer);
            lexer.getState().process(c, length, index, lexer);
        }
    }

    private boolean isPartOfNumber(char c) {
     return (Character.isLetterOrDigit(c) || c == '.' || c == '"' || Character.isWhitespace(c));
    }

    @Override
    protected void flushToken(LexerContext context) {
        TokenType token = ANALYZER.analyze(context.getBuffer());
        context.generateToken(token);
    }

    @Override
    public void finalize(LexerContext lexer) {

    }
}
