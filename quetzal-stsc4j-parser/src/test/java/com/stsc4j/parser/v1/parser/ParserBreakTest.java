package com.stsc4j.parser.v1.parser;

import com.stsc4j.lexer.LexerContext;
import com.stsc4j.lexer.Token;
import com.stsc4j.parser.v1.ast.ASTPrinter;
import com.stsc4j.parser.v1.ast.StatementBreak;
import com.stsc4j.parser.v1.exception.ParserException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ParserBreak - Tests")
class ParserBreakTest {

    private static LexerContext context;
    private static TokenStream tokenStream;
    private static ParserBreak parser;
    private final static ASTPrinter astPrinter = new ASTPrinter();

    @AfterEach
    void clean() {
        tokenStream.clear();
        context.cleanToken();
    }

    @BeforeAll
    static void setup() {
        context = new LexerContext();
        List<Token> tokens = context.getTokens();
        tokenStream = new TokenStream(tokens);
        parser = new ParserBreak(tokenStream);
    }

    @Test
    @DisplayName("Test - Declaración de break correcta")
    void testDeclaracionBreakCorrecta() {
        final String code = "romper";
        context.process(code);
        var ast = parser.parseStatement();

        assertNotNull(ast);
        assertThat(ast).isInstanceOf(StatementBreak.class);
        System.out.println(astPrinter.print(ast));
    }

    @Test
    @DisplayName("Test - Declaración de break con espacios")
    void testDeclaracionBreakConEspacios() {
        final String code = "   romper   ";
        context.process(code);
        var ast = parser.parseStatement();

        assertNotNull(ast);
        assertThat(ast).isInstanceOf(StatementBreak.class);
        System.out.println(astPrinter.print(ast));
    }

    @Test
    @DisplayName("Test - Error cuando falta el token break")
    void testErrorCuandoFaltaBreak() {
        final String code = "continuar";
        context.process(code);
        var ex = assertThrows(ParserException.class, () -> parser.parseStatement());
        assertThat(ex.getMessage()).contains("Se esperaba 'romper'");
        System.out.println(ex.getMessage());
    }

    @Test
    @DisplayName("Test - Verificar que el token del break se almacena correctamente")
    void testVerificarTokenBreak() {
        final String code = "romper";
        context.process(code);
        var ast = parser.parseStatement();

        StatementBreak statementBreak = (StatementBreak) ast;
        assertNotNull(statementBreak.token);
        assertThat(statementBreak.token.getLexeme()).isEqualTo("romper");
        System.out.println("Token lexeme: " + statementBreak.token.getLexeme());
    }
}

