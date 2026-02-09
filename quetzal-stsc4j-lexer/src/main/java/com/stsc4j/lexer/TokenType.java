package com.stsc4j.lexer;

public enum TokenType {
    ////////////////
    /// KEYWORDS ///
    ////////////////
    PRIMITIVE_VOID, PRIMITIVE_LONG,
    PRIMITIVE_INT, PRIMITIVE_SHORT,
    PRIMITIVE_DOUBLE, PRIMITIVE_FLOAT,
    PRIMITIVE_STRING, PRIMITIVE_BOOLEAN,

    IDENTIFIER, SPACE, EOF, UNKNOW, END_LINE,


    ///////////////////////////
    /// SYMBOLS & OPERATORS ///
    ///////////////////////////
    // PARENTHESES
    LEFT_PARENT, RIGHT_PARENT,
    // BRACKETS
    LEFT_BRACKET, RIGHT_BRACKET,
    // OPERATORS
    PLUS, MINUS,
    MULTIPLY, DIVIDE,
    EQUAL, EXCLAMATION,
    GREATER_THAN, LESS_THAN,
    AND, OR, MODULO,

    // LITERALS
    LIT_LONG, LIT_INT,
    LIT_SHORT, LIT_DOUBLE,
    LIT_FLOAT, LIT_STRING,
    LIT_TRUE, LIT_FALSE,

    // OTHERS
    DOT

}
