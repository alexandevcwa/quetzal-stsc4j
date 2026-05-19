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
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("SemanticIncDecTest - Validación de Operadores Incrementales")
class SemanticIncDecTest {

    private static LexerContext context;
    private static List<Token> tokens;
    private static TokenStream tokenStream;
    private static ParserPrincipal parser;
    private SemanticAnalyzer semanticAnalyzer;

    @AfterEach
    void clean() {
        tokenStream.clear();
        context.cleanToken();
        tokens.clear();
    }

    @BeforeAll
    static void setup() {
        context = new LexerContext();
        tokens = new ArrayList<>();
        tokenStream = new TokenStream(tokens);
        parser = new ParserPrincipal(tokenStream);
    }

    @Test
    @DisplayName("Éxito: Incremento a una variable entera mutable")
    void testIncrementoValidoEntero() {
        semanticAnalyzer = new SemanticAnalyzer();
        final String code =
                "entero var contador = 0\n" +
                        "contador++\n";

        ejecutar(code);
    }

    @Test
    @DisplayName("Éxito: Decremento a una variable decimal mutable")
    void testDecrementoValidoDecimal() {
        semanticAnalyzer = new SemanticAnalyzer();
        final String code =
                "número var saldo = 100.5\n" +
                        "saldo--\n";

        ejecutar(code);
    }

    @Test
    @DisplayName("Error Semántico: Intentar incrementar una constante (sin var)")
    void testErrorIncrementarConstante() {
        semanticAnalyzer = new SemanticAnalyzer();
        final String code =
                "entero contador = 10\n" +
                        "contador++\n";

        context.process(code);
        tokens.addAll(context.getTokens());
        var ast = parser.parse();

        // Esperamos que bloquee por inmutabilidad
        assertThrows(com.stsc4j.semantic.SemanticError.class, () -> semanticAnalyzer.analyze(ast));
    }

    @Test
    @DisplayName("Error Semántico: Intentar incrementar un texto")
    void testErrorIncrementarTipoInvalido() {
        semanticAnalyzer = new SemanticAnalyzer();
        final String code =
                "texto var saludo = \"Hola\"\n" +
                        "saludo++\n";

        context.process(code);
        tokens.addAll(context.getTokens());
        var ast = parser.parse();

        // Esperamos que bloquee por tipo de dato incompatible
        assertThrows(com.stsc4j.semantic.SemanticError.class, () -> semanticAnalyzer.analyze(ast));
    }

    private void ejecutar(String code) {
        context.process(code);
        tokens.addAll(context.getTokens());
        var ast = parser.parse();
        assertDoesNotThrow(() -> semanticAnalyzer.analyze(ast));
    }
}