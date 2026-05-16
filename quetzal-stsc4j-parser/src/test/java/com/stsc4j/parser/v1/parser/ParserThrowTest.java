package com.stsc4j.parser.v1.parser;

import com.stsc4j.lexer.LexerContext;
import com.stsc4j.lexer.Token;
import com.stsc4j.parser.v1.exception.ParserException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ParserThrow - Tests")
class ParserThrowTest {

    private static LexerContext context;
    private static List<Token> tokens;
    private static TokenStream tokenStream;

    private static ParserThrow parser;

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
        parser = new ParserThrow(tokenStream, new ParserExpression(tokenStream));
    }

    @Test
    @DisplayName("Test - Throw con mensaje valido")
    void testThrowConMensajeValido() {
        final String code = "lanzar \"Error de prueba\"";
        context.process(code);
        tokens.addAll(context.getTokens());
        var ast = assertDoesNotThrow(() -> parser.parseStatement());
        assertNotNull(ast);
    }

    @Test
    @DisplayName("Test - Throw con mensaje invalido")
    void testThrowConMensajeInvalido(){
        final String code = "lanzar 1";
        context.process(code);
        tokens.addAll(context.getTokens());
        assertThrows(ParserException.class, () -> parser.parseStatement());
    }

    @Test
    @DisplayName("Test - Throw sin mensaje")
    void testThrowSinMensaje(){
        final String code = "lanzar";
        context.process(code);
        tokens.addAll(context.getTokens());
        assertThrows(ParserException.class, () -> parser.parseStatement());
    }

    @Test
    @DisplayName("Test - Throw con expresión binaria")
    void testThrowSinExpresionBinaria(){
        final String code = "lanzar \"Error de \" + 1";
    }
}