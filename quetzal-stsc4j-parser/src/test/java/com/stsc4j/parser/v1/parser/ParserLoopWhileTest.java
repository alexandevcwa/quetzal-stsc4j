package com.stsc4j.parser.v1.parser;

import com.stsc4j.lexer.LexerContext;
import com.stsc4j.lexer.Token;
import com.stsc4j.parser.v1.ast.ASTPrinter;
import com.stsc4j.parser.v1.ast.StatementLoopWhile;
import com.stsc4j.parser.v1.exception.ParserException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("ParserLoopWhile - Tests")
class ParserLoopWhileTest {

    private static LexerContext context;
    private static List<Token> tokens;
    private static TokenStream tokenStream;
    private final static ASTPrinter astPrinter = new ASTPrinter();

    private static ParserLoopWhile parser;

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

        parser = new ParserLoopWhile(
                tokenStream,
                new ParserExpression(tokenStream),
                new ParserBlock(tokenStream, new ParserPrincipal(tokenStream))
        );
    }

    @Test
    @DisplayName("Test - Declaración de while loop correcta")
    void testDeclaracionWhileLoopCorrecta() {
        final String code = "mientras (iterador_mientras < 10){ iterador_mientras = iterador_mientras + 1 }";
        context.process(code);
        tokens.addAll(context.getTokens());
        var ast = parser.parseStatement();
        assertThat(ast).isNotNull();
        assertThat(ast).matches(s -> s instanceof StatementLoopWhile);
        System.out.println(astPrinter.print(ast));
    }

    @Test
    @DisplayName("Test - Declaración de while loop incorrecta sin expresión")
    void testDeclaracionWhileLoopIncorrectaSinExpresion() {
        final String code = "mientras { iterador_mientras = iterador_mientras + 1 }";
        context.process(code);
        tokens.addAll(context.getTokens());
        var ex = assertThrows(ParserException.class, () -> parser.parseStatement());
        System.out.println(ex.getMessage());
    }

    @Test
    @DisplayName("Test - Declaración de while loop sin bloque")
    void testDeclaracionWhileLoopSinBloque() {
        final String code = "mientras (iterador_mientras < 10)";
        context.process(code);
        tokens.addAll(context.getTokens());
        var ex = assertThrows(ParserException.class, () -> parser.parseStatement());
        System.out.println(ex.getMessage());
    }

    @Test
    @DisplayName("Test - Declaración de while loop con paréntesis pero sin expresión")
    void testDeclaracionWhileLoopConParentesisPeroSinExpresion() {
        final String code = "mientras () { iterador_mientras = iterador_mientras + 1 }";
        context.process(code);
        tokens.addAll(context.getTokens());
        var ex = assertThrows(ParserException.class, () -> parser.parseStatement());
        System.out.println(ex.getMessage());
    }

    @Test
    @DisplayName("Test - Declaración de while loop con bloque pero sin declaración")
    void testDeclaracionWhileLoopConBloquePeroSinDeclaracion() {
        final String code = "mientras (iterador_mientras < 10) {}";
        context.process(code);
        tokens.addAll(context.getTokens());
        var ast = parser.parseStatement();
        assertThat(ast).isNotNull();
        assertThat(ast).matches(s -> s instanceof StatementLoopWhile);
        System.out.println(astPrinter.print(ast));
    }
}