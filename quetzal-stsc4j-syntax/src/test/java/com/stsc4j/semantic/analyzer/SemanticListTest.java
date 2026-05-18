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

@DisplayName("SemanticListTest - Estructuras de Datos y Matrices")
class SemanticListTest {

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
    @DisplayName("Éxito: Declaración de lista tipada válida")
    void testListaTipadaValida() {
        semanticAnalyzer = new SemanticAnalyzer();
        final String code =
                "lista<entero> numeros = [10, 20, 30]\n";

        context.process(code);
        tokens.addAll(context.getTokens());
        var ast = parser.parse();

        assertDoesNotThrow(() -> semanticAnalyzer.analyze(ast));
    }

    @Test
    @DisplayName("Error: Intruso en una lista tipada")
    void testListaTipadaConIntruso() {
        semanticAnalyzer = new SemanticAnalyzer();
        // Prometemos enteros, pero intentamos meter un texto
        final String code =
                "lista<entero> numeros = [10, \"veinte\", 30]\n";

        context.process(code);
        tokens.addAll(context.getTokens());
        var ast = parser.parse();

        var ex = assertThrows(SemanticError.class, () -> semanticAnalyzer.analyze(ast));
        assertTrue(ex.getMessage().contains("esperaba elementos de tipo 'integer'"), "El error debe mencionar el tipo esperado.");
    }

    @Test
    @DisplayName("Éxito: Acceso válido a índice y asignación")
    void testAccesoIndiceValido() {
        semanticAnalyzer = new SemanticAnalyzer();
        // Accedemos a la lista usando un número entero y lo guardamos en un entero
        final String code =
                "lista<entero> puntos = [100, 200]\n" +
                        "entero miPunto = puntos[1]\n";

        context.process(code);
        tokens.addAll(context.getTokens());
        var ast = parser.parse();

        assertDoesNotThrow(() -> semanticAnalyzer.analyze(ast));
    }

    @Test
    @DisplayName("Error: Índice de lista no es un número entero")
    void testAccesoIndiceInvalido() {
        semanticAnalyzer = new SemanticAnalyzer();

        // Engañamos al parser guardando el texto en una variable.
        // El Parser lo dejará pasar, pero el Semántico detectará que 'indiceMal' no es un entero.
        final String code =
                "lista<entero> puntos = [100, 200]\n" +
                        "texto indiceMal = \"cero\"\n" +
                        "entero miPunto = puntos[indiceMal]\n";

        context.process(code);
        tokens.addAll(context.getTokens());
        var ast = parser.parse();

        var ex = assertThrows(SemanticError.class, () -> semanticAnalyzer.analyze(ast));
        assertTrue(ex.getMessage().contains("estrictamente números enteros"), "El error debe quejarse del índice no entero.");
    }

    @Test
    @DisplayName("Éxito: Acceso profundo a una matriz (Listas 2D)")
    void testAccesoMatriz() {
        semanticAnalyzer = new SemanticAnalyzer();
        // Una lista de listas. Accedemos a fila 0, columna 1.
        final String code =
                "lista<lista<entero>> matriz = [[1, 2], [3, 4]]\n" +
                        "entero valor = matriz[0][1]\n";

        context.process(code);
        tokens.addAll(context.getTokens());
        var ast = parser.parse();

        assertDoesNotThrow(() -> semanticAnalyzer.analyze(ast));
    }
}