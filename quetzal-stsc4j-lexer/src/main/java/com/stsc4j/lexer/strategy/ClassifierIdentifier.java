package com.stsc4j.lexer.strategy;

import com.stsc4j.lexer.TokenType;

public class ClassifierIdentifier implements Classifier {

    @Override
    public boolean match(String s) {
        return s.matches("^[a-zA-Z_][a-zA-Z0-9_]*$");
    }

    @Override
    public TokenType classify(String s) {
        return TokenType.IDENTIFIER;
    }
}
