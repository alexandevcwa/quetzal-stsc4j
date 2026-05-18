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

@DisplayName("SemanticControlFlowTest - Fase 3")
class SemanticControlFlowTest {

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
    @DisplayName("Test - IF con condición booleana válida")
    void testIfCondicionValida(){
        // Quetzal debe permitir esto porque 10 > 5 devuelve BOOLEANO
        final String code = "si (10 > 5) { entero a = 1 }";
        context.process(code);
        tokens.addAll(context.getTokens());
        var ast = parser.parse();
        assertDoesNotThrow(() -> semanticAnalyzer.analyze(ast));
    }

    @Test
    @DisplayName("Test - IF lanza error si la condición no es booleana")
    void testIfCondicionInvalida(){
        // "hola" no es un booleano. El compilador debe estallar.
        final String code = "si (\"hola\") { entero a = 1 }";
        context.process(code);
        tokens.addAll(context.getTokens());
        var ast = parser.parse();

        var ex = assertThrows(SemanticError.class, () -> semanticAnalyzer.analyze(ast));
        assertTrue(ex.getMessage().contains("debe ser booleana"), "Mensaje de error incorrecto: " + ex.getMessage());
    }

    @Test
    @DisplayName("Test - Scope: Las variables locales mueren fuera de su bloque")
    void testScopeVariablesLocales(){
        // Declaramos 'temporal' dentro del if. Intentamos llamarla afuera.
        // Debe lanzar error diciendo que 'temporal' no está definida.
        final String code = "si (verdadero) { entero temporal = 1 } \n entero b = temporal + 5";
        context.process(code);
        tokens.addAll(context.getTokens());
        var ast = parser.parse();

        var ex = assertThrows(SemanticError.class, () -> semanticAnalyzer.analyze(ast));
        assertTrue(ex.getMessage().contains("no ha sido definida"), "Mensaje de error incorrecto: " + ex.getMessage());
    }
}