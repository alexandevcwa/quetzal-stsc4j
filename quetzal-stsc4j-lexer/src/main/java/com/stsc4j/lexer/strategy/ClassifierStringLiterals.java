package com.stsc4j.lexer.strategy;

import com.stsc4j.lexer.TokenType;

public class ClassifierStringLiterals implements Classifier {

    @Override
    public boolean match(String s) {
        return (s.startsWith("\"") && s.endsWith("\""));
    }

    @Override
    public TokenType classify(String s) {
        return TokenType.LIT_STRING;
    }
}
