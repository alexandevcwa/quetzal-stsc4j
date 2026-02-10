package com.stsc4j.lexer;

public class Main {
    public static void main(String[] args) {
        final LexerContext context = new LexerContext();
        context.process("numero n = \"H\"");
        context.getTokens().forEach(System.out::println);
        context.cleanToken();

        System.out.println("==============================");
        context.process("numero n = 10  texto = 1232.43 log=verdadero \"hola mundo desde quetzal \"");
        context.getTokens().forEach(System.out::println);
    }
}
