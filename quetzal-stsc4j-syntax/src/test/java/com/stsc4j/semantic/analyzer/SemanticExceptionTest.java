package com.stsc4j.semantic.analyzer;

import com.stsc4j.lexer.LexerContext;
import com.stsc4j.lexer.Token;
import com.stsc4j.parser.v1.parser.ParserPrincipal;
import com.stsc4j.parser.v1.parser.TokenStream;
import com.stsc4j.semantic.SemanticAnalyzer;
import com.stsc4j.semantic.SemanticError;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("SemanticExceptionTest - Manejo de Errores")
class SemanticExceptionTest {

    private static LexerContext context;
    private static List<Token> tokens;
    private static TokenStream tokenStream;
    private static ParserPrincipal parser;
    private SemanticAnalyzer semanticAnalyzer;

    @AfterEach
    void clean(){
        tokenStream.clear();
        context.cleanToken();
        tokens.clear();
    }

    @BeforeAll
    static void setup(){
        context = new LexerContext();
        tokens = new ArrayList<>();
        tokenStream = new TokenStream(tokens);
        parser = new ParserPrincipal(tokenStream);
    }

    @Test
    @DisplayName("Éxito: Bloque completo y alcance (scope) correcto de la excepción")
    void testTryCatchValido() {
        semanticAnalyzer = new SemanticAnalyzer();
        final String code =
                "intentar {\n" +
                        "    lanzar \"Hubo un problema grave\"\n" +
                        "} capturar (excepcion mi_error) {\n" +
                        "    // Si 'mi_error' existe en el scope, esto no debe lanzar error semántico\n" +
                        "    consola.mostrar(mi_error)\n" +
                        "} finalmente {\n" +
                        "    entero var limpieza = 100\n" +
                        "}\n";

        context.process(code);
        tokens.addAll(context.getTokens());
        var ast = parser.parse();

        assertDoesNotThrow(() -> semanticAnalyzer.analyze(ast));
    }

    @Test
    @DisplayName("Error: La variable de excepción no debe existir fuera del catch")
    void testErrorScopeExcepcion() {
        semanticAnalyzer = new SemanticAnalyzer();
        final String code =
                "intentar {\n" +
                        "    entero var a = 1\n" +
                        "} capturar (excepcion e) {\n" +
                        "    consola.mostrar(e)\n" +
                        "} finalmente {\n" +
                        "    // ESTO DEBE FALLAR SEMÁNTICAMENTE: 'e' ya no existe aquí\n" +
                        "    consola.mostrar(e)\n" +
                        "}\n";

        context.process(code);
        tokens.addAll(context.getTokens());
        var ast = parser.parse();

        var ex = assertThrows(SemanticError.class, () -> semanticAnalyzer.analyze(ast));
        assertTrue(ex.getMessage().contains("no ha sido definida"), "El compilador debe detectar que la variable 'e' ya murió.");
    }

    @Test
    @DisplayName("Test Maestro - Documentación Oficial: Try/Catch, Funciones y Propiedades de Excepción")
    void testTryCatchDocumentacionOficial() {
        semanticAnalyzer = new SemanticAnalyzer();

        final String code =
                "// Definimos una función que puede lanzar una excepción\n" +
                        "número dividir(número numerador, número denominador){\n" +
                        "    si (denominador == 0) {\n" +
                        "        lanzar \"Error: División por cero no permitida\"\n" +
                        "    }\n" +
                        "    retornar numerador / denominador\n" +
                        "}\n" +
                        "\n" +
                        "// Tratar la excepción al llamar a la función\n" +
                        "intentar {\n" +
                        "    número var resultado = dividir(10, 0)\n" +
                        "    consola.mostrar(resultado)\n" +
                        "} capturar (excepcion e){\n" +
                        "    // Obtenemos el mensaje de la excepción\n" +
                        "    consola.mostrar(e.mensaje)\n" +
                        "    // Mostramos la pila de llamadas directa\n" +
                        "    consola.mostrar(e.llamadas)\n" +
                        "}\n" +
                        "finalmente {\n" +
                        "    consola.mostrar(\"Fin del manejo de excepciones.\")\n" +
                        "}\n";

        context.process(code);
        tokens.addAll(context.getTokens());
        var ast = parser.parse();

        assertDoesNotThrow(() -> semanticAnalyzer.analyze(ast));
    }

}