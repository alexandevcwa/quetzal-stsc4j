package com.stsc4j.parser.v1.parser;

import com.stsc4j.lexer.LexerContext;
import com.stsc4j.lexer.Token;
import com.stsc4j.lexer.TokenType;
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
    private final static ASTPrinter astPrinter = new ASTPrinter();

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
        parser = new ParserDeclaration(tokenStream, new ParserExpressions(tokenStream));
    }

    @Test
    @DisplayName("Test - Declaración de variable con tipo de dato primitivo")
    void testDeclaracionVariable() {
        // Arrange: crear tokens manuales
        tokens.add(new Token(TokenType.PRIMITIVE_BOOLEAN, "log", 1));
        tokens.add(new Token(TokenType.IDENTIFIER, "a", 1));
        tokens.add(new Token(TokenType.EQUAL, "=", 1));
        tokens.add(new Token(TokenType.LIT_FALSE, "false", 1));
        tokens.add(new Token(TokenType.EOF, "", 1));

        var tokenStream = new TokenStream(tokens);
        parser = new ParserDeclaration(tokenStream, new ParserExpressions(tokenStream));

        // Act
        final Statement sta = parser.parseStatement();

        // Assert
        assertThat(sta)
                .matches(s -> s instanceof StatementVariable);
        tokens.clear();

    }

    @Test
    @DisplayName("Test - Variable con identificador primero que tipo de dato")
    void testVariableConIdentificadorPrimeroQueTipoDato() {
        tokens.add(new Token(TokenType.IDENTIFIER, "a", 1));
        tokens.add(new Token(TokenType.PRIMITIVE_INTEGER, "número", 1));
        tokens.add(new Token(TokenType.EQUAL, "=", 1));
        tokens.add(new Token(TokenType.LIT_INTEGER, "1", 1));
        tokens.add(new Token(TokenType.EOF, "", 1));
        var tokenStream = new TokenStream(tokens);
        parser = new ParserDeclaration(tokenStream, new ParserExpressions(tokenStream));
        assertThrows(ParserException.class, () -> parser.parseStatement());
        tokens.clear();
    }

    @Test
    @DisplayName("Test - Variable sin identificador")
    void testVariableSinIdentificador() {
        tokens.add(new Token(TokenType.PRIMITIVE_BOOLEAN, "log", 1));
        tokens.add(new Token(TokenType.EQUAL, "=", 1));
        tokens.add(new Token(TokenType.LIT_FALSE, "false", 1));
        tokens.add(new Token(TokenType.EOF, "", 1));
        var tokenStream = new TokenStream(tokens);
        parser = new ParserDeclaration(tokenStream, new ParserExpressions(tokenStream));
        assertThrows(ParserException.class, () -> parser.parseStatement());
        tokens.clear();
    }

    @Test
    @DisplayName("Test - Variable existente con nueva asignación")
    void testVariableExistenteConNuevaAsignacion() {
        tokens.add(new Token(TokenType.IDENTIFIER, "a", 1));
        tokens.add(new Token(TokenType.EQUAL, "=", 1));
        tokens.add(new Token(TokenType.LIT_INTEGER, "1", 1));
        tokens.add(new Token(TokenType.PLUS, "+", 1));
        tokens.add(new Token(TokenType.IDENTIFIER, "a", 1));
        tokens.add(new Token(TokenType.PLUS, "+", 1));
        tokens.add(new Token(TokenType.LIT_INTEGER, "2", 1));
        tokens.add(new Token(TokenType.EOF, "", 1));
        var tokenStream = new TokenStream(tokens);
        parser = new ParserDeclaration(tokenStream, new ParserExpressions(tokenStream));
        var sta = parser.parseStatement();
        assertThat(sta)
                .matches(s -> s instanceof StatementVariable);
        tokens.clear();
    }

    @Test
    @DisplayName("Test - Variable con con asignación de valor desde una matriz unidimensional")
    void testVariableDeclaracionConValorMatrixN1() {
        final String code = "entero valorMatriz = matriz_n1[1]";
        context.process(code);
        tokens.addAll(context.getTokens());
        var ast = parser.parseStatement();
        assertThat(ast).isNotNull();
        assertThat(ast).matches(s -> s instanceof StatementVariable);
        System.out.println(astPrinter.print(ast));
    }

    @Test
    @DisplayName("Test - Variable con con asignación de valor desde una matriz bidimensional")
    void testVariableDeclaracionConValorMatrixN2() {
        final String code = "entero valorMatriz = matriz_n2[1][2]";
        context.process(code);
        tokens.addAll(context.getTokens());
        var ast = parser.parseStatement();

        assertThat(ast).isNotNull();
        assertThat(ast).matches(s -> s instanceof StatementVariable);
        var formated = (StatementVariable) ast;
        assertThat(((ExpressionIndexAccess) formated.initialValue).indexList.size()).isEqualTo(2);
        System.out.println(astPrinter.print(ast));
    }
}