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

@DisplayName("ParserMatrixAssignation - Tests")
class ParserMatrixAssignationTest {

    private static LexerContext context;
    private static List<Token> tokens;
    private static TokenStream tokenStream;
    private final static ASTPrinter astPrinter = new ASTPrinter();

    public static ParserMatrixAssignation parser;

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

        parser = new ParserMatrixAssignation(
                tokenStream,
                new ParserExpressions(tokenStream)
        );
    }

    @Test
    @DisplayName("Test - Asignar nuevo valor a matriz 2D correcto por literal")
    void testDecAsigMatrixNuevoValor() {
        final String code = "matriz[0][1] = 10";
        context.process(code);
        tokens.addAll(context.getTokens());
        var ast = parser.parseStatement();
        assertNotNull(ast);
        System.out.println(astPrinter.print(ast));
    }

    @Test
    @DisplayName("Test - Asignar nuevo valor a matriz 2D correcto por variable")
    void testDecAsigMatrixValorVariable() {
        final String code = "matriz[0][1] = valor";
        context.process(code);
        tokens.addAll(context.getTokens());
        var ast = parser.parseStatement();
        assertNotNull(ast);
        System.out.println(astPrinter.print(ast));
    }

    @Test
    @DisplayName("Test - Asignar valor nulo a matriz 2D")
    void testDecAsigMatrixValorNull() {
        final String code = "matriz[0][1] = nulo";
        context.process(code);
        tokens.addAll(context.getTokens());
        var ast = parser.parseStatement();
        assertNotNull(ast);
        System.out.println(astPrinter.print(ast));
    }

    @Test
    @DisplayName("Test - Asignar valor a matriz 2D sin valor")
    void testDecAsigMatrixSinValor() {
        final String code = "matriz[0][1] =";
        context.process(code);
        tokens.addAll(context.getTokens());
        var ex = assertThrows(ParserException.class, () -> parser.parseStatement());
        assertNotNull(ex);
        System.out.println(ex.getMessage());
    }

    @Test
    @DisplayName("Test - Asignar valor a matriz 2D sin indice")
    void testDecAsigMatrixSinIndice() {
        final String code = "matriz[1][] = 1";
        context.process(code);
        tokens.addAll(context.getTokens());
        var ex = assertThrows(ParserException.class, () -> parser.parseStatement());
        assertNotNull(ex);
        System.out.println(ex.getMessage());
    }

    @Test
    @DisplayName("Test - Asignar valor a matriz 2D sin indice 2")
    void testDecAsigMatrixSinIndice2() {
        final String code = "matriz[][] = 1";
        context.process(code);
        tokens.addAll(context.getTokens());
        var ex = assertThrows(ParserException.class, () -> parser.parseStatement());
        assertNotNull(ex);
        System.out.println(ex.getMessage());
    }

    @Test
    @DisplayName("Test - Asignar valor a matriz 2D sin cierre")
    void testDecAsigMatrixSinCierre() {
        final String code = "matriz[0][1 = 1";
        context.process(code);
        tokens.addAll(context.getTokens());
        var ex = assertThrows(ParserException.class, () -> parser.parseStatement());
        assertNotNull(ex);
        System.out.println(ex.getMessage());
    }

    @Test
    @DisplayName("Test - Asignar valor a matriz 2D sin referencia de variable")
    void testDecAsigMatrixSinReferenciaVariable() {
        final String code = "[0][1] = valor_variable";
        context.process(code);
        tokens.addAll(context.getTokens());
        var ex = assertThrows(ParserException.class, () -> parser.parseStatement());
        assertNotNull(ex);
        System.out.println(ex.getMessage());
    }

    @Test
    @DisplayName("Test - Asignar matriz 1D literal")
    void testDecAsigMatrix1DLiteral() {
        final String code = "matriz[0] = 1";
        context.process(code);
        tokens.addAll(context.getTokens());
        var ast = parser.parseStatement();
        assertNotNull(ast);
        System.out.println(astPrinter.print(ast));
    }

    @Test
    @DisplayName("Test - Asignar matriz 1D nulo")
    void testDecAsigMatrix1DNulo() {
        final String code = "matriz[0] = nulo";
        context.process(code);
        tokens.addAll(context.getTokens());
        var ast = parser.parseStatement();
        assertNotNull(ast);
        System.out.println(astPrinter.print(ast));
    }

    @Test
    @DisplayName("Test - Asignar matriz 1D variable")
    void testDecAsigMatrix1DVariable() {
        final String code = "matriz[0] = valor";
        context.process(code);
        tokens.addAll(context.getTokens());
        var ast = parser.parseStatement();
        assertNotNull(ast);
        System.out.println(astPrinter.print(ast));
    }

    @Test
    @DisplayName("Test - Asignar matriz 1D indice no numérico o variable")
    void testDecAsigMatrix1DIndiceNoNumerico(){
        final String code = "matriz[1.1] = valor";
        context.process(code);
        tokens.addAll(context.getTokens());
        var ex = assertThrows(ParserException.class, () -> parser.parseStatement());
        assertNotNull(ex);
        System.out.println(ex.getMessage());
    }

    @Test
    @DisplayName("Test - Asignar matriz 1D indice no numérico o variable 2")
    void testDecAsigMatrix1DIndiceNoNumerico2(){
        final String code = "matriz[falso] = valor";
        context.process(code);
        tokens.addAll(context.getTokens());
        var ex = assertThrows(ParserException.class, () -> parser.parseStatement());
        assertNotNull(ex);
        System.out.println(ex.getMessage());
    }

    @Test
    @DisplayName("Test - Asignar matriz 1D indice no numérico o variable 3")
    void testDecAsigMatrix1DIndiceNoNumerico3(){
        final String code = "matriz[1,2] = valor";
        context.process(code);
        tokens.addAll(context.getTokens());
        var ex = assertThrows(ParserException.class, () -> parser.parseStatement());
        assertNotNull(ex);
        System.out.println(ex.getMessage());
    }

    @Test
    @DisplayName("Test - Asignar matriz 1D indice no numérico o variable 4")
    void testDecAsigMatrix1DIndiceNoNumerico4(){
        final String code = "matriz[\"a\"] = valor";
        context.process(code);
        tokens.addAll(context.getTokens());
        var ex = assertThrows(ParserException.class, () -> parser.parseStatement());
        assertNotNull(ex);
        System.out.println(ex.getMessage());
    }
}