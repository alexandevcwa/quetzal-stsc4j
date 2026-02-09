package com.stsc4j.lexer.state;

import com.stsc4j.lexer.LexerContext;
import com.stsc4j.lexer.strategy.Classifier;
import com.stsc4j.lexer.strategy.ClassifierSymbols;

import static com.stsc4j.lexer.strategy.LexerDictionary.SYMBOLS;

public class SymbolState implements LexerState {

    public static final Classifier SYMBOL_CLASSIFIER = new ClassifierSymbols();

    @Override
    public void process(char c, int length, int index, LexerContext lexer) {
        if (SYMBOLS.contains(c)) {
            lexer.add(c);
            lexer.generateToken(SYMBOL_CLASSIFIER.classify(String.valueOf(c)));
        } else {
            lexer.setState(new InitialState());
            lexer.getState().process(c, length, index, lexer);
        }
    }

    @Override
    public void finalize(LexerContext lexer) {

    }
}
