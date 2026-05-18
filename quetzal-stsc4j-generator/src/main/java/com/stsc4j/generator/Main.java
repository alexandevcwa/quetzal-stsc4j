package com.stsc4j.generator;

import com.stsc4j.lexer.LexerContext;
import com.stsc4j.lexer.Token;
import com.stsc4j.parser.v1.ast.Statement;
import com.stsc4j.parser.v1.ast.ASTPrinter; // Importamos tu impresor de árboles
import com.stsc4j.parser.v1.parser.ParserPrincipal;
import com.stsc4j.parser.v1.parser.TokenStream;
import com.stsc4j.semantic.SemanticAnalyzer;

import java.util.List;

public class Main {
    public static void main(String[] args) {

        String codigoQuetzal = "entero a = 15 \n" +
                "número b = 15.5 \n"+
                "número d = 10 \n"+
                "número c = 3 \n"+
                "consola.mostrar (d/c)" ;

                /*"consola.mostrar (a*b) \n"+
                "consola.mostrar (a/b) \n"+
                "consola.mostrar (c/d) \n"+
                "consola.mostrar (d/c) \n"+
                "consola.mostrar (a+b)" ;*/

        System.out.println("======================================");
        System.out.println("🦅 COMPILADOR QUETZAL INICIADO 🦅");
        System.out.println("======================================");
        System.out.println("Código a compilar:\n" + codigoQuetzal);
        System.out.println("--------------------------------------");

        try {
            // ==========================================
            // FASE 1: ANÁLISIS LÉXICO (LEXER)
            // ==========================================
            System.out.print("\n[1/4] Ejecutando Lexer... ");
            LexerContext lexer = new LexerContext();
            lexer.process(codigoQuetzal);
            List<Token> tokens = lexer.getTokens();
            System.out.println("✅ (" + tokens.size() + " tokens encontrados)");

            System.out.println("      🔍 RESULTADO LEXER:");
            for (Token t : tokens) {
                System.out.println("         [" + t.getType() + "] -> '" + t.getLexeme() + "'");
            }

            // ==========================================
            // FASE 2: ANÁLISIS SINTÁCTICO (PARSER)
            // ==========================================
            System.out.print("\n[2/4] Ejecutando Parser... ");
            TokenStream tokenStream = new TokenStream(tokens);
            ParserPrincipal parser = new ParserPrincipal(tokenStream);
            List<Statement> ast = parser.parse();
            System.out.println("✅ (AST construido con " + ast.size() + " sentencias)");

            System.out.println("      🌳 RESULTADO PARSER (AST):");
            ASTPrinter astPrinter = new ASTPrinter();
            for (Statement stmt : ast) {
                // Usamos tu ASTPrinter para mostrar la estructura jerárquica
                System.out.println("         " + astPrinter.print(stmt).replace("\n", "\n         "));
            }

            // ==========================================
            // FASE 3: ANÁLISIS SEMÁNTICO
            // ==========================================
            System.out.print("\n[3/4] Ejecutando Análisis Semántico... ");
            SemanticAnalyzer semantico = new SemanticAnalyzer();
            semantico.analyze(ast);
            // El semántico no devuelve un árbol, si pasa sin lanzar errores, es un éxito.
            System.out.println("✅ (Reglas de tipado validadas correctamente)");

            // ==========================================
            // FASE 4: GENERACIÓN DE CÓDIGO (BYTECODE)
            // ==========================================
            System.out.print("\n[4/4] Generando Bytecode JVM... ");
            BytecodeGenerator generador = new BytecodeGenerator();
            generador.compile(ast, "ProgramaQuetzal");
            // El BytecodeGenerator imprime internamente su propio mensaje "¡Compilación exitosa!"

            System.out.println("\n======================================");
            System.out.println("🎉 ¡PROCESO COMPLETADO CON ÉXITO! 🎉");
            System.out.println("======================================");

        } catch (Exception e) {
            System.err.println("❌");
            System.err.println("\n======================================");
            System.err.println("🛑 ERROR EN LA COMPILACIÓN 🛑");
            System.err.println("======================================");
            System.err.println(e.getMessage());
        }
    }
}