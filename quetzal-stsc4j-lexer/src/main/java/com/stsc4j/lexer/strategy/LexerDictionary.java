package com.stsc4j.lexer.strategy;


import java.util.Set;

public class LexerDictionary {

    public static final String ENTERO = "entero";
    public static final String NUMERO = "numero";
    public static final String TEXTO = "texto";
    public static final String LOG = "log";
    public static final Set<String> KEYWORDS = Set.of(ENTERO, NUMERO, TEXTO, LOG);


    public static final Set<String> BOOLEAN_LITERALS = Set.of("verdadero", "falso");


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
    public static final Set<Character> OPERATORS = Set.of(PLUS, MINUS, MULTIPLY, DIVIDE, EQUALS, EXCLAMATION, LESS_THAN, GREATER_THAN, AND, OR, MODULO);

    public static final Set<Character> PARENTHESES = Set.of('(', ')');

    public static final Set<Character> BRACKETS = Set.of('[', ']');

}
