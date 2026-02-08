package com.stsc4j.lexer.strategy;

import com.stsc4j.lexer.TokenType;

public class ClassifierLiterals implements Classifier{

    @Override
    public boolean match(String s) {
        return false;
    }

    @Override
    public TokenType classify(String s) {
        return null;
    }
}
