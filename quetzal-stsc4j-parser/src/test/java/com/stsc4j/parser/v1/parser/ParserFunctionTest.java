package com.stsc4j.parser.v1.parser;

import com.stsc4j.lexer.LexerContext;
import com.stsc4j.lexer.Token;
import com.stsc4j.parser.v1.ast.ASTPrinter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ParserFunctionTest {

    private static LexerContext context;
    private static List<Token> tokens;
    private static TokenStream tokenStream;
    private final static ASTPrinter astPrinter = new ASTPrinter();

    private static ParserFunction parser;

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
        parser = new ParserFunction(tokenStream, new ParserPrincipal(tokenStream));
    }

    @Test
    @DisplayName("Test - Función con sufijo a variable")
    void testFuncionSufijo(){
        final String code = "texto agregar_sufijo(texto var base) {\n" +
                "    base += \" agregado\"\n" +
                "    retornar base\n" +
                "}";
        context.process(code);
        tokens.addAll(context.getTokens());
        var ast = assertDoesNotThrow(() -> parser.parseStatement());
        assertNotNull(ast);
    }

    @Test
    @DisplayName("Test - Función con parámetros y retorno de función")
    void testFuncionFactorial(){
        final String code = "entero factorial(entero n) {\n" +
                "    si (n == 0) {\n" +
                "        retornar 1\n" +
                "    }\n" +
                "    retornar n * factorial(n - 1)\n" +
                "}";
        context.process(code);
        tokens.addAll(context.getTokens());
        var ast = assertDoesNotThrow(() -> parser.parseStatement());
        assertNotNull(ast);
    }

    @Test
    @DisplayName("Test - Función con parámetros y retorno de función con múltiples parámetros")
    void testFuncionSuma(){
        final String code = "número sumar(número a, número b) {\n" +
                "    retornar a + b\n" +
                "}";
        context.process(code);
        tokens.addAll(context.getTokens());
        var ast = assertDoesNotThrow(() -> parser.parseStatement());
        assertNotNull(ast);
    }

    @Test
    @DisplayName("Test - Función con con retorno boolean")
    void testFuncionLogaritmo(){
        final String code = "log es_par(entero valor) {\n" +
                "    retornar valor % 2 == 0\n" +
                "}";
        context.process(code);
        tokens.addAll(context.getTokens());
        var ast = assertDoesNotThrow(() -> parser.parseStatement());
        assertNotNull(ast);
    }

    @Test
    @DisplayName("Test - Función con retorno de tipo lista tipada")
    void testFuncionConRetornoListaTipada(){
        final String code = "lista<entero> obtener_numeros() {\n" +
                "retornar [1, 2, 3, 4, 5]\n" +
                "}";
        context.process(code);
        tokens.addAll(context.getTokens());
        var ast = assertDoesNotThrow(() -> parser.parseStatement());
        assertNotNull(ast);
    }
}