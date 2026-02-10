package com.stsc4j.lexer.strategy;


import java.util.Set;

public class LexerDictionary {

    public static final String ENTERO = "entero";
    public static final String NUMERO = "numero";
    public static final String TEXTO = "texto";
    public static final String LOG = "log";
    public static final String LIT_TRUE = "verdadero";
    public static final String LIT_FALSE = "falso";
    public static final Set<String> KEYWORDS = Set.of(ENTERO, NUMERO, TEXTO, LOG, LIT_TRUE, LIT_FALSE);

    public static final char PLUS = '+';
    public static final char MINUS = '-';
    public static final char MULTIPLY = '*';
    public static final char DIVIDE = '/';
    public static final char EQUALS = '=';
    public static final char EXCLAMATION = '!';
    public static final char LESS_THAN = '<';
    public static final char GREATER_THAN = '>';
    public static final char AND = '&';
    public static final char OR = '|';
    public static final char MODULO = '%';
    public static final char PARENTHESES_OPEN = '(';
    public static final char PARENTHESES_CLOSE = ')';
    public static final char BRACKETS_OPEN = '[';
    public static final char BRACKETS_CLOSE = ']';
    public static final Set<Character> SYMBOLS = Set.of(PLUS, MINUS, MULTIPLY, DIVIDE, EQUALS, EXCLAMATION,
            LESS_THAN, GREATER_THAN, AND, OR, MODULO, PARENTHESES_OPEN, PARENTHESES_CLOSE, BRACKETS_OPEN, BRACKETS_CLOSE
    );
}
