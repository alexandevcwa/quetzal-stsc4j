package com.stsc4j.parser.v1.parser;

import com.stsc4j.lexer.LexerContext;
import com.stsc4j.lexer.Token;
import com.stsc4j.parser.v1.ast.StatementLoopFor;
import com.stsc4j.parser.v1.exception.ParserException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ParserLoopFor - Tests")
class ParserLoopForTest {

    private static LexerContext context;
    private static List<Token> tokens;
    private static TokenStream tokenStream;
    public static ParserLoopFor parser;

    @AfterEach
    void cleanTokens() {
        tokenStream.clear();
        context.cleanToken();
    }

    @BeforeAll
    static void setup() {
        context = new LexerContext();
        tokens = new ArrayList<>();
        tokenStream = new TokenStream(tokens);

        var parserExpression = new ParserExpression(tokenStream);
        parser = new ParserLoopFor(
                tokenStream,
                new ParserDeclaration(tokenStream, parserExpression),
                parserExpression,
                new ParserBlock(tokenStream, new ParserPrincipal(tokenStream))
        );
    }

    @Test
    @DisplayName("Test - Declaración de for loop correcta")
    void testLoopForCorrecto() {
        final String code = "para (entero var i = 0; i < 5; i++) {\n" +
                " entero b = 1+i" +
                " a-- " +
                "}";
        context.process(code);
        tokens.addAll(context.getTokens());
        var ast = parser.parseStatement();
        assertThat(ast).isNotNull();
        assertThat(ast).matches(s -> s instanceof StatementLoopFor);
    }

    @Test
    @DisplayName("Test - Declaración de for loop incorrecta")
    void testLoopForIncorrecto() {
        final String code = "para (entero var i = 0; i < 5; i+) {\n" +
                " entero b = 1+i" +
                " a-- " +
                "}";
        context.process(code);
        tokens.addAll(context.getTokens());
        assertThrows(ParserException.class, () -> parser.parseStatement());
    }

    @Test
    @DisplayName("Test - Declaración de for loop incorrecta sin paréntesis")
    void testLoopForSinParentesis() {
        final String code = "para entero var i = 0; i < 5; i++ {\n" +
                " entero b = 1+i" +
                " a-- " +
                "}";
        context.process(code);
        tokens.addAll(context.getTokens());
        assertThrows(ParserException.class, () -> parser.parseStatement());
    }

    @Test
    @DisplayName("Test - Declaración de for loop incorrecta sin expresión de incremento")
    void testLoopForSinVariable() {
        final String code = "para (i < 5; i++) {\n" +
                " entero b = 1+i" +
                " a-- " +
                "}";
        context.process(code);
        tokens.addAll(context.getTokens());
        assertThrows(ParserException.class, () -> parser.parseStatement());
    }

    @Test
    @DisplayName("Test - Declaración de for loop incorrecta con operador de incremento mal declarado")
    void testLoopForIncrementalMalDeclarado() {
        final String code = "para (entero var i = 0; i < 5; i+-) {\n" +
                " entero b = 1+i" +
                " a-- " +
                "}";
        context.process(code);
        tokens.addAll(context.getTokens());
        assertThrows(ParserException.class, () -> parser.parseStatement());
    }

    @Test
    @DisplayName("Test - Declaración de for loop incorrecta sin expresión condicional")
    void testLoopForSinExpresionCondicional() {
        final String code = "para (entero var i = 0; ; i++) {\n" +
                " entero b = 1+i" +
                " a-- " +
                "}";
        context.process(code);
        tokens.addAll(context.getTokens());
        assertThrows(ParserException.class, () -> parser.parseStatement());
    }

    @Test
    @DisplayName("Test - Declaración de for loop incorrecta sin bloque")
    void testLoopForSinBloque() {
        final String code = "para (entero var i = 0; i < 5; i++)";
        context.process(code);
        tokens.addAll(context.getTokens());
        assertThrows(ParserException.class, () -> parser.parseStatement());
    }

    @Test
    @DisplayName("Test - Declaración de for loop incorrecta sin expresiones")
    void testLoopForSinExpresiones() {
        final String code = "para () {\n" +
                " entero b = 1+i" +
                " a-- " +
                "}";
        context.process(code);
        tokens.addAll(context.getTokens());
        assertThrows(ParserException.class, () -> parser.parseStatement());
    }

    @Test
    @DisplayName("Test - Declaración de for loop correcta con variable externa")
    void testLoopForConVariableExterna(){
        final String code = "para (i; i < 5; i++) {\n" +
                " entero b = 1+i" +
                " a-- " +
                "}";
        context.process(code);
        tokens.addAll(context.getTokens());
        var ast = parser.parseStatement();
        assertThat(ast).isNotNull();
        assertThat(ast).matches(s -> s instanceof StatementLoopFor);
    }
}