package com.stsc4j.parser;

import com.stsc4j.lexer.LexerContext;
import com.stsc4j.parser.ast.ASTPrinter;
import com.stsc4j.parser.ast.statement.Statement;
import com.stsc4j.parser.parser.Parser;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Ejemplo de código fuente
        String sourceCode = "log mayor = a > b";

        System.out.println("=== Código fuente ===");
        System.out.println(sourceCode);
        System.out.println();

        // Análisis léxico
        LexerContext context = new LexerContext();
        context.process(sourceCode);

        System.out.println("=== Tokens ===");
        context.getTokens().forEach(System.out::println);
        System.out.println();

        // Análisis sintáctico
        Parser parser = new Parser(context.getTokens());
        List<Statement> statements = parser.parse();

        System.out.println("=== AST (Árbol de Sintaxis Abstracta) ===");
        ASTPrinter printer = new ASTPrinter();
        for (Statement stmt : statements) {
            System.out.println(printer.print(stmt));
        }

        // Mostrar errores si los hay
        if (!parser.getErrors().isEmpty()) {
            System.out.println("=== Errores ===");
            parser.getErrors().forEach(System.out::println);
        }
    }
}
