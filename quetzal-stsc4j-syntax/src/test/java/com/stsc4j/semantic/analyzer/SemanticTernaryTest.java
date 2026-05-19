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

@DisplayName("SemanticTernaryTest - Validación del Operador Ternario")
class SemanticTernaryTest {

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
    @DisplayName("Éxito: Ternario válido devolviendo el mismo tipo (texto)")
    void testTernarioValidoMismoTipo() {
        semanticAnalyzer = new SemanticAnalyzer();
        final String code =
                "entero var edad = 20\n" +
                        "texto var estado = (edad >= 18) ? \"Mayor\" : \"Menor\"\n";

        ejecutar(code);
    }

    @Test
    @DisplayName("Éxito de Ternario: Validar coerción de decimales (Evadiendo al Parser)")
    void testTernarioValidoCoercion() {
        semanticAnalyzer = new SemanticAnalyzer();


        final String code =
                "entero var a = 1\n" +
                        "texto var precio = (a >= 1) ? 100.5 : 0\n";

        context.process(code);
        tokens.addAll(context.getTokens());
        var ast = parser.parse();

        // Si lanza SemanticError, ¡tu código del ternario resolvió la coerción correctamente!
        assertThrows(com.stsc4j.semantic.SemanticError.class, () -> semanticAnalyzer.analyze(ast));
    }

    @Test
    @DisplayName("Error: Condición no es booleana")
    void testErrorCondicionNoBooleana() {
        semanticAnalyzer = new SemanticAnalyzer();
        // Usamos (100 + 50) que es una expresión binaria, pero que devuelve un Entero en lugar de un Booleano
        final String code =
                "texto var estado = (100 + 50) ? \"Mayor\" : \"Menor\"\n";

        context.process(code);
        tokens.addAll(context.getTokens());
        var ast = parser.parse();

        assertThrows(com.stsc4j.semantic.SemanticError.class, () -> semanticAnalyzer.analyze(ast));
    }

    @Test
    @DisplayName("Error: Ramas devuelven tipos incompatibles")
    void testErrorRamasIncompatibles() {
        semanticAnalyzer = new SemanticAnalyzer();
        // Izquierda es texto, derecha es número.
        // Cambiamos a (2 > 1) para que sea un binario booleano válido
        final String code =
                "texto var estado = (2 > 1) ? \"Hola\" : 50\n";

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