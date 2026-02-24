package com.stsc4j.lexer;

public class Main {
    public static void main(String[] args) {
        final LexerContext context = new LexerContext();

        System.out.println("=============CODE============");

        String code = "entero mayor = 123\n entero mad = 4f4";
        context.process(code);
        context.getTokens().forEach(System.out::println);
        context.cleanToken();
    }
}
