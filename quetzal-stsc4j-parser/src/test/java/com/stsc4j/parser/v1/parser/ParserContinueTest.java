package com.stsc4j.parser.v1.parser;

import com.stsc4j.lexer.LexerContext;
import com.stsc4j.lexer.Token;
import com.stsc4j.parser.v1.ast.ASTPrinter;
import com.stsc4j.parser.v1.ast.StatementContinue;
import com.stsc4j.parser.v1.exception.ParserException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ParserContinue - Tests")
class ParserContinueTest {

    private static LexerContext context;
    private static TokenStream tokenStream;
    private static ParserContinue parser;
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
        parser = new ParserContinue(tokenStream);
    }

    @Test
    @DisplayName("Test - Declaración de continue correcta")
    void testDeclaracionContinueCorrecta() {
        final String code = "continuar";
        context.process(code);
        var ast = parser.parseStatement();

        assertNotNull(ast);
        assertThat(ast).isInstanceOf(StatementContinue.class);
        System.out.println(astPrinter.print(ast));
    }

    @Test
    @DisplayName("Test - Declaración de continue con espacios")
    void testDeclaracionContinueConEspacios() {
        final String code = "   continuar   ";
        context.process(code);
        var ast = parser.parseStatement();

        assertNotNull(ast);
        assertThat(ast).isInstanceOf(StatementContinue.class);
        System.out.println(astPrinter.print(ast));
    }

    @Test
    @DisplayName("Test - Error cuando falta el token continue")
    void testErrorCuandoFaltaContinue() {
        final String code = "romper";
        context.process(code);
        var ex = assertThrows(ParserException.class, () -> parser.parseStatement());
        assertThat(ex.getMessage()).contains("Se esperaba 'continuar'");
        System.out.println(ex.getMessage());
    }

    @Test
    @DisplayName("Test - Verificar que el token del continue se almacena correctamente")
    void testVerificarTokenContinue() {
        final String code = "continuar";
        context.process(code);
        var ast = parser.parseStatement();

        StatementContinue statementContinue = (StatementContinue) ast;
        assertNotNull(statementContinue.token);
        assertThat(statementContinue.token.getLexeme()).isEqualTo("continuar");
        System.out.println("Token lexeme: " + statementContinue.token.getLexeme());
    }
}