package com.stsc4j.lexer.strategy;

import com.stsc4j.lexer.TokenType;

public interface Classifier {
    boolean match(String s);
    TokenType classify(String s);
}
