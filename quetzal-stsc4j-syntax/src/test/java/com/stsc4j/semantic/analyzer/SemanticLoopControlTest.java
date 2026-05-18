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

@DisplayName("SemanticLoopControlTest - Romper y Continuar")
class SemanticLoopControlTest {

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
    @DisplayName("Éxito: Romper y Continuar dentro de un ciclo válido")
    void testBreakDentroDeCiclo() {
        final String code =
                "entero contador = 0\n" +
                        "mientras (contador < 10) {\n" +
                        "    si (contador == 5) {\n" +
                        "        romper\n" + // Válido porque el IF está dentro de un WHILE
                        "    }\n" +
                        "    contador++\n" +
                        "}\n";

        context.process(code);
        tokens.addAll(context.getTokens());
        var ast = parser.parse();

        assertDoesNotThrow(() -> semanticAnalyzer.analyze(ast));
    }

    @Test
    @DisplayName("Error: Romper fuera de un ciclo")
    void testBreakFueraDeCiclo() {
        final String code =
                "entero edad = 18\n" +
                        "si (edad > 15) {\n" +
                        "    romper\n" + // ILEGAL: El IF no es un ciclo
                        "}\n";

        context.process(code);
        tokens.addAll(context.getTokens());
        var ast = parser.parse();

        var ex = assertThrows(SemanticError.class, () -> semanticAnalyzer.analyze(ast));
        assertTrue(ex.getMessage().contains("solo puede usarse dentro de un ciclo"));
    }
}