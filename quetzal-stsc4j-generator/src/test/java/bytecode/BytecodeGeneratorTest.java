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

    @Test
    @DisplayName("Éxito: Mutar una lista (Asignación en índice)")
    void testGenerarYEjecutarMutacionLista() throws Exception {
        final String code =
                "lista<entero> numeros = [10, 20, 30]\n" +
                        "numeros[1] = 99\n" + // Cambiamos el 20 por un 99
                        "imprimir(numeros[1])\n";

        String salida = compilarYEjecutar(code, "TestMutacion");

        // Esperamos que la consola imprima 99
        assertEquals("99" + System.lineSeparator(), salida);
    }

    @Test
    @DisplayName("Éxito: Crear y guardar un objeto JSN")
    void testGenerarYEjecutarJSN() throws Exception {
        // CORREGIDO: Sintaxis oficial de Quetzal usando Identificadores en lugar de Textos
        final String code =
                "jsn var usuario = {\n" +
                        "    nombre: \"Nilver\",\n" +
                        "    edad: 25\n" +
                        "}\n" +
                        "imprimir(usuario)\n";

        String salida = compilarYEjecutar(code, "TestJSN");

        // El LinkedHashMap de Java se imprime así por defecto
        String salidaEsperada = "{nombre=Nilver, edad=25}" + System.lineSeparator();

        assertEquals(salidaEsperada, salida);
    }

    @Test
    @DisplayName("Éxito: Leer y mutar propiedades de un objeto JSN")
    void testGenerarYEjecutarPropiedadesJSN() throws Exception {
        final String code =
                "jsn var persona = {\n" +
                        "    nombre: \"Nilver\",\n" +
                        "    rol: \"Estudiante\"\n" +
                        "}\n" +
                        "persona.rol = \"IT Director\"\n" + // Mutamos la propiedad
                        "imprimir(persona.rol)\n" +         // Leemos la propiedad mutada
                        "imprimir(persona.nombre)\n";       // Leemos la propiedad intacta

        String salida = compilarYEjecutar(code, "TestPropiedadesJSN");

        // Debería imprimir el nuevo rol y luego el nombre
        String salidaEsperada = "IT Director" + System.lineSeparator() +
                "Nilver" + System.lineSeparator();

        assertEquals(salidaEsperada, salida);
    }

    @Test
    @DisplayName("Éxito: Especificación Parcial de JSN (Pendiente Parser)")
    void testEspecificacionCompletaJSN() throws Exception {
        final String code =
                "jsn var persona = {\n" +
                        "    nombre: \"Ana\",\n" +
                        "    edad: 28\n" +
                        "}\n" +
                        // 1. Probar contiene_clave (Funciona porque no es palabra reservada)
                        "log tieneNombre = persona.contiene_clave(\"nombre\")\n" +
                        "imprimir(tieneNombre)\n" +

                        // 2. Probar acceso dinámico por corchete (Con el truco de la variable)
                        "texto claveEdad = \"edad\"\n" +
                        "imprimir(persona[claveEdad])\n" +

                        // 3. Probar establecer() método nativo
                        "persona.establecer(\"activo\", verdadero)\n" +

                        // Confirmamos que el paso 3 funcionó usando el truco del paso 2
                        "texto claveActivo = \"activo\"\n" +
                        "imprimir(persona[claveActivo])\n";

        String salida = compilarYEjecutar(code, "TestEspecificacionJSN");

        String n = System.lineSeparator();
        // Esperamos: true (tiene_clave), 28 (edad), true (activo recién agregado)
        String salidaEsperada = "true" + n +
                "28" + n +
                "true" + n;

        assertEquals(salidaEsperada, salida);
    }

    @Test
    @DisplayName("Éxito: MEGA TEST - Variables, Operaciones, Listas, If, Loops y Consola")
    void testMegaCompleto() throws Exception {
        // 1. EL TRUCO: Simulamos que el usuario teclea "2026" y presiona Enter
        String entradaSimulada = "2026\n";
        System.setIn(new java.io.ByteArrayInputStream(entradaSimulada.getBytes()));

        // 2. EL CÓDIGO QUETZAL COMPLETO
        final String code =
                "texto saludo = \"¡Bienvenido al Mega Test de Quetzal!\"\n" +
                        "imprimir(saludo)\n" +

                        "imprimir(\"Ingrese el año actual:\")\n" +
                        // Ojo: ajusta "consola.leer_numero()" al comando exacto de tu lenguaje
                        "entero anio = consola.leer_numero()\n" +

                        "entero edad = anio - 1998\n" +
                        "imprimir(\"Tu edad calculada es:\")\n" +
                        "imprimir(edad)\n" +

                        "imprimir(\"Procesando lista de puntajes...\")\n" +
                        "lista<entero> puntajes = [85, 90, 100]\n" +
                        "puntajes[0] = puntajes[0] + 5\n" + // Modificamos el primer elemento (90)

                        "para (entero var p en puntajes) {\n" +
                        "    si (p >= 95) {\n" +
                        "        imprimir(\"Puntaje de Excelencia:\")\n" +
                        "        imprimir(p)\n" +
                        "    } sino {\n" +
                        "        imprimir(\"Puntaje Normal:\")\n" +
                        "        imprimir(p)\n" +
                        "    }\n" +
                        "}\n";

        // 3. EJECUCIÓN
        String salida = compilarYEjecutar(code, "TestMegaCompleto");

        // 4. RESTAURAR EL TECLADO ORIGINAL (Muy importante para no romper otros tests)
        System.setIn(System.in);

        // 5. VALIDACIÓN EXACTA
        String n = System.lineSeparator();
        String salidaEsperada = "¡Bienvenido al Mega Test de Quetzal!" + n +
                "Ingrese el año actual:" + n +
                "Tu edad calculada es:" + n +
                "28" + n +
                "Procesando lista de puntajes..." + n +
                "Puntaje Normal:" + n +
                "90" + n +         // El 85 que mutó a 90
                "Puntaje Normal:" + n +
                "90" + n +         // El 90 original
                "Puntaje de Excelencia:" + n +
                "100" + n;         // El 100 original

        assertEquals(salidaEsperada, salida);
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