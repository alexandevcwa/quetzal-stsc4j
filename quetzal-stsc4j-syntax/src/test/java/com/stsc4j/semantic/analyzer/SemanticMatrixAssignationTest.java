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

@DisplayName("SemanticMatrixAssignationTest - Validación de Arreglos y Matrices")
class SemanticMatrixAssignationTest {

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
    @DisplayName("Éxito: Modificar un índice de una lista mutable")
    void testAsignacionValidaMatriz() {
        semanticAnalyzer = new SemanticAnalyzer();
        // Asumiendo que el tipo es 'lista' y que el parser de Alex soporta arreglos [ ]
        final String code =
                "lista var puntajes = [10, 20, 30]\n" +
                        "puntajes[0] = 50\n";

        ejecutar(code);
    }

    @Test
    @DisplayName("Éxito: Coerción al asignar un entero a una lista de decimales")
    void testAsignacionValidaCoercionMatriz() {
        semanticAnalyzer = new SemanticAnalyzer();
        final String code =
                "lista var precios = [10.5, 20.5]\n" +
                        "precios[1] = 100\n"; // 100 es entero, pero entra en decimal sin problema

        ejecutar(code);
    }

    @Test
    @DisplayName("Error Semántico: Modificar un índice de una lista constante (sin var)")
    void testErrorInmutabilidadMatriz() {
        semanticAnalyzer = new SemanticAnalyzer();
        final String code =
                "lista puntajes = [10, 20, 30]\n" +
                        "puntajes[0] = 50\n";

        context.process(code);
        tokens.addAll(context.getTokens());
        var ast = parser.parse();

        // Esperamos que bloquee porque no tiene 'var'
        assertThrows(com.stsc4j.semantic.SemanticError.class, () -> semanticAnalyzer.analyze(ast));
    }

    @Test
    @DisplayName("Error Semántico: Asignar un texto a una lista de números")
    void testErrorTipoIncompatibleMatriz() {
        semanticAnalyzer = new SemanticAnalyzer();

        // Le ponemos lista<entero> para que no sea 'mixta' y active la validación estricta
        final String code =
                "lista<entero> var puntajes = [10, 20, 30]\n" +
                        "puntajes[0] = \"Hola\"\n";

        context.process(code);
        tokens.addAll(context.getTokens());
        var ast = parser.parse();

        assertThrows(com.stsc4j.semantic.SemanticError.class, () -> semanticAnalyzer.analyze(ast));
    }

    private void ejecutar(String code) {
        context.process(code);
        tokens.addAll(context.getTokens());
        var ast = parser.parse();
        assertDoesNotThrow(() -> semanticAnalyzer.analyze(ast));
    }
}