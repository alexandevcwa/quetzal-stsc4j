package com.stsc4j.parser.v1.parser;

import com.stsc4j.lexer.LexerContext;
import com.stsc4j.lexer.Token;
import com.stsc4j.parser.v1.ast.StatementLoopDoWhile;
import com.stsc4j.parser.v1.exception.ParserException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ParserLoopDoWhile - Tests")
class ParserLoopDoWhileTest {
    private static LexerContext context;
    private static TokenStream tokenStream;
    public static ParserLoopDoWhile parser;

    @AfterEach
    void cleanTokens() {
        tokenStream.clear();
        context.cleanToken();
    }

    @BeforeAll
    static void setup() {
        context = new LexerContext();
        List<Token> tokens = context.getTokens();
        tokenStream = new TokenStream(tokens);

        parser = new ParserLoopDoWhile(
                tokenStream,
                new ParserExpression(tokenStream),
                new ParserBlock(tokenStream, new ParserPrincipal(tokenStream))
        );
    }

    @Test
    @DisplayName("Test - Declaración de do while loop correcta")
    void testDeclaracionDoWhileLoopCorrecta() {
        final String code = "hacer {\n" +
                "    texto atemporal = \"Hola mundo\" + interador_hacer \n" +
                "    iterador_hacer = interador_hacer + 1\n" +
                "} mientras (iterador_hacer < 3)";
        context.process(code);
        var ast = parser.parseStatement();
        assertNotNull(ast);
        assertThat(ast).matches(s -> s instanceof StatementLoopDoWhile);
    }

    @Test
    @DisplayName("Test - Declaración de do while loop incorrecta sin expresión")
    void testDeclaracionDoWhileLoopIncorrectaSinExpresion() {
        final String code = "hacer { texto_temporal = \"Hola mundo\" + iterador_hacer } mientras";
        context.process(code);
        assertThrows(ParserException.class, () -> parser.parseStatement());
    }

    @Test
    @DisplayName("Test - Declaración de do while loop con doble paréntesis pero sin expresión")
    void testDeclaracionDoWhileLoopConDobleParentesisPeroSinExpresion() {
        final String code = "hacer () { texto_temporal = \"Hola mundo\" + iterador_hacer } mientras (1>2)";
        context.process(code);
        assertThrows(ParserException.class, () -> parser.parseStatement());
    }

    @Test
    @DisplayName("Test - Declaración de do while loop con paréntesis pero sin expresión")
    void testDeclaracionDoWhileLoopConParentesisPeroSinExpresion() {
        final String code = "hacer { texto_temporal = \"Hola mundo\" + iterador_hacer } mientras ()";
        context.process(code);
        assertThrows(ParserException.class, () -> parser.parseStatement());
    }

    @Test
    @DisplayName("Test - Declaración de do while loop sin bloque")
    void testDeclaracionDoWhileLoopSinBloque() {
        final String code = "hacer (iterador_hacer < 10)";
        context.process(code);
        assertThrows(ParserException.class, () -> parser.parseStatement());
    }

    @Test
    @DisplayName("Test - Declaración de do while loop sin declaración")
    void testDeclaracionDoWhileLoopSinDo(){
        final String code = "{ a = 1 } mientras (iterador_mientras < 10)";
        context.process(code);
        assertThrows(ParserException.class, () -> parser.parseStatement());
    }

    @Test
    @DisplayName("Test - Declaración de do while loop sin while y expresión")
    void testDeclaracionDoWhileLoopSinWhile(){
        final String code = "hacer { a = 1 }";
        context.process(code);
        assertThrows(ParserException.class, () -> parser.parseStatement());
    }
}