package com.stsc4j.parser.v1.parser;

import com.stsc4j.lexer.Token;
import com.stsc4j.lexer.TokenType;
import com.stsc4j.parser.v1.ast.Statement;
import com.stsc4j.parser.v1.ast.StatementVariable;
import com.stsc4j.parser.v1.exception.ParserException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ParseDeclaration - Tests")
class ParserDeclarationTest {

    private List<Token> tokens;
    private ParserDeclaration parser;

    @BeforeEach
    void setup() {
        tokens = new ArrayList<>();
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
    void testVariableConIdentificadorPrimeroQueTipoDato(){
        tokens.add(new Token(TokenType.IDENTIFIER, "a", 1));
        tokens.add(new Token(TokenType.PRIMITIVE_INTEGER,"número",1));
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
    void testVariableSinIdentificador(){
        tokens.add(new Token(TokenType.PRIMITIVE_BOOLEAN,"log",1));
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
    void testVariableExistenteConNuevaAsignacion(){
        tokens.add(new Token(TokenType.IDENTIFIER, "a", 1));
        tokens.add(new Token(TokenType.EQUAL, "=", 1));
        tokens.add(new Token(TokenType.LIT_INTEGER, "1", 1));
        tokens.add(new Token(TokenType.PLUS,"+",1));
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
}