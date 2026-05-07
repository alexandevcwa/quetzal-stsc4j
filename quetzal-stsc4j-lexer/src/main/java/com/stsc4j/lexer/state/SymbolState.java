package com.stsc4j.lexer.state;

import com.stsc4j.lexer.LexerContext;
import com.stsc4j.lexer.strategy.Classifier;
import com.stsc4j.lexer.strategy.ClassifierSymbols;

import static com.stsc4j.lexer.strategy.LexerDictionary.SYMBOLS;

public class SymbolState implements LexerState {

    public static final Classifier SYMBOL_CLASSIFIER = new ClassifierSymbols();

    @Override
    public void process(char c, int length, int index, LexerContext context) {
        if (SYMBOLS.contains(c)) {
            context.add(c);
            context.generateToken(SYMBOL_CLASSIFIER.classify(String.valueOf(c)));
        } else if(!Character.isLetterOrDigit(c) && !Character.isWhitespace(c)) {
            context.throwContext();
        }
        else {
            context.setState(new InitialState());
            context.getState().process(c, length, index, context);
        }
    }

    @Override
    public void finalize(LexerContext lexer) {

    }
}
