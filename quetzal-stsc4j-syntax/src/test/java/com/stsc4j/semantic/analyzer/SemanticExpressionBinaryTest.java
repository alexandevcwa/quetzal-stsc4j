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

@DisplayName("SemanticExpressionBinaryTest - Fase 2")
class SemanticExpressionBinaryTest {

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
    @DisplayName("Test - Suma matemática y asignación a entero")
    void testMatematicaValida(){
        // ENTERO + ENTERO = ENTERO
        final String code = "entero resultado = 10 + 5";
        context.process(code);
        tokens.addAll(context.getTokens());
        var ast = parser.parse();
        assertDoesNotThrow(() -> semanticAnalyzer.analyze(ast));
    }

    @Test
    @DisplayName("Test - Coerción Matemática: Entero * Decimal = Decimal")
    void testMatematicaMixta(){
        // ENTERO * DECIMAL genera un DECIMAL, y encaja en 'número'
        final String code = "número area = 10 * 3.14";
        context.process(code);
        tokens.addAll(context.getTokens());
        var ast = parser.parse();
        assertDoesNotThrow(() -> semanticAnalyzer.analyze(ast));
    }

    @Test
    @DisplayName("Test - Concatenación: String + Entero = String")
    void testConcatenacion(){
        final String code = "texto saludo = \"Edad: \" + 25";
        context.process(code);
        tokens.addAll(context.getTokens());
        var ast = parser.parse();
        assertDoesNotThrow(() -> semanticAnalyzer.analyze(ast));
    }

    @Test
    @DisplayName("Test - Error Semántico: Restar un texto es ilegal")
    void testOperacionMatematicaIlegal(){
        final String code = "entero fallo = 10 - \"hola\"";
        context.process(code);
        tokens.addAll(context.getTokens());
        var ast = parser.parse();

        var ex = assertThrows(SemanticError.class, () -> semanticAnalyzer.analyze(ast));
        assertTrue(ex.getMessage().contains("solo puede usarse con números"));
    }

    @Test
    @DisplayName("Test - Relacional: Las comparaciones generan Booleanos")
    void testRelacional(){
        // 10 > 5 produce BOOLEANO, el cual se guarda en una variable 'log'
        final String code = "log mayor = 10 > 5";
        context.process(code);
        tokens.addAll(context.getTokens());
        var ast = parser.parse();
        assertDoesNotThrow(() -> semanticAnalyzer.analyze(ast));
    }

    @Test
    @DisplayName("Test - Error Semántico: Lógica (&&) exige booleanos")
    void testLogicaIlegal(){
        final String code = "log fallo = 10 && 20";
        context.process(code);
        tokens.addAll(context.getTokens());
        var ast = parser.parse();

        var ex = assertThrows(SemanticError.class, () -> semanticAnalyzer.analyze(ast));
        assertTrue(ex.getMessage().contains("solo pueden evaluar valores booleanos"));
    }
}