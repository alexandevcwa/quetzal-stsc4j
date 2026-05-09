package com.stsc4j.parser.v1.parser;

import com.stsc4j.lexer.LexerContext;
import com.stsc4j.lexer.Token;
import com.stsc4j.parser.v1.ast.ASTPrinter;
import com.stsc4j.parser.v1.ast.StatementConsolaOut;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ParserConsoleOutTest {

    private static LexerContext context;
    private static List<Token> tokens;
    private static TokenStream tokenStream;
    private static final ASTPrinter astPrinter = new ASTPrinter();

    private static ParserConsoleOut parser;

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
        parser = new ParserConsoleOut(tokenStream, new ParserExpressions(tokenStream));
    }

    @Test
    @DisplayName("Test - Llamada a función de consola con función válida")
    void testCOutCorrecto(){
        final String code = "consola.mostrar(\"¡Hola, Quetzal!\")";
        context.process(code);
        tokens.addAll(context.getTokens());
        var ast = assertDoesNotThrow(() -> parser.parseStatement());
        assertNotNull(ast);
        assertThat(ast).matches(s -> s instanceof StatementConsolaOut);
        System.out.println(astPrinter.print(ast));
    }

    @Test
    @DisplayName("Test - Llamada a función de consola con función no válida")
    void testCOutIncorrecto(){
        final String code = "consola.mostrarr(5)";
        context.process(code);
        tokens.addAll(context.getTokens());
        assertThrows(RuntimeException.class, () -> parser.parseStatement());
    }

    @Test
    @DisplayName("Test - Llamada a función de consola sin expresión")
    void testCOutSinExpresionEnFuncion(){
        final String code = "consola.mostrar()";
        context.process(code);
        tokens.addAll(context.getTokens());
        assertThrows(RuntimeException.class, () -> parser.parseStatement());
    }

    @Test
    @DisplayName("Test - Llamada a función de consola sin función")
    void testCOutSinFuncion(){
        final String code = "consola.(\"Hola\")";
        context.process(code);
        tokens.addAll(context.getTokens());
        assertThrows(RuntimeException.class, () -> parser.parseStatement());
    }
}