package com.stsc4j.parser.v1.parser;

import com.stsc4j.lexer.LexerContext;
import com.stsc4j.lexer.Token;
import com.stsc4j.parser.v1.ast.ASTPrinter;
import com.stsc4j.parser.v1.ast.ExpressionIndexAccess;
import com.stsc4j.parser.v1.ast.Statement;
import com.stsc4j.parser.v1.ast.StatementVariable;
import com.stsc4j.parser.v1.exception.ParserException;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ParseDeclaration - Tests")
class ParserDeclarationTest {

    private static LexerContext context;
    private static List<Token> tokens;
    private static TokenStream tokenStream;
    private static ParserDeclaration parser;

    @AfterEach
    void cleanTokens() {
        tokenStream.clear();
        context.cleanToken();
        tokens.clear();
    }

    @BeforeAll
    static void staticSetup() {
        context = new LexerContext();
        tokens = new ArrayList<>();
        tokenStream = new TokenStream(tokens);
        parser = new ParserDeclaration(tokenStream, new ParserExpression(tokenStream));
    }

    @Test
    @DisplayName("Test - Declaración de variable con tipo de dato primitivo")
    void testDeclaracionVariable() {
        // Arrange: crear tokens manuales
        final String code = "log a = false";
        context.process(code);
        tokens.addAll(context.getTokens());
        // Act
        final Statement sta = assertDoesNotThrow(() -> parser.parseStatement());

        // Assert
        assertThat(sta)
                .matches(s -> s instanceof StatementVariable);

    }

    @Test
    @DisplayName("Test - Variable con identificador primero que tipo de dato")
    void testVariableConIdentificadorPrimeroQueTipoDato() {
        final String code = "a número = 1";
        context.process(code);
        tokens.addAll(context.getTokens());
        assertThrows(ParserException.class, () -> parser.parseStatement());
    }

    @Test
    @DisplayName("Test - Variable sin identificador")
    void testVariableSinIdentificador() {
        final String coder = "log = false";
        context.process(coder);
        tokens.addAll(context.getTokens());
        assertThrows(ParserException.class, () -> parser.parseStatement());
    }

    @Test
    @DisplayName("Test - Variable existente con nueva asignación")
    void testVariableExistenteConNuevaAsignacion() {
        final String code = "entero a = 1 + a + 2";
        context.process(code);
        tokens.addAll(context.getTokens());
        var ast = assertDoesNotThrow(() -> parser.parseStatement());
        assertThat(ast)
                .matches(s -> s instanceof StatementVariable);
    }

    @Test
    @DisplayName("Test - Variable con con asignación de valor desde una matriz unidimensional")
    void testVariableDeclaracionConValorMatrixN1() {
        final String code = "entero valorMatriz = matriz_n1[1]";
        context.process(code);
        tokens.addAll(context.getTokens());
        var ast = assertDoesNotThrow(() -> parser.parseStatement());
        assertThat(ast).isNotNull();
        assertThat(ast).matches(s -> s instanceof StatementVariable);
    }

    @Test
    @DisplayName("Test - Variable con con asignación de valor desde una matriz bidimensional")
    void testVariableDeclaracionConValorMatrixN2() {
        final String code = "entero valorMatriz = matriz_n2[1][2]";
        context.process(code);
        tokens.forEach(System.out::println);
        tokens.addAll(context.getTokens());
        var ast = assertDoesNotThrow(() -> parser.parseStatement());

        assertThat(ast).isNotNull();
        assertThat(ast).matches(s -> s instanceof StatementVariable);
        var formated = (StatementVariable) ast;
        assertThat(((ExpressionIndexAccess) formated.initialValue).indexList.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("Test - Variable con asignación de valor negativo")
    void testVariableNumerosNegativos() {
        final String code = "entero valor = -1";
        context.process(code);
        tokens.addAll(context.getTokens());
        var ast = assertDoesNotThrow(() -> parser.parseStatement());
        assertThat(ast).isNotNull();
        assertThat(ast).matches(s -> s instanceof StatementVariable);
    }

    @Test
    @DisplayName("Test - Variable con asignación de valor negativo con decimal")
    void testVariableNumerosNegativos2() {
        final String code = "entero valor = -1.5";
        context.process(code);
        tokens.addAll(context.getTokens());
        var ast = assertDoesNotThrow(() -> parser.parseStatement());
        assertThat(ast).isNotNull();
        assertThat(ast).matches(s -> s instanceof StatementVariable);
    }

    @Test
    @DisplayName("Test - Variable con asignación de valor negativo con operación")
    void testVariableNumerosNegativos3() {
        final String code = "entero valor = - 1 - (-1)";
        context.process(code);
        tokens.addAll(context.getTokens());
        var ast = assertDoesNotThrow(() -> parser.parseStatement());
        assertThat(ast).isNotNull();
        assertThat(ast).matches(s -> s instanceof StatementVariable);
    }

    @Test
    @DisplayName("Test - Variable con asignación de valor inicial por consola")
    void testVariableObtenerValorPorConsola() {
        final String code = "entero valor = consola.pedir(\"Ingrese un valor: \")";
        context.process(code);
        tokens.addAll(context.getTokens());
        var ast = assertDoesNotThrow(() -> parser.parseStatement());
        assertThat(ast).isNotNull();
        assertThat(ast).matches(s -> s instanceof StatementVariable);
    }

    @Test
    @DisplayName("Test - Variable con asignación de valor booleano con operadores lógicos")
    void testVariableBooleano() {
        final String code = "log acceso = (usuario y !usuario)";
        context.process(code);
        tokens.addAll(context.getTokens());
        var ast = assertDoesNotThrow(() -> parser.parseStatement());
        assertThat(ast).isNotNull();
        assertThat(ast).matches(s -> s instanceof StatementVariable);
    }

    @Test
    @DisplayName("Test - Variable con asignación de valor booleano con operadores lógicos y comparación")
    void testVariableBooleano2() {
        final String code = "log disponible = (stock > 0) o (pedido_en_camino == verdadero)";
        context.process(code);
        tokens.addAll(context.getTokens());
        var ast = assertDoesNotThrow(() -> parser.parseStatement());
        assertThat(ast).isNotNull();
        assertThat(ast).matches(s -> s instanceof StatementVariable);
    }
}