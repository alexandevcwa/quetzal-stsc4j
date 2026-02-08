package com.stsc4j.lexer.strategy;

import com.stsc4j.lexer.TokenType;

import static com.stsc4j.lexer.strategy.LexerDictionary.*;

public class ClassifierKeywords implements Classifier {

    @Override
    public boolean match(String s) {
        return LexerDictionary.KEYWORDS.contains(s);
    }

    @Override
    public TokenType classify(String s) {
        TokenType type = null;
        switch (s) {
            case ENTERO:
                type = TokenType.PRIMITIVE_LONG;
                break;
            case NUMERO:
                type = TokenType.PRIMITIVE_DOUBLE;
                break;
            case TEXTO:
                type = TokenType.PRIMITIVE_STRING;
                break;
            case LOG:
                type = TokenType.PRIMITIVE_BOOLEAN;
                break;
        }
        return type;
    }
}
