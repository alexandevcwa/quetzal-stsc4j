package com.stsc4j.semantic.analyzer;

import com.stsc4j.lexer.LexerContext;
import com.stsc4j.lexer.Token;
import com.stsc4j.parser.v1.parser.ParserPrincipal;
import com.stsc4j.parser.v1.parser.TokenStream;
import com.stsc4j.semantic.SemanticAnalyzer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@DisplayName("SemanticControlFlow - Diagnóstico del Parser")
class SemanticControlFlowIntegrationTest {

    private static LexerContext context;
    private static List<Token> tokens;
    private static TokenStream tokenStream;
    private static ParserPrincipal parser;
    private SemanticAnalyzer semanticAnalyzer;

    @AfterEach
    void clean(){
        tokenStream.clear();
        context.cleanToken();
        tokens.clear();
    }

    @BeforeAll
    static void setup(){
        context = new LexerContext();
        tokens = new ArrayList<>();
        tokenStream = new TokenStream(tokens);
        parser = new ParserPrincipal(tokenStream);
    }

    @Test
    @DisplayName("Prueba 1 - Variables básicas y Condicional IF")
    void testSoloIf() {
        semanticAnalyzer = new SemanticAnalyzer();
        final String code =
                "entero limite = 5\n" +
                        "log activo = verdadero\n" +
                        "si (limite > 3) {\n" +
                        "    entero temporal = 10\n" +
                        "}\n";
        ejecutar(code);
    }

    @Test
    @DisplayName("Prueba 2 - Ciclos Mientras y Hacer-Mientras")
    void testSoloWhile() {
        semanticAnalyzer = new SemanticAnalyzer();
        final String code =
                "entero contador = 0\n" +
                        "mientras (contador < 3) {\n" +
                        "    contador++\n" +
                        "}\n" +
                        "hacer {\n" +
                        "    contador--\n" +
                        "} mientras (contador > 0)\n";
        ejecutar(code);
    }

    @Test
    @DisplayName("Prueba 3 - Solo ciclo FOR (Sin Listas)")
    void testSoloFor() {
        semanticAnalyzer = new SemanticAnalyzer();
        final String code =
                "entero limite = 5\n" +
                        "para (entero i = 0; i < limite; i++) {\n" +
                        "    entero multiplicador = 2\n" +
                        "}\n";
        ejecutar(code);
    }

    // Método auxiliar para no repetir código
    private void ejecutar(String code) {
        context.process(code);
        tokens.addAll(context.getTokens());
        var ast = parser.parse();
        assertDoesNotThrow(() -> semanticAnalyzer.analyze(ast));
    }
}