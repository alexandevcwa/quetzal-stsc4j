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

@DisplayName("ParserPropertyAssignation - Tests")
class ParserPropertyAssignationTest {
    private static LexerContext context;
    private static List<Token> tokens;
    private static TokenStream tokenStream;

    private static ParserPropertyAssignation parser;
    private static final ASTPrinter astPrinter = new ASTPrinter();

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
        parser = new ParserPropertyAssignation(tokenStream, new ParserExpression(tokenStream));
    }

    @Test
    @DisplayName("Test - Asignación correcta a propiedad")
    void testPA_AsignacionCorrecta(){
        final String code = "a.b.c = 1";
        context.process(code);
        tokens.addAll(context.getTokens());
        var ast = assertDoesNotThrow(() -> parser.parseStatement());
        assertNotNull(ast);
        System.out.println(astPrinter.print(ast));
    }

    @Test
    @DisplayName("Test - Asignación correcta por funciones anidadas")
    void testPA_AsignacionAccedientoPorFuncionesAnidadas(){
        final String code = "a.b().v1().c = 1";
        context.process(code);
        tokens.addAll(context.getTokens());
        var ast = assertDoesNotThrow(() -> parser.parseStatement());
        assertNotNull(ast);
        System.out.println(astPrinter.print(ast));
    }

    @Test
    @DisplayName("Test - Asignación incorrecta a propiedad (falta expresión derecha)")
    void testPA_AsignacionIncorrecta(){
        final String code = "a.b.c = ";
        context.process(code);
        tokens.addAll(context.getTokens());
        var ex = assertThrows(ParserException.class, () -> parser.parseStatement());
        assertNotNull(ex);
        System.out.println(ex.getMessage());
    }
}