package com.stsc4j.lexer.strategy;

import com.stsc4j.lexer.TokenType;

public class ClassifierIntegerLiterals implements Classifier {
    @Override
    public boolean match(String s) {
        return s.matches("^[0-9]+$");
    }

    @Override
    public TokenType classify(String s) {
        return TokenType.LIT_LONG;
    }
}
