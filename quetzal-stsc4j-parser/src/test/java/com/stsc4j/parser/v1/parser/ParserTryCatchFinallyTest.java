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

@DisplayName("ParserTryCatchFinally - Tests")
class ParserTryCatchFinallyTest {

    private static LexerContext context;
    private static List<Token> tokens;
    private static TokenStream tokenStream;
    private static final ASTPrinter astPrinter = new ASTPrinter();

    public static ParserTryCatchFinally parser;

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

        parser = new ParserTryCatchFinally(
                new ParserBlock(tokenStream, new ParserPrincipal(tokenStream)),
                new ParserExpressions(tokenStream),
                tokenStream
        );
    }

    @Test
    @DisplayName("Test - Try-Catch-Finally correcto")
    void testTryCatchFinallyCorrecto(){
        final String code = "intentar {\n" +
                "    entero a = 1\n" +
                "} capturar (excepcion error) {\n" +
                "    entero b = 1\n" +
                "} finalmente {\n" +
                "    entero c = 1\n" +
                "}";
        context.process(code);
        tokens.addAll(context.getTokens());
        var ast = parser.parseStatement();
        assertNotNull(ast);
        System.out.println(astPrinter.print(ast));
    }

    @Test
    @DisplayName("Test - Try-Catch sin Finally")
    void testTryCatchSinFinally(){
        final String code = "intentar {\n" +
                "    entero a = 1\n" +
                "} capturar (excepcion error) {\n" +
                "    entero b = 1\n" +
                "}";
        context.process(code);
        tokens.addAll(context.getTokens());
        var ast = parser.parseStatement();
        assertNotNull(ast);
        assertTrue(ast.toString().contains("StatementTryCatchFinally"));
        System.out.println(astPrinter.print(ast));
    }

    @Test
    @DisplayName("Test - Try-Catch-Finally con múltiples statements en Try")
    void testTryCatchFinallyMultipleStatementsInTry(){
        final String code = "intentar {\n" +
                "    entero a = 1\n" +
                "    entero b = 2\n" +
                "    entero c = a + b\n" +
                "} capturar (excepcion error) {\n" +
                "    entero x = 0\n" +
                "} finalmente {\n" +
                "    consola.mostrar(\"Finalizando\")\n" +
                "}";
        context.process(code);
        tokens.addAll(context.getTokens());
        var ast = parser.parseStatement();
        assertNotNull(ast);
        String printed = astPrinter.print(ast);
        assertTrue(printed.contains("Try-Catch-Finally Statement"));
        System.out.println(printed);
    }

    @Test
    @DisplayName("Test - Try-Catch-Finally con múltiples statements en Catch")
    void testTryCatchFinallyMultipleStatementsInCatch(){
        final String code = "intentar {\n" +
                "    entero a = 1\n" +
                "} capturar (excepcion error) {\n" +
                "    consola.mostrar(\"Error capturado\")\n" +
                "    entero resultado = 0\n" +
                "    retornar resultado\n" +
                "} finalmente {\n" +
                "    consola.mostrar(\"Fin\")\n" +
                "}";
        context.process(code);
        tokens.addAll(context.getTokens());
        var ast = parser.parseStatement();
        assertNotNull(ast);
        System.out.println(astPrinter.print(ast));
    }

    @Test
    @DisplayName("Test - Try-Catch-Finally con Finally vacío")
    void testTryCatchFinallyWithEmptyFinally(){
        final String code = "intentar {\n" +
                "    entero a = 1\n" +
                "} capturar (excepcion error) {\n" +
                "    entero b = 2\n" +
                "} finalmente {\n" +
                "}";
        context.process(code);
        tokens.addAll(context.getTokens());
        var ast = parser.parseStatement();
        assertNotNull(ast);
        System.out.println(astPrinter.print(ast));
    }

    @Test
    @DisplayName("Test - Try-Catch-Finally anidado")
    void testTryCatchFinallyNested(){
        final String code = "intentar {\n" +
                "    entero a = 1\n" +
                "    intentar {\n" +
                "        entero b = 2\n" +
                "    } capturar (excepcion e) {\n" +
                "        entero c = 3\n" +
                "    }\n" +
                "} capturar (excepcion error) {\n" +
                "    entero d = 4\n" +
                "} finalmente {\n" +
                "    consola.mostrar(\"Completado\")\n" +
                "}";
        context.process(code);
        tokens.addAll(context.getTokens());
        var ast = parser.parseStatement();
        assertNotNull(ast);
        String printed = astPrinter.print(ast);
        assertTrue(printed.contains("Try-Catch-Finally Statement"));
        System.out.println(printed);
    }

    @Test
    @DisplayName("Test - Try-Catch-Finally con expresiones complejas")
    void testTryCatchFinallyComplexExpressions(){
        final String code = "intentar {\n" +
                "    entero resultado = 10 + 5 * 2\n" +
                "    texto mensaje = \"Error\"\n" +
                "} capturar (excepcion error) {\n" +
                "    entero codigo = -1\n" +
                "} finalmente {\n" +
                "    entero limpieza = 0\n" +
                "}";
        context.process(code);
        tokens.addAll(context.getTokens());
        var ast = parser.parseStatement();
        assertNotNull(ast);
        System.out.println(astPrinter.print(ast));
    }

    @Test
    @DisplayName("Test - Try-Catch-Finally con condicional en Try")
    void testTryCatchFinallyWithConditionalInTry(){
        final String code = "intentar {\n" +
                "    entero a = 1\n" +
                "    si (a > 0) {\n" +
                "        consola.mostrar(\"Positivo\")\n" +
                "    }\n" +
                "} capturar (excepcion error) {\n" +
                "    consola.mostrar(\"Error\")\n" +
                "} finalmente {\n" +
                "    consola.mostrar(\"Fin\")\n" +
                "}";
        context.process(code);
        tokens.addAll(context.getTokens());
        var ast = parser.parseStatement();
        assertNotNull(ast);
        System.out.println(astPrinter.print(ast));
    }

    @Test
    @DisplayName("Test - Try-Catch-Finally con bucle en Catch")
    void testTryCatchFinallyWithLoopInCatch(){
        final String code = "intentar {\n" +
                "    entero a = 1\n" +
                "} capturar (excepcion error) {\n" +
                "    para (entero var i = 0; i < 5; i++) {\n" +
                "        consola.mostrar(i)\n" +
                "    }\n" +
                "} finalmente {\n" +
                "    consola.mostrar(\"Completado\")\n" +
                "}";
        context.process(code);
        tokens.addAll(context.getTokens());
        var ast = parser.parseStatement();
        assertNotNull(ast);
        System.out.println(astPrinter.print(ast));
    }

    @Test
    @DisplayName("Test - Try-Catch-Finally con lista en Try")
    void testTryCatchFinallyWithListInTry(){
        final String code = "intentar {\n" +
                "    lista<entero> numeros = [1, 2, 3, 4, 5]\n" +
                "} capturar (excepcion error) {\n" +
                "    consola.mostrar(\"Error al crear lista\")\n" +
                "} finalmente {\n" +
                "    consola.mostrar(\"Operación finalizada\")\n" +
                "}";
        context.process(code);
        tokens.addAll(context.getTokens());
        var ast = parser.parseStatement();
        assertNotNull(ast);
        System.out.println(astPrinter.print(ast));
    }

    @Test
    @DisplayName("Test - Try-Catch-Finally con llamada a método")
    void testTryCatchFinallyWithMethodCall(){
        final String code = "intentar {\n" +
                "    entero resultado = miObjeto.metodo(5, 10)\n" +
                "} capturar (excepcion error) {\n" +
                "    consola.mostrar(error)\n" +
                "} finalmente {\n" +
                "    limpiar()\n" +
                "}";
        context.process(code);
        tokens.addAll(context.getTokens());
        var ast = parser.parseStatement();
        assertNotNull(ast);
        System.out.println(astPrinter.print(ast));
    }

    @Test
    @DisplayName("Test - Verificar estructura AST de Try-Catch-Finally")
    void testVerifyASTStructure(){
        final String code = "intentar {\n" +
                "    entero a = 1\n" +
                "} capturar (excepcion miError) {\n" +
                "    entero b = 2\n" +
                "} finalmente {\n" +
                "    entero c = 3\n" +
                "}";
        context.process(code);
        tokens.addAll(context.getTokens());
        var ast = parser.parseStatement();

        assertNotNull(ast, "El AST no debe ser nulo");
        assertTrue(ast.getClass().getSimpleName().contains("TryCatchFinally"),
                "El AST debe ser de tipo StatementTryCatchFinally");

        String printed = astPrinter.print(ast);
        assertTrue(printed.contains("Try-Catch-Finally Statement"), "Debe contener 'Try-Catch-Finally Statement'");
        assertTrue(printed.contains("Try Block"), "Debe contener 'Try Block'");
        assertTrue(printed.contains("Catch Block"), "Debe contener 'Catch Block'");
        assertTrue(printed.contains("Finally Block"), "Debe contener 'Finally Block'");
        assertTrue(printed.contains("Exception Variable"), "Debe contener 'Exception Variable'");
        assertTrue(printed.contains("miError"), "Debe contener el nombre de la excepción");

        System.out.println(printed);
    }
}