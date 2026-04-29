package com.stsc4j.lexer.strategy;

import com.stsc4j.lexer.TokenType;

import java.util.HashMap;
import java.util.Map;

import static com.stsc4j.lexer.strategy.LexerDictionary.*;

public class ClassifierSymbols implements Classifier {

    private static final Map<Character, TokenType> SYMBOLS_MAP = new HashMap<>();

    static {
        SYMBOLS_MAP.put(PLUS, TokenType.PLUS);
        SYMBOLS_MAP.put(MINUS, TokenType.MINUS);
        SYMBOLS_MAP.put(MULTIPLY, TokenType.MULTIPLY);
        SYMBOLS_MAP.put(DIVIDE, TokenType.DIVIDE);
        SYMBOLS_MAP.put(EQUALS, TokenType.EQUAL);
        SYMBOLS_MAP.put(EXCLAMATION, TokenType.EXCLAMATION);
        SYMBOLS_MAP.put(LESS_THAN, TokenType.LESS_THAN);
        SYMBOLS_MAP.put(GREATER_THAN, TokenType.GREATER_THAN);
        SYMBOLS_MAP.put(AND, TokenType.AND);
        SYMBOLS_MAP.put(AND_ESP, TokenType.AND_ESP);
        SYMBOLS_MAP.put(OR, TokenType.OR);
        SYMBOLS_MAP.put(OR_ESP, TokenType.OR_ESP);
        SYMBOLS_MAP.put(MODULO, TokenType.MODULE);
        SYMBOLS_MAP.put(PARENTHESES_OPEN, TokenType.LEFT_PARENT);
        SYMBOLS_MAP.put(PARENTHESES_CLOSE, TokenType.RIGHT_PARENT);
        SYMBOLS_MAP.put(BRACKETS_OPEN, TokenType.BRACKETS_OPEN);
        SYMBOLS_MAP.put(BRACKETS_CLOSE, TokenType.BRACKETS_CLOSE);
        SYMBOLS_MAP.put(BRACES_OPEN, TokenType.BRACES_OPEN);
        SYMBOLS_MAP.put(BRACES_CLOSE, TokenType.BRACES_CLOSE);
        SYMBOLS_MAP.put(COMMA, TokenType.COMMA);
        SYMBOLS_MAP.put(DOUBLE_DOT, TokenType.DOUBLE_DOT);
        SYMBOLS_MAP.put(DOT, TokenType.DOT);
        SYMBOLS_MAP.put(QUESTION,TokenType.QUESTION);
    }

    @Override
    public boolean match(String s) {
        return SYMBOLS.contains(s.toCharArray()[0]);
    }

    @Override
    public TokenType classify(String s) {
        return SYMBOLS_MAP.getOrDefault(s.toCharArray()[0], TokenType.UNKNOW);
    }
}
