package com.stsc4j.lexer.strategy;

import com.stsc4j.lexer.TokenType;

import java.util.HashMap;
import java.util.Map;

import static com.stsc4j.lexer.strategy.LexerDictionary.*;

public class ClassifierKeywords implements Classifier {

    private static final Map<String, TokenType> KEYWORDS_MAP = new HashMap<>();

    static {
        KEYWORDS_MAP.put(ENTERO, TokenType.PRIMITIVE_INTEGER);
        KEYWORDS_MAP.put(NUMERO, TokenType.PRIMITIVE_DECIMAL);
        KEYWORDS_MAP.put(TEXTO, TokenType.PRIMITIVE_STRING);
        KEYWORDS_MAP.put(LOG, TokenType.PRIMITIVE_BOOLEAN);
        KEYWORDS_MAP.put(LIT_TRUE, TokenType.LIT_TRUE);
        KEYWORDS_MAP.put(LIT_FALSE, TokenType.LIT_FALSE);
        KEYWORDS_MAP.put(MUTABLE_VARIABLE, TokenType.MUTABLE_VARIABLE);
        KEYWORDS_MAP.put(IF, TokenType.IF);
        KEYWORDS_MAP.put(ELSE, TokenType.ELSE);
        KEYWORDS_MAP.put(NULL, TokenType.NULL);
        KEYWORDS_MAP.put(LIST, TokenType.LIST);
        KEYWORDS_MAP.put(JSN, TokenType.JSN);
        KEYWORDS_MAP.put(LOOP_WHILE, TokenType.LOOP_WHILE);
        KEYWORDS_MAP.put(LOOP_DO, TokenType.LOOP_DO);
        KEYWORDS_MAP.put(LOOP_FOR, TokenType.LOOP_FOR);
        KEYWORDS_MAP.put(LOOP_EACH_1, TokenType.LOOP_EACH_1);
        KEYWORDS_MAP.put(LOOP_EACH_2, TokenType.LOOP_EACH_2);
        KEYWORDS_MAP.put(BREAK, TokenType.BREAK);
        KEYWORDS_MAP.put(CONTINUE, TokenType.CONTINUE);
        KEYWORDS_MAP.put(RETURN, TokenType.RETURN);
        KEYWORDS_MAP.put(THROW, TokenType.THROW);
        KEYWORDS_MAP.put(TRY, TokenType.TRY);
        KEYWORDS_MAP.put(CATCH, TokenType.CATCH);
        KEYWORDS_MAP.put(EXCEPTION, TokenType.EXCEPTION);
        KEYWORDS_MAP.put(FINALLY, TokenType.FINALLY);
        KEYWORDS_MAP.put(OBJECT, TokenType.OBJECT);
        KEYWORDS_MAP.put(PUBLIC_ACCESS, TokenType.PUBLIC_ACCESS);
        KEYWORDS_MAP.put(PRIVATE_ACCESS, TokenType.PRIVATE_ACCESS);
        KEYWORDS_MAP.put(STATIC, TokenType.STATIC);
        KEYWORDS_MAP.put(THIS, TokenType.THIS);
        KEYWORDS_MAP.put(ASYNC, TokenType.ASYNC);
        KEYWORDS_MAP.put(AWAIT, TokenType.AWAIT);
        KEYWORDS_MAP.put(NEW_INSTANCE, TokenType.NEW_INSTANCE);
        KEYWORDS_MAP.put(IMPORT_MODULE, TokenType.IMPORT_MODULE);
        KEYWORDS_MAP.put(IMPORT_MODULE_L, TokenType.IMPORT_MODULE_L);
        KEYWORDS_MAP.put(C_CONSOLE, TokenType.C_CONSOLE);
        KEYWORDS_MAP.put(F_PRINT,TokenType.F_PRINT);
    }

    @Override
    public boolean match(String s) {
        return LexerDictionary.KEYWORDS.contains(s);
    }

    @Override
    public TokenType classify(String s) {
        return KEYWORDS_MAP.getOrDefault(s, TokenType.UNKNOW);
    }
}
