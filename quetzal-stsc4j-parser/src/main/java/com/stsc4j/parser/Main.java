package com.stsc4j.parser;

import com.stsc4j.lexer.LexerContext;
import com.stsc4j.parser.v1.ast.ASTPrinter;
import com.stsc4j.parser.v1.ast.Statement;
import com.stsc4j.parser.v1.parser.ParserPrincipal;
import com.stsc4j.parser.v1.parser.TokenStream;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Ejemplo de código fuente
        String sourceCode = "si (x > 10) {\n" +
                "    y = 1\n" +
                "} sino {\n" +
                "    y = 2\n" +
                "}" +
                "" +
                "texto saludar(texto a1, texto a2){ retornar a + b }";


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
        ParserPrincipal principal = new ParserPrincipal(new TokenStream(context.getTokens()));
        List<Statement> astTree = principal.parse();

        System.out.println("=== AST ===");
        ASTPrinter printer = new ASTPrinter();
        for (Statement statement : astTree) {
            System.out.println(printer.print(statement));
        }
        
    }
}
