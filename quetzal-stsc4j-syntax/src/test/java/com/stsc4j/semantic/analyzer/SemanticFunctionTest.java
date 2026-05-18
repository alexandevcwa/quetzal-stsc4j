package com.stsc4j.semantic.analyzer;

import com.stsc4j.lexer.LexerContext;
import com.stsc4j.lexer.Token;
import com.stsc4j.parser.v1.parser.ParserPrincipal;
import com.stsc4j.parser.v1.parser.TokenStream;
import com.stsc4j.semantic.SemanticAnalyzer;
import com.stsc4j.semantic.SemanticError;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("SemanticFunctionTest - Fase 4")
class SemanticFunctionTest {

    private static LexerContext context;
    private static List<Token> tokens;
    private static TokenStream tokenStream;
    private static ParserPrincipal parser;
    private final SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer();

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
    @DisplayName("Test - Definición y llamada correcta de función")
    void testFuncionCorrecta() {
        final String code =
                "entero duplicar(entero x) {\n" +
                        "    retornar x * 2\n" +
                        "}\n" +
                        "entero resultado = duplicar(5)\n";

        context.process(code);
        tokens.addAll(context.getTokens());
        var ast = parser.parse();

        assertDoesNotThrow(() -> semanticAnalyzer.analyze(ast));
    }

    @Test
    @DisplayName("Test - Error si el tipo retornado no coincide con la promesa")
    void testFuncionRetornoInvalido() {
        // Promete entero pero devuelve texto ("hola")
        final String code =
                "entero romperContrato() {\n" +
                        "    retornar \"hola\"\n" +
                        "}\n";

        context.process(code);
        tokens.addAll(context.getTokens());
        var ast = parser.parse();

        var ex = assertThrows(SemanticError.class, () -> semanticAnalyzer.analyze(ast));
        assertTrue(ex.getMessage().contains("promete devolver"));
    }

    @Test
    @DisplayName("Test - Error por Aridad incorrecta (falla cantidad de argumentos)")
    void testFuncionAridadInvalida() {
        // Espera 2 argumentos, pero solo le mandamos 1
        final String code =
                "entero sumar(entero a, entero b) {\n" +
                        "    retornar a + b\n" +
                        "}\n" +
                        "entero total = sumar(10)\n";

        context.process(code);
        tokens.addAll(context.getTokens());
        var ast = parser.parse();

        var ex = assertThrows(SemanticError.class, () -> semanticAnalyzer.analyze(ast));
        assertTrue(ex.getMessage().contains("espera 2 argumentos"));
    }

    @Test
    @DisplayName("Test - Error por tipo de argumento incompatible")
    void testFuncionArgumentoIncompatible() {
        // Espera un entero, le enviamos un texto ("cinco")
        final String code =
                "entero cuadrado(entero n) {\n" +
                        "    retornar n * n\n" +
                        "}\n" +
                        "entero res = cuadrado(\"cinco\")\n";

        context.process(code);
        tokens.addAll(context.getTokens());
        var ast = parser.parse();

        var ex = assertThrows(SemanticError.class, () -> semanticAnalyzer.analyze(ast));
        assertTrue(ex.getMessage().contains("Argumento inválido en la posición 1"));
    }

    @Test
    @DisplayName("Test - Error al usar retornar fuera de una función")
    void testReturnHuerfano() {
        final String code = "retornar 10\n";

        context.process(code);
        tokens.addAll(context.getTokens());
        var ast = parser.parse();

        var ex = assertThrows(SemanticError.class, () -> semanticAnalyzer.analyze(ast));
        assertTrue(ex.getMessage().contains("no puede usarse fuera de una función"));
    }
}