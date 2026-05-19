package bytecode;

import com.stsc4j.generator.BytecodeGenerator;
import com.stsc4j.lexer.LexerContext;
import com.stsc4j.lexer.Token;
import com.stsc4j.parser.v1.parser.ParserPrincipal;
import com.stsc4j.parser.v1.parser.TokenStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("BytecodeGeneratorTest - Pruebas de Generación y Ejecución en la JVM")
class BytecodeGeneratorTest {

    private static LexerContext context;
    private static List<Token> tokens;
    private static TokenStream tokenStream;
    private static ParserPrincipal parser;

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
    @DisplayName("Éxito: Declarar e imprimir una variable entera")
    void testGenerarYEjecutarEntero() throws Exception {
        final String code =
                "entero var a = 42\n" +
                        "imprimir(a)\n";

        // Ejecutamos y esperamos que la consola de Java imprima "42"
        String salida = compilarYEjecutar(code, "TestEntero");
        assertEquals("42", salida.trim());
    }

    @Test
    @DisplayName("Éxito: Declarar e imprimir una variable decimal")
    void testGenerarYEjecutarDecimal() throws Exception {
        // Recuerda que en tu parser 'numero' es el decimal
        final String code =
                "numero var b = 15.5\n" +
                        "imprimir(b)\n";

        String salida = compilarYEjecutar(code, "TestDecimal");
        assertEquals("15.5", salida.trim());
    }

    @Test
    @DisplayName("Éxito: Coerción automática (Guardar un entero en un decimal)")
    void testGenerarYEjecutarCoercion() throws Exception {
        final String code =
                "numero var c = 100\n" + // 100 es entero, pero se guarda en 'numero'
                        "consola.mostrar(c)\n";

        // Al imprimirlo, la JVM debería mostrarlo como flotante (100.0) gracias al I2F
        String salida = compilarYEjecutar(code, "TestCoercion");
        assertEquals("100.0", salida.trim());
    }

    @Test
    @DisplayName("Éxito: Operación Binaria con mezcla de tipos")
    void testGenerarYEjecutarOperacionMixta() throws Exception {
        // Sumamos un entero (10) y un decimal (5.5)
        final String code =
                "numero var resultado = 10 + 5.5\n" +
                        "imprimir(resultado)\n";

        String salida = compilarYEjecutar(code, "TestOperacionMixta");
        assertEquals("15.5", salida.trim());
    }

    @Test
    @DisplayName("Éxito: Control de Flujo (If-Else) con bloques")
    void testGenerarYEjecutarIfElse() throws Exception {
        final String code =
                "log var condicion = verdadero\n" +
                        "si (condicion) {\n" +
                        "    imprimir(\"Entró al bloque TRUE\")\n" +
                        "} sino {\n" +
                        "    imprimir(\"Entró al bloque FALSE\")\n" +
                        "}\n";

        String salida = compilarYEjecutar(code, "TestIfElse");

        // Como 'condicion' es verdadero, solo debe imprimir el bloque true
        assertEquals("Entró al bloque TRUE", salida.trim());
    }

    @Test
    @DisplayName("Éxito: Ciclo Do-While con Incremento")
    void testGenerarYEjecutarDoWhile() throws Exception {
        final String code =
                "entero var contador = 1\n" +
                        "hacer {\n" +
                        "    imprimir(contador)\n" +
                        "    contador++\n" +
                        "} mientras (contador < 4)\n";

        String salida = compilarYEjecutar(code, "TestDoWhile");

        // Imprime primero 1, luego 2, luego 3
        String salidaEsperada = "1" + System.lineSeparator() +
                "2" + System.lineSeparator() +
                "3" + System.lineSeparator();

        assertEquals(salidaEsperada.trim(), salida.trim());
    }

    @Test
    @DisplayName("Éxito: Ciclo Mientras (While) Clásico")
    void testGenerarYEjecutarWhile() throws Exception {
        final String code =
                "entero var contador = 1\n" +
                        "mientras (contador < 4) {\n" +
                        "    imprimir(contador)\n" +
                        "    contador++\n" +
                        "}\n";

        String salida = compilarYEjecutar(code, "TestWhile");

        // Esperamos que imprima 1, 2, 3 al igual que el do-while
        String salidaEsperada = "1" + System.lineSeparator() +
                "2" + System.lineSeparator() +
                "3" + System.lineSeparator();

        assertEquals(salidaEsperada.trim(), salida.trim());
    }

