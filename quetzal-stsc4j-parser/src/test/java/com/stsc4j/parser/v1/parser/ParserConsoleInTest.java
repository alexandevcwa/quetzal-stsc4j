package com.stsc4j.parser.v1.parser;

import com.stsc4j.lexer.LexerContext;
import com.stsc4j.lexer.Token;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ParserConsoleInTest {

    private static LexerContext context;
    private static List<Token> tokens;
    private static TokenStream tokenStream;

    private static ParserConsoleIn parser;

    @AfterEach
    void clean() {
        tokenStream.clear();
        context.cleanToken();
    }

    @BeforeAll
    static void setup() {
        context = new LexerContext();
        tokens = new ArrayList<>();
        tokenStream = new TokenStream(tokens);
        parser = new ParserConsoleIn(tokenStream, new ParserExpression(tokenStream));
    }

    @Test
    @DisplayName("Test - Declaración correcta de consola.pedir")
    void testDeclaracionCorrecta(){
        final String code = "consola.pedir(\"Nombre:\")";
        context.process(code);
        tokens.addAll(context.getTokens());
        var ast = assertDoesNotThrow(() -> parser.parseExpression());
        assertNotNull(ast);
    }

    @Test
    @DisplayName("Test - Declaración incorrecta de consola.pedir")
    void testDeclaracionIncorrecta(){
        final String code = "consola.pedir(1)";
        context.process(code);
        tokens.addAll(context.getTokens());
        assertThrows(RuntimeException.class, () -> parser.parseExpression());
    }

    @Test
    @DisplayName("Test - Declaración sin paréntesis")
    void testDeclaracionSinParentesis(){
        final String code = "consola.pedir";
        context.process(code);
        tokens.addAll(context.getTokens());
        assertThrows(RuntimeException.class, () -> parser.parseExpression());
    }

    @Test
    @DisplayName("Test - Declaración sin valor")
    void testDeclaracionSinValor(){
        final String code = "consola.pedir()";
        context.process(code);
        tokens.addAll(context.getTokens());
        assertThrows(RuntimeException.class, () -> parser.parseExpression());
    }
}