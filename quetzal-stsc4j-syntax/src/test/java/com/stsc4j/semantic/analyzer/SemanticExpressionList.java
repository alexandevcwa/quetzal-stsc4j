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

@DisplayName("SemanticExpressionListTest")
class SemanticExpressionListTest {

    private static LexerContext context;
    private static List<Token> tokens;
    private static TokenStream tokenStream;
    private static ParserPrincipal parser;
    private final SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer();

    @AfterEach
    void clean() {
        tokenStream.clear();
        context.cleanToken();
        tokens.clear(); // Limpieza crucial para evitar acumulación de tokens entre ejecuciones
    }

    @BeforeAll
    static void setup() {
        context = new LexerContext();
        tokens = new ArrayList<>();
        tokenStream = new TokenStream(tokens);
        parser = new ParserPrincipal(tokenStream);
    }

    @Test
    @DisplayName("Test - Declaración de lista tipada homogénea (números decimales)")
    void testListaHomogeneaDecimales() {
        final String code = "lista<número> precios = [10.5, 20.3, 15.0]";
        context.process(code);
        tokens.addAll(context.getTokens());
        var ast = parser.parse();

        assertNotNull(ast);
        assertDoesNotThrow(() -> semanticAnalyzer.analyze(ast));
    }

    @Test
    @DisplayName("Test - Coerción e Inferencia: Acepta mezcla de entero y número expandiendo a número")
    void testListaMezclaEnteroYDecimalValida() {
        // Tu SemanticExpressionList ensancha el '10' a 'número' automáticamente.
        // Por ende, esta lista es homogéneamente válida y NO debe lanzar error.
        final String code = "lista<número> precios = [10, 20.3, 15.0]";
        context.process(code);
        tokens.addAll(context.getTokens());
        var ast = parser.parse();

        assertNotNull(ast);
        assertDoesNotThrow(() -> semanticAnalyzer.analyze(ast));
    }

    @Test
    @DisplayName("Test - Declaración de lista lanza error si se mezclan tipos incompatibles (Texto y Entero)")
    void testListaHeterogeneaError() {
        final String code = "lista<entero> mixta = [1, \"dos\", 3]";
        context.process(code);
        tokens.addAll(context.getTokens());
        var ast = parser.parse();

        var ex = assertThrows(SemanticError.class, () -> semanticAnalyzer.analyze(ast));

        // ACTUALIZADO: Ahora buscamos el mensaje real que lanza tu compilador
        assertTrue(ex.getMessage().contains("se encontró un elemento de tipo"),
                "El error no coincide con la validación de la lista. Error actual: " + ex.getMessage());
    }

    @Test
    @DisplayName("Test - Declaración de lista vacía tipada pasa el análisis sin romper")
    void testListaVacia() {
        final String code = "lista<entero> vacia = []";
        context.process(code);
        tokens.addAll(context.getTokens());
        var ast = parser.parse();

        assertNotNull(ast);
        assertDoesNotThrow(() -> semanticAnalyzer.analyze(ast));
    }
}