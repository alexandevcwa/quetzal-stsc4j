package com.stsc4j.lexer.state;

import com.stsc4j.lexer.LexerContext;
import com.stsc4j.lexer.TokenType;
import com.stsc4j.lexer.strategy.Analyzer;
import com.stsc4j.lexer.strategy.ClassifierIntegerLiterals;
import com.stsc4j.lexer.strategy.ClassifierNumericLiterals;

import java.util.List;

public class DigitLiteralState extends AbstractState implements LexerState {

    private final Analyzer ANALYZER = new Analyzer(List.of(
            new ClassifierIntegerLiterals(),
            new ClassifierNumericLiterals()
    ));

    private boolean isFirstDigit = true;

    @Override
    public void process(char c, int length, int index, LexerContext context) {

        if (isPartOfNumber(c)) {
            context.add(c);
            if (length == index + 1) {
                flushToken(context);
            }
        } else if (Character.isLetter(c)) {
            context.generateToken(TokenType.UNKNOW);
        } else {
            flushToken(context);
            context.getState().process(c, length, index, context);
        }
    }

    private boolean isPartOfNumber(char c) {
        return (Character.isDigit(c) || c == '.');
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
