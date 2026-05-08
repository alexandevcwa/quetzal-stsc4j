package com.stsc4j.lexer;

public enum TokenType {

    // Keywords
    PRIMITIVE_VOID,
    PRIMITIVE_INTEGER,
    PRIMITIVE_DECIMAL,
    PRIMITIVE_STRING,
    PRIMITIVE_BOOLEAN,
    MUTABLE_VARIABLE,
    IF,
    ELSE,
    NULL,
    LIST,
    JSN,
    LOOP_WHILE,
    LOOP_DO,
    LOOP_FOR,
    LOOP_EACH_1,
    LOOP_EACH_2,
    BREAK,
    CONTINUE,
    RETURN,
    THROW,
    TRY,
    CATCH,
    EXCEPTION,
    FINALLY,
    OBJECT,
    PUBLIC_ACCESS,
    PRIVATE_ACCESS,
    STATIC,
    THIS,
    ASYNC,
    AWAIT,
    NEW_INSTANCE,
    IMPORT_MODULE,
    IMPORT_MODULE_L,

    // Identifier for variable, function, class, etc.
    IDENTIFIER,
    UNKNOW,


    // Symbols
    LEFT_PARENT,
    RIGHT_PARENT,
    PLUS,
    MINUS,
    MULTIPLY,
    DIVIDE,
    EQUAL,
    EXCLAMATION,
    GREATER_THAN,
    LESS_THAN,
    AND,
    AND_ESP,
    OR,
    OR_ESP,
    MODULE,
    BRACKETS_OPEN,
    BRACKETS_CLOSE,
    BRACES_OPEN,
    BRACES_CLOSE,
    COMMA,
    DOUBLE_DOT,
    SEMICOLON,
    DOT,
    QUESTION,

    // LITERALS
    LIT_INTEGER,
    LIT_DECIMAL,
    LIT_STRING,
    LIT_TRUE,
    LIT_FALSE,

    // End of file/input
    EOF,

    // RESERVED KEYWORDS FOR QUETZAL
    C_CONSOLE,
    F_PRINT,
}
