package com.stsc4j.lexer.strategy;


import java.util.Set;

public class LexerDictionary {

    public static final String ENTERO = "entero";
    public static final String NUMERO = "número";
    public static final String TEXTO = "texto";
    public static final String LOG = "log";
    public static final String LIT_TRUE = "verdadero";
    public static final String LIT_FALSE = "falso";
    public static final String MUTABLE_VARIABLE = "var";
    public static final String IF = "si";
    public static final String ELSE = "sino";
    public static final String NULL = "nulo";
    public static final String LIST = "lista";
    public static final String JSN = "jsn";
    public static final String LOOP_WHILE = "mientras";
    public static final String LOOP_DO = "hacer";
    public static final String LOOP_FOR = "para";
    public static final String LOOP_EACH_1 = "en";
    public static final String LOOP_EACH_2 = "cada";
    public static final String BREAK = "romper";
    public static final String CONTINUE = "continuar";
    public static final String RETURN = "retornar";
    public static final String THROW = "lanzar";
    public static final String TRY = "intentar";
    public static final String CATCH = "capturar";
    public static final String EXCEPTION = "excepcion";
    public static final String FINALLY = "finalmente";
    public static final String OBJECT = "objeto";
    public static final String PUBLIC_ACCESS = "publico";
    public static final String PRIVATE_ACCESS = "privado";
    public static final String STATIC = "libre";
    public static final String THIS = "ambiente";
    public static final String ASYNC = "asincrono";
    public static final String AWAIT = "esperar";
    public static final String NEW_INSTANCE = "nuevo";
    public static final String IMPORT_MODULE = "importar";
    public static final String IMPORT_MODULE_L = "desde";

    // Palabras, Funciones Predefinidas
    public static final String C_CONSOLE = "consola";
    public static final String F_PRINT = "mostrar";
    public static final String F_PRINT_ERROR = "mostrar_error";
    public static final String F_PRINT_WARNING = "mostrar_advertencia";
    public static final String F_PRINT_INFO = "mostrar_informacion";
    public static final String F_PRINT_SUCCESS = "mostrar_exito";
    public static final String F_SCANNER = "pedir";
    public static final String F_SCANNER_SECRET = "pedir_secreto";

    public static final String AND_ESP = "y";
    public static final String OR_ESP = "o";

    public static final Set<String> KEYWORDS = Set.of(
            ENTERO, NUMERO, TEXTO, LOG, LIT_TRUE, LIT_FALSE, MUTABLE_VARIABLE,
            IF, ELSE, NULL, LIST, JSN, LOOP_WHILE, LOOP_DO, LOOP_FOR, LOOP_EACH_1,
            LOOP_EACH_2, BREAK, CONTINUE, RETURN, THROW, TRY, CATCH, EXCEPTION,
            FINALLY, OBJECT, PUBLIC_ACCESS, PRIVATE_ACCESS, STATIC, THIS, ASYNC, AWAIT,
            NEW_INSTANCE, IMPORT_MODULE, IMPORT_MODULE_L, C_CONSOLE, F_PRINT,
            AND_ESP, OR_ESP
    );

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
    public static final char BRACES_OPEN = '{';
    public static final char BRACES_CLOSE = '}';
    public static final char COMMA = ',';
    public static final char COLON = ':';
    public static final char SEMICOLON = ';';
    public static final char DOT = '.';
    public static final char QUESTION = '?';
    public static final Set<Character> SYMBOLS = Set.of(
            PLUS, MINUS, MULTIPLY, DIVIDE, EQUALS, EXCLAMATION,
            LESS_THAN, GREATER_THAN, AND, OR, MODULO, PARENTHESES_OPEN,
            PARENTHESES_CLOSE, BRACKETS_OPEN, BRACKETS_CLOSE,
            BRACES_OPEN, BRACES_CLOSE,
            COMMA,
            COLON, SEMICOLON, DOT,
            QUESTION
    );
}
