package com.stsc4j.lexer.strategy;

import com.stsc4j.lexer.TokenType;

import static com.stsc4j.lexer.strategy.LexerDictionary.*;

public class ClassifierOperators implements Classifier {

    @Override
    public boolean match(String s) {
        return LexerDictionary.OPERATORS.contains(s.toCharArray()[0]);
    }

    @Override
    public TokenType classify(String s) {
        TokenType type = null;

        switch (s.toCharArray()[0]) {
            case PLUS:
                type = TokenType.PLUS;
                break;
            case MINUS:
                type = TokenType.MINUS;
                break;
            case MULTIPLY:
                type = TokenType.MULTIPLY;
                break;
            case DIVIDE:
                type = TokenType.DIVIDE;
                break;
            case EQUALS:
                type = TokenType.EQUAL;
                break;
            case EXCLAMATION:
                type = TokenType.EXCLAMATION;
                break;
            case LESS_THAN:
                type = TokenType.LESS_THAN;
                break;
            case GREATER_THAN:
                type = TokenType.GREATER_THAN;
                break;
            case AND:
                type = TokenType.AND;
                break;
            case OR:
                type = TokenType.OR;
                break;
            case MODULO:
                type = TokenType.MODULO;
                break;
        }

        return type;
    }
}
