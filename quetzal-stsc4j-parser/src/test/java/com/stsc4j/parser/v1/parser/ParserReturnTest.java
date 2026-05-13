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

@DisplayName("ParserReturn - Tests")
public class ParserReturnTest {

	private static LexerContext context;
    private static List<Token> tokens;
    private static TokenStream tokenStream;
    private final static ASTPrinter astPrinter = new ASTPrinter();

	private static ParserReturn parser;

	@AfterEach
    void clean(){
        tokenStream.clear();
        context.cleanToken();
    }

    @BeforeAll
    static void setup(){
        context = new LexerContext();
        tokens = new ArrayList<>();
        tokenStream = new TokenStream(tokens);

        parser = new ParserReturn(
                new ParserExpressions(tokenStream),
                tokenStream
        );
    }

	@Test
	@DisplayName("Test - Retorna variable multiplica valor funcion")
	void testReturnConVariableYValorFuncion(){
		final String code = "retornar n * factorial(n -1)";
		context.process(code);
		tokens.addAll(context.getTokens());
		var ast = assertDoesNotThrow(() -> parser.parseStatement());
		assertNotNull(ast);
	}

	@Test
    @DisplayName("Test - Retornar variable por valor retorno función con multiples parámetros")
    void testReturnConVariablePorValorRetornoFuncionMultiplesParametros(){
        final String code = "retornar n * factorial(n - 1, 1, 2, 3, 54)";
        context.process(code);
        tokens.addAll(context.getTokens());
        var ast = parser.parseStatement();
        assertNotNull(ast);
        System.out.println(astPrinter.print(ast));
    }

    @Test
    @DisplayName("Test - Retornar variable por valor retorno función con funciones anidadas")
    void testReturnConRetornoDeFuncionConFuncionesAnidadas(){
        final String code = "retornar factorial(n - 1) * factorial(n - 2)";
        context.process(code);
        tokens.addAll(context.getTokens());
        var ast = parser.parseStatement();
        assertNotNull(ast);
        System.out.println(astPrinter.print(ast));
    }

    @Test
    @DisplayName("Test - Retornar variable por valor retorno función con funciones anidadas como parámetros")
    void testReturnConFuncionesConOtrasFuncionesComoParametros(){
        final String code = "retornar formatoFecha(obtenerDia(),obtenerMes(),obtenerAno())";
        context.process(code);
        tokens.addAll(context.getTokens());
        var ast = parser.parseStatement();
        assertNotNull(ast);
        System.out.println(astPrinter.print(ast));
    }
}
