package com.stsc4j.parser.v1.parser;

import com.stsc4j.lexer.LexerContext;
import com.stsc4j.lexer.Token;
import com.stsc4j.parser.v1.ast.ASTPrinter;
import com.stsc4j.parser.v1.exception.ParserException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ParserLoopForEach - Tests")
class ParserLoopForEachTest {

    private static LexerContext context;
    private static List<Token> tokens;
    private static TokenStream tokenStream;
    private final static ASTPrinter astPrinter = new ASTPrinter();

    public static ParserLoopForEach parser;

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

        var parserExpression = new ParserExpressions(tokenStream);
        parser = new ParserLoopForEach(
                tokenStream,
                parserExpression,
                new ParserBlock(tokenStream, new ParserPrincipal(tokenStream))
        );
    }

    @Test
    @DisplayName("Test - Declaración de for-each loop correcta")
    void testLoopDeclaracionCorrecta(){
        final String code ="para (entero var valor_numero en lista_numeros) {\n" +
                "a = 1+1\n" +
                "}";
        context.process(code);
        tokens.addAll(context.getTokens());
        var ast = parser.parseStatement();
        assertNotNull(ast);
        System.out.println(astPrinter.print(ast));
    }

    @Test
    @DisplayName("Test - Declaración de for-each loop incorrecta sin 'var' en variable")
    void testLoopDeclaracionIncorrecta(){
        final String code ="para (entero valor_numero en lista_numeros) {\n" +
                "a = 1+1\n" +
                "}";
        context.process(code);
        tokens.addAll(context.getTokens());
        var ex = assertThrows(ParserException.class, () -> parser.parseStatement());
        System.out.println(ex.getMessage());
    }

    @Test
    @DisplayName("Test - Declaración de for-each loop incorrecta sin tipo de variable")
    void testLoopDeclaracionSinVariable(){
        final String code ="para (var a1 en lista_numeros) {\n" +
                "a = 1+1\n" +
                "}";
        context.process(code);
        tokens.addAll(context.getTokens());
        var ex = assertThrows(ParserException.class, () -> parser.parseStatement());
        System.out.println(ex.getMessage());
    }

    @Test
    @DisplayName("Test - Declaración de for-each loop incorrecta sin lista de iteración")
    void testLoopDeclaracionSinLista(){
        final String code ="para (entero var valor_numero en ) {\n" +
                "a = 1+1\n" +
                "}";
        context.process(code);
        tokens.addAll(context.getTokens());
        var ex = assertThrows(ParserException.class, () -> parser.parseStatement());
        System.out.println(ex.getMessage());
    }

    @Test
    @DisplayName("Test - Declaración de for-each loop incorrecta sin palabra reservada 'en' o 'cada'")
    void testLoopDeclaracionSinPalabraReservada(){
        final String code ="para (entero var valor_numero  lista_numeros) {\n" +
                "a = 1+1\n" +
                "}";
        context.process(code);
        tokens.addAll(context.getTokens());
        var ex = assertThrows(ParserException.class, () -> parser.parseStatement());
        System.out.println(ex.getMessage());
    }

    @Test
    @DisplayName("Test - Declaración de for-each loop incorrecta sin bloque de código")
    void testLoopDeclaracionSinBloque() {
        final String code = "para (entero var valor_numero en lista_numeros)";
        context.process(code);
        tokens.addAll(context.getTokens());
        var ex = assertThrows(ParserException.class, () -> parser.parseStatement());
        System.out.println(ex.getMessage());
    }
}