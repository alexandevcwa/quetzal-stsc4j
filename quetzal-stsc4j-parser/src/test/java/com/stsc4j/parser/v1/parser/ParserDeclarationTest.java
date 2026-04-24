package com.stsc4j.parser.v1.parser;

import com.stsc4j.lexer.Token;
import com.stsc4j.lexer.TokenType;
import com.stsc4j.parser.v1.ast.Statement;
import com.stsc4j.parser.v1.ast.StatementVariable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ParseDeclaration - Test")
class ParserDeclarationTest {

    private List<Token> tokens;
    private ParserDeclaration parser;

    @BeforeEach
    void setup() {
        tokens = new ArrayList<>();
    }

    @Test
    @DisplayName("Simple Var Declaration")
    void testSimpleVarDeclaration() {
        // Arrange: crear tokens manuales
        tokens.add(new Token(TokenType.PRIMITIVE_BOOLEAN, "log", 1));
        tokens.add(new Token(TokenType.IDENTIFIER, "a", 1));
        tokens.add(new Token(TokenType.EQUAL, "=", 1));
        tokens.add(new Token(TokenType.LIT_FALSE, "false", 1));
        tokens.add(new Token(TokenType.EOF, "", 1));

        var tokenStream = new TokenStream(tokens);
        parser = new ParserDeclaration(tokenStream, new ParserExpressions(tokenStream));

        // Act
        final Statement sta = parser.parseVarDeclaration();

        // Assert
        assertThat(sta)
                .matches(s -> s instanceof StatementVariable);

    }

}