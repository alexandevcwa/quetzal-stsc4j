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

class ParserPrincipalTest {

    private static LexerContext context;
    private static List<Token> tokens;
    private static TokenStream tokenStream;
    private static ParserPrincipal parser;

    private final static ASTPrinter astPrinter = new ASTPrinter();

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
        parser = new ParserPrincipal(tokenStream);
    }

    @Test
    @DisplayName("Test - Asignar nuevo valor a propiedad JSN")
    void testAccesoPropiedadesNuevoValor() {
        final String code = "jsn var persona = {nombre: \"Juan\", edad: 25}\n" +
                "persona.nombre = \"Maria\"";
        context.process(code);
        tokens.addAll(context.getTokens());
        var ast = assertDoesNotThrow(() -> parser.parse());
        assertNotNull(ast);
        ast.forEach(s -> System.out.println(astPrinter.print(s)));
    }

    @Test
    @DisplayName("Test - Asignar nuevo valor a indice de matriz")
    void testAsignarValor_A_Matriz_Por_Indice() {
        final String code = "lista var puntajes = [1,2,3,4,5]\n" +
                "puntajes[2] = 10";
        context.process(code);
        tokens.addAll(context.getTokens());
        var ast = assertDoesNotThrow(() -> parser.parse());
        assertNotNull(ast);
    }

}