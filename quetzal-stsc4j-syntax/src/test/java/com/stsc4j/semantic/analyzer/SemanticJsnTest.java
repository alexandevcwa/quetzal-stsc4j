package com.stsc4j.semantic.analyzer;

import com.stsc4j.lexer.LexerContext;
import com.stsc4j.lexer.Token;
import com.stsc4j.parser.v1.parser.ParserPrincipal;
import com.stsc4j.parser.v1.parser.TokenStream;
import com.stsc4j.semantic.SemanticAnalyzer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("SemanticJsnTest - Validación de Objetos JSON")
class SemanticJsnTest {

    private static LexerContext context;
    private static List<Token> tokens;
    private static TokenStream tokenStream;
    private static ParserPrincipal parser;
    private final SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer();

    @AfterEach
    void clean() {
        tokenStream.clear();
        context.cleanToken();
        tokens.clear();
    }

    @BeforeAll
    static void setup() {
        context = new LexerContext();
        tokens = new ArrayList<>();
        tokenStream = new TokenStream(tokens);
        parser = new ParserPrincipal(tokenStream);
    }

    @Test
    @DisplayName("Prueba 1: Declaración JSN Básica (Constante)")
    void testJsnBasicoConstante() {
        final String code =
                "jsn persona = {\n" +
                        "    nombre: \"Ana\",\n" +
                        "    edad: 28,\n" +
                        "    activo: verdadero\n" +
                        "}\n";

        ejecutar(code);
    }

    @Test
    @DisplayName("Prueba 2: Claves Alternativas (Sin comillas para al Parser actual)")
    void testJsnClavesEspeciales() {
        final String code =
                "jsn datos = {\n" +
                        "    nombre: \"valor normal\",\n" +
                        "    clave_con_espacios: \"permitido\",\n" +
                        "    clave_numerica: 456,\n" +
                        "    clave_con_guiones: verdadero\n" +
                        "}\n";

        ejecutar(code);
    }

    @Test
    @DisplayName("Prueba 3: JSN con Listas de Objetos Anidados")
    void testJsnConListas() {
        final String code =
                "jsn var tienda = {\n" +
                        "    nombre: \"Mi Tienda\",\n" +
                        "    productos: [\n" +
                        "        { id: 1, nombre: \"Laptop\", precio: 999.99 },\n" +
                        "        { id: 2, nombre: \"Mouse\", precio: 29.99 }\n" +
                        "    ]\n" +
                        "}\n";

        ejecutar(code);
    }

    @Test
    @DisplayName("Prueba 4: El Objeto Completo de la Documentación Oficial")
    void testJsnComplejoAnidado() {
        final String code =
                "jsn var persona = {\n" +
                        "    nombre: \"Ana\",\n" +
                        "    datos_personales: {\n" +
                        "        fecha_nacimiento: \"1998-05-15\",\n" +
                        "        dpi: \"1234567890101\",\n" +
                        "        peso: 65.5,\n" +
                        "        altura: 1.70,\n" +
                        "        genero: \"Femenino\",\n" +
                        "        nacionalidad: \"Guatemalteca\"\n" +
                        "    },\n" +
                        "    direcciones: [\n" +
                        "        { tipo: \"casa\", direccion: \"123 Calle Principal\" },\n" +
                        "        { tipo: \"trabajo\", direccion: \"456 Avenida Secundaria\" }\n" +
                        "    ],\n" +
                        "    telefonos: [\"555-1234\", \"555-5678\"],\n" +
                        "    activo: verdadero\n" +
                        "}\n";

        ejecutar(code);
    }

    @Test
    @DisplayName("Caso de Uso 1: Modificación exitosa de propiedad usando establecer")
    void testModificarPropiedadJsnMutable() {
        final String code =
                "jsn var persona = { nombre: \"Ana\", edad: 28 }\n" +
                        "persona.establecer(\"nombre\", \"Maria\")\n";

        ejecutar(code);
    }

    @Test
    @DisplayName("Caso de Uso 2: Error Semántico al intentar modificar un JSON constante (sin var)")
    void testErrorModificarJsnConstante() {
        // 'persona' es constante (no tiene 'var').
        // Usamos .establecer() para evadir el bug del Parser y probar directamente la semántica.
        final String code =
                "jsn persona = { nombre: \"Ana\", edad: 28 }\n" +
                        "persona.establecer(\"nombre\", \"Maria\")\n";

        context.process(code);
        tokens.addAll(context.getTokens());
        var ast = parser.parse();

        assertThrows(com.stsc4j.semantic.SemanticError.class, () -> semanticAnalyzer.analyze(ast));
    }

    @Test
    @DisplayName("Caso de Uso 2: Cambio de tipo dinámico en propiedad (Entero a Texto)")
    void testCambioTipoPropiedadJsn() {
        final String code =
                "jsn var persona = { nombre: \"Ana\", edad: 28 }\n" +
                        "persona.establecer(\"edad\", \"veintiocho\")\n";

        ejecutar(code);
    }

    @Test
    @DisplayName("Caso de Uso 4: Error Semántico al usar un método que no existe en JSN")
    void testErrorMetodoInexistenteJsn() {
        final String code =
                "jsn var persona = { nombre: \"Ana\", edad: 28 }\n" +
                        "persona.volar()\n";

        context.process(code);
        tokens.addAll(context.getTokens());
        var ast = parser.parse();

        assertThrows(com.stsc4j.semantic.SemanticError.class, () -> semanticAnalyzer.analyze(ast));
    }


    @Test
    @DisplayName("Caso de Uso: Modificación directa de propiedad JSN mutable (con var)")
    void testAsignacionDirectaJsnMutable() {
        final String code =
                "jsn var persona = { nombre: \"Ana\", edad: 28 }\n" +
                        "persona.nombre = \"Maria\"\n";

        ejecutar(code);
    }

    @Test
    @DisplayName("Caso de Uso: Error al hacer asignación directa a JSN constante")
    void testErrorAsignacionDirectaJsnConstante() {
        final String code =
                "jsn persona = { nombre: \"Ana\", edad: 28 }\n" +
                        "persona.nombre = \"Maria\"\n"; // Esto debería lanzar SemanticError

        context.process(code);
        tokens.addAll(context.getTokens());
        var ast = parser.parse();

        // Esperamos que bloquee por inmutabilidad
        assertThrows(com.stsc4j.semantic.SemanticError.class, () -> semanticAnalyzer.analyze(ast));
    }

    // auxiliar para reducir duplicación de código
    private void ejecutar(String code) {
        context.process(code);
        tokens.addAll(context.getTokens());
        var ast = parser.parse();
        assertDoesNotThrow(() -> semanticAnalyzer.analyze(ast));
    }


}