    @Test
    @DisplayName("Éxito: Definición, Llamada y Retorno de Función")
    void testGenerarYEjecutarFuncion() throws Exception {
        final String code =
                "entero duplicar(entero x) {\n" +
                        "    retornar x * 2\n" +
                        "}\n" +
                        "entero resultado = duplicar(5)\n" +
                        "imprimir(resultado)\n";

        // Pasamos el código a nuestro orquestador para que genere TestFuncion.class
        String salida = compilarYEjecutar(code, "TestFuncion");

        // Esperamos que 5 * 2 sea exactamente 10
        String salidaEsperada = "10" + System.lineSeparator();

        assertEquals(salidaEsperada.trim(), salida.trim());
    }

    @Test
    @DisplayName("Éxito: Crear arreglo, acceder a índice y operar")
    void testGenerarYEjecutarArreglo() throws Exception {
        // CORREGIDO: Sintaxis oficial de Quetzal
        final String code =
                "lista<entero> numeros = [10, 20, 30]\n" +
                        "entero resultado = numeros[1] + 10\n" +
                        "imprimir(resultado)\n";

        String salida = compilarYEjecutar(code, "TestArreglo");

        // El índice 1 tiene el número 20. Al sumarle 10, debe imprimir 30.
        assertEquals("30" + System.lineSeparator(), salida);
    }

    @Test
    @DisplayName("Éxito: Ciclo Para-Cada (ForEach)")
    void testGenerarYEjecutarForEach() throws Exception {
        // CORREGIDO: Usando la sintaxis oficial de Quetzal "para (tipo var nombre en lista)"
        final String code =
                "lista<entero> numeros = [1, 2, 3]\n" +
                        "para (entero var n en numeros) {\n" +
                        "    imprimir(n)\n" +
                        "}\n";

        String salida = compilarYEjecutar(code, "TestForEach");

        // Esperamos que imprima 1, 2 y 3 en líneas separadas
        assertEquals("1" + System.lineSeparator() + "2" + System.lineSeparator() + "3" + System.lineSeparator(), salida);
    }


    // ==========================================
    // MOTOR DE EJECUCIÓN DINÁMICA (MAGIA OSCURA)
    // ==========================================

    /**
     * Este compila el código Quetzal a un .class, intercepta la consola de Java,
     * carga la clase generada, la ejecuta y devuelve lo que imprimió.
     */
    private String compilarYEjecutar(String code, String className) throws Exception {
        // 1. Lexer y Parser
        context.process(code);
        tokens.addAll(context.getTokens());
        var ast = parser.parse(); // Devuelve una lista de Statements

        // 2. Generación de Bytecode (.class)
        BytecodeGenerator generator = new BytecodeGenerator();
        // Asumiendo que parser.parse() devuelve una Lista. Si devuelve un Statement único, mételo en un List.of(ast)
        generator.compile(ast, className);

        // 3. Redirigir System.out para atrapar lo que imprima tu programa
        PrintStream originalOut = System.out;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        System.setOut(new PrintStream(bos));

        try {
            // 4. Cargar el .class generado desde la carpeta actual
            File file = new File(""); // Directorio raíz
            URL url = file.toURI().toURL();
            URLClassLoader classLoader = new URLClassLoader(new URL[]{url});
            Class<?> claseGenerada = classLoader.loadClass(className);

            // 5. Buscar el "public static void main(String[] args)"
            Method mainMethod = claseGenerada.getMethod("main", String[].class);

            // 6. ¡EJECUTAR EL BYTECODE!
            String[] params = null;
            mainMethod.invoke(null, (Object) params);

        } finally {
            // 7. Restaurar la consola a la normalidad
            System.setOut(originalOut);

            // Opcional: Borrar el archivo .class después de la prueba para no ensuciar tu proyecto
            //new File(className + ".class").delete();
        }

        // Devolvemos lo que el programa escribió en la consola
        return bos.toString();
    }
}