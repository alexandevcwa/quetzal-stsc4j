package com.stsc4j.lexer;

public class Main {
    public static void main(String[] args) {
        final LexerContext context = new LexerContext();
        context.process("numero num1 =");
        context.getTokens().forEach(System.out::println);
    }
}
