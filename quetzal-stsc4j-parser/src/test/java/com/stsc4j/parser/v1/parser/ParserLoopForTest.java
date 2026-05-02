package com.stsc4j.parser.v1.parser;

import com.stsc4j.lexer.LexerContext;
import com.stsc4j.lexer.Token;
import com.stsc4j.parser.v1.ast.ASTPrinter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ParserLoopFor - Tests")
class ParserLoopForTest {

    private static LexerContext context;
    private static List<Token> tokens;
    private static TokenStream tokenStream;
    private final static ASTPrinter astPrinter = new ASTPrinter();

    public static ParserLoopFor parser;

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

        var parserExpression = new ParserExpressions(tokenStream);
        parser = new ParserLoopFor(
                tokenStream,
                new ParserDeclaration(tokenStream, parserExpression),
                parserExpression,
                new ParserBlock(tokenStream, new ParserPrincipal(tokenStream))
        );
    }

    //TODO: no pasa test con declaracion en bloque a--
    @Test
    @DisplayName("Test - Declaración de for loop correcta")
    void testLoopForCorrecto() {
        final String code = "para (entero var i = 0; i < 5; i++) {\n" +
                " entero b = 1+i" +
                " a-- " +
                "}";
        context.process(code);
        tokens.addAll(context.getTokens());
        var ast = parser.parseStatement();
        System.out.println(astPrinter.print(ast));
    }
}