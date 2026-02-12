package com.stsc4j.parser;

import com.stsc4j.lexer.LexerContext;
import com.stsc4j.parser.ast.Parser;
import com.stsc4j.parser.ast.Statement;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        LexerContext context = new LexerContext();
        context.process("entero a = 10");
        context.getTokens().forEach(System.out::println);
        Parser parser = new Parser(context.getTokens());
        List<Statement> statements = parser.parse();
        statements.forEach(s -> {
            System.out.println(s);
        });
    }
}
