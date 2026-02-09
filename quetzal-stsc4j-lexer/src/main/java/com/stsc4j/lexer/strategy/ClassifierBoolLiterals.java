package com.stsc4j.lexer.strategy;

import com.stsc4j.lexer.TokenType;

import static com.stsc4j.lexer.strategy.LexerDictionary.*;

public class ClassifierBoolLiterals implements Classifier {

    @Override
    public boolean match(String s) {
        return BOOLEAN_LITERALS.contains(s);
    }

    @Override
    public TokenType classify(String s) {
        TokenType type = null;
        switch (s) {
            case LIT_TRUE:
                type = TokenType.LIT_TRUE;
                break;
            case LIT_FALSE:
                type = TokenType.LIT_FALSE;
                break;
        }
        return type;
    }
}
