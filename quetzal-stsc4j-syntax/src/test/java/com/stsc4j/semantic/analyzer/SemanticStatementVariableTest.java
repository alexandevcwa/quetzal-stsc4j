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

@DisplayName("SemanticStatementVariableTest")
class SemanticStatementVariableTest {

    private static LexerContext context;
    private static List<Token> tokens;
    private static TokenStream tokenStream;
    private static ParserPrincipal parser;
    private SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer();

    @AfterEach
    void clean(){
        tokenStream.clear();
        context.cleanToken();
    }

    @BeforeAll
    static void setup(){
        context = new LexerContext();
        tokens = new ArrayList<>();
        tokenStream = new TokenStream(tokens);
        parser = new ParserPrincipal(tokenStream);
    }

    @Test
    @DisplayName("Test - Declaración de variable correcta")
    void testDecVariableCorrecta(){
        final String code = "entero edad = 25";
        context.process(code);
        tokens.addAll(context.getTokens());
        var ast = parser.parse();
        assertNotNull(ast);
        assertDoesNotThrow(() -> semanticAnalyzer.analyze(ast));
    }

    @Test
    @DisplayName("Test - Declaración de variable con tipo incorrecto")
    void testDecVariableIncorrecta(){
        final String code = "entero edad = 1.1";
        context.process(code);
        tokens.addAll(context.getTokens());
        var ast = parser.parse();
        var ex = assertThrows(SemanticError.class, () -> semanticAnalyzer.analyze(ast));
        System.out.println(ex.getMessage());
    }

    @Test
    @DisplayName("Test - Declaración de variable con tipo incorrecto")
    void testDecVariableNoDeclarada(){
        final String code = "entero edad = 1\n entero edad2 = edad1 + 4";
        context.process(code);
        tokens.addAll(context.getTokens());
        var ast = parser.parse();
        var ex = assertThrows(SemanticError.class, () -> semanticAnalyzer.analyze(ast));
        System.out.println(ex.getMessage());
    }

    @Test
    @DisplayName("Test - Declaración de variable con tipo correcto y asignación correcta")
    void testDecVariableDeclaradaAsignada(){
        final String code = "entero edad = 1\n entero edad2 = edad";
        context.process(code);
        tokens.addAll(context.getTokens());
        var ast = parser.parse();
        assertNotNull(ast);
        assertDoesNotThrow(() -> semanticAnalyzer.analyze(ast));
    }

    @Test
    @DisplayName("Test - Declaración de variable con tipo correcto y asignación incorrecta por tipo diferente")
    void testDecVariableAsignarVariableConTipoDiferente(){
        final String code = "entero edad = 1\n número edad2 = edad";
        context.process(code);
        tokens.addAll(context.getTokens());
        var ast = parser.parse();
        assertDoesNotThrow(() -> semanticAnalyzer.analyze(ast));
    }

}