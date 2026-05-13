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

@DisplayName("ParserExpressions - Tests")
class ParserExpressionsTest {

    private static LexerContext context;
    private static List<Token> tokens;
    private static TokenStream tokenStream;

    public static ParserExpressions parser;

    @AfterEach
    void cleanTokens() {
        tokenStream.clear();
        context.cleanToken();
    }

    @BeforeAll
    static void setup(){
        context = new LexerContext();
        tokens = new ArrayList<>();
        tokenStream = new TokenStream(tokens);
        parser = new ParserExpressions(tokenStream);
    }

    @Test
    @DisplayName("Test - Expresión increments ++")
    void testExpresionIncremental(){
        final String code = "a++";
        context.process(code);
        tokens.addAll(context.getTokens());
        var ast = parser.parseExpression();
    }

    @Test
    @DisplayName("Test - Expresión decrements --")
    void testExpressionDecremental(){
        final String code = "a--";
        context.process(code);
        tokens.addAll(context.getTokens());
        var ast = parser.parseExpression();
    }

    @Test
    @DisplayName("Test - Expresión con mezcla de operadores incrementales y decrementales")
    void testExpressionMixOperadores(){
        final String code = "a+-";
        context.process(code);
        tokens.addAll(context.getTokens());
        var ex = assertThrows(ParserException.class, () -> parser.parseExpression());
        assertNotNull(ex);
    }

    @Test
    @DisplayName("Test - Expresión con mezcla de operadores incrementales y multiplicativos")
    void testExpressionMixMasYPor(){
        final String code = "a+*";
        context.process(code);
        tokens.addAll(context.getTokens());
        var ex = assertThrows(ParserException.class, () -> parser.parseExpression());
        assertNotNull(ex);
    }

    @Test
    @DisplayName("Test - Expresión con mezcla de operadores incrementales y división")
    void testExpressionMixMasYDiv(){
        final String code = "a+/";
        context.process(code);
        tokens.addAll(context.getTokens());
        var ex = assertThrows(ParserException.class, () -> parser.parseExpression());
        assertNotNull(ex);
    }
}