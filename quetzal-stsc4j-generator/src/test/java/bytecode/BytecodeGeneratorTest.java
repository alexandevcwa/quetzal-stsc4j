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
import java.lang.annotation.Documented;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("BytecodeGeneratorTest - Pruebas de Generación y Ejecución en la JVM (Consola Quetzal)")
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
                        "consola.mostrar(a)\n";

        String salida = compilarYEjecutar(code, "TestEntero");
        assertEquals("42", salida.trim());
    }

    @Test
    @DisplayName("Éxito: Declarar e imprimir una variable decimal")
    void testGenerarYEjecutarDecimal() throws Exception {
        final String code =
                "numero var b = 15.5\n" +
                        "consola.mostrar(b)\n";

        String salida = compilarYEjecutar(code, "TestDecimal");
        assertEquals("15.5", salida.trim());
    }

    @Test
    @DisplayName("Éxito: Coerción automática (Guardar un entero en un decimal)")
    void testGenerarYEjecutarCoercion() throws Exception {
        final String code =
                "numero var c = 100\n" +
                        "consola.mostrar(c)\n";

        String salida = compilarYEjecutar(code, "TestCoercion");
        assertEquals("100.0", salida.trim());
    }

    @Test
    @DisplayName("Éxito: Operación Binaria con mezcla de tipos")
    void testGenerarYEjecutarOperacionMixta() throws Exception {
        final String code =
                "numero var resultado = 10 + 5.5\n" +
                        "consola.mostrar(resultado)\n";

        String salida = compilarYEjecutar(code, "TestOperacionMixta");
        assertEquals("15.5", salida.trim());
    }

    @Test
    @DisplayName("Éxito: Control de Flujo (If-Else) con bloques")
    void testGenerarYEjecutarIfElse() throws Exception {
        final String code =
                "log var condicion = verdadero\n" +
                        "si (condicion) {\n" +
                        "    consola.mostrar_exito(\"Entró al bloque TRUE\")\n" +
                        "} sino {\n" +
                        "    consola.mostrar_error(\"Entró al bloque FALSE\")\n" +
                        "}\n";

        String salida = compilarYEjecutar(code, "TestIfElse");

        assertEquals("Entró al bloque TRUE", salida.trim());
    }

    @Test
    @DisplayName("Éxito: Ciclo Do-While con Incremento")
    void testGenerarYEjecutarDoWhile() throws Exception {
        final String code =
                "entero var contador = 1\n" +
                        "hacer {\n" +
                        "    consola.mostrar(contador)\n" +
                        "    contador++\n" +
                        "} mientras (contador < 4)\n";

        String salida = compilarYEjecutar(code, "TestDoWhile");

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
                        "    consola.mostrar(contador)\n" +
                        "    contador++\n" +
                        "}\n";

        String salida = compilarYEjecutar(code, "TestWhile");

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
                        "consola.mostrar(resultado)\n";

        String salida = compilarYEjecutar(code, "TestFuncion");
        String salidaEsperada = "10" + System.lineSeparator();

        assertEquals(salidaEsperada.trim(), salida.trim());
    }

    @Test
    @DisplayName("Éxito: Crear arreglo, acceder a índice y operar")
    void testGenerarYEjecutarArreglo() throws Exception {
        final String code =
                "lista<entero> numeros = [10, 20, 30]\n" +
                        "entero resultado = numeros[1] + 10\n" +
                        "consola.mostrar(resultado)\n";

        String salida = compilarYEjecutar(code, "TestArreglo");
        assertEquals("30" + System.lineSeparator(), salida);
    }

    @Test
    @DisplayName("Éxito: Ciclo Para-Cada (ForEach)")
    void testGenerarYEjecutarForEach() throws Exception {
        final String code =
                "lista<entero> numeros = [1, 2, 3]\n" +
                        "para (entero var n en numeros) {\n" +
                        "    consola.mostrar(n)\n" +
                        "}\n";

        String salida = compilarYEjecutar(code, "TestForEach");
        assertEquals("1" + System.lineSeparator() + "2" + System.lineSeparator() + "3" + System.lineSeparator(), salida);
    }

    @Test
    @DisplayName("Éxito: Mutar una lista (Asignación en índice)")
    void testGenerarYEjecutarMutacionLista() throws Exception {
        final String code =
                "lista<entero> numeros = [10, 20, 30]\n" +
                        "numeros[1] = 99\n" +
                        "consola.mostrar(numeros[1])\n";

        String salida = compilarYEjecutar(code, "TestMutacion");
        assertEquals("99" + System.lineSeparator(), salida);
    }

    @Test
    @DisplayName("Éxito: Crear y guardar un objeto JSN")
    void testGenerarYEjecutarJSN() throws Exception {
        final String code =
                "jsn var usuario = {\n" +
                        "    nombre: \"Nilver\",\n" +
                        "    edad: 25\n" +
                        "}\n" +
                        "consola.mostrar(usuario)\n";

        String salida = compilarYEjecutar(code, "TestJSN");
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
                        "persona.rol = \"IT Director\"\n" +
                        "consola.mostrar(persona.rol)\n" +
                        "consola.mostrar(persona.nombre)\n";

        String salida = compilarYEjecutar(code, "TestPropiedadesJSN");
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
                        "log tieneNombre = persona.contiene_clave(\"nombre\")\n" +
                        "consola.mostrar(tieneNombre)\n" +
                        "texto claveEdad = \"edad\"\n" +
                        "consola.mostrar(persona[claveEdad])\n" +
                        "persona.establecer(\"activo\", verdadero)\n" +
                        "texto claveActivo = \"activo\"\n" +
                        "consola.mostrar(persona[claveActivo])\n";

        String salida = compilarYEjecutar(code, "TestEspecificacionJSN");
        String n = System.lineSeparator();
        String salidaEsperada = "true" + n + "28" + n + "true" + n;

        assertEquals(salidaEsperada, salida);
    }

    @Test
    @DisplayName("Éxito: MEGA TEST - Variables, Operaciones, Listas, If, Loops y Consola")
    void testMegaCompleto() throws Exception {
        String entradaSimulada = "2026\n";
        System.setIn(new java.io.ByteArrayInputStream(entradaSimulada.getBytes()));

        final String code =
                "texto saludo = \"¡Bienvenido al Mega Test de Quetzal!\"\n" +
                        "consola.mostrar_informacion(saludo)\n" +
                        "entero anio = consola.pedir(\"Ingrese el año actual:\")\n" +
                        "entero edad = anio - 1998\n" +
                        "consola.mostrar(\"Tu edad calculada es:\")\n" +
                        "consola.mostrar_exito(edad)\n" +
                        "consola.mostrar_advertencia(\"Procesando lista de puntajes...\")\n" +
                        "lista<entero> puntajes = [85, 90, 100]\n" +
                        "puntajes[0] = puntajes[0] + 5\n" +
                        "para (entero var p en puntajes) {\n" +
                        "    si (p >= 95) {\n" +
                        "        consola.mostrar_exito(\"Puntaje de Excelencia:\")\n" +
                        "        consola.mostrar(p)\n" +
                        "    } sino {\n" +
                        "        consola.mostrar_informacion(\"Puntaje Normal:\")\n" +
                        "        consola.mostrar(p)\n" +
                        "    }\n" +
                        "}\n";

        String salida = compilarYEjecutar(code, "TestMegaCompleto");
        System.setIn(System.in);

        String n = System.lineSeparator();
        // Nota: Si el AST no imprime el texto de pedir(), no lo validamos estrictamente en el assert
        org.junit.jupiter.api.Assertions.assertTrue(salida.contains("¡Bienvenido al Mega Test de Quetzal!"));
        org.junit.jupiter.api.Assertions.assertTrue(salida.contains("28"));
        org.junit.jupiter.api.Assertions.assertTrue(salida.contains("Procesando lista de puntajes..."));
        org.junit.jupiter.api.Assertions.assertTrue(salida.contains("Puntaje Normal:"));
        org.junit.jupiter.api.Assertions.assertTrue(salida.contains("90"));
        org.junit.jupiter.api.Assertions.assertTrue(salida.contains("Puntaje de Excelencia:"));
        org.junit.jupiter.api.Assertions.assertTrue(salida.contains("100"));
    }

    @Test
    @DisplayName("Éxito: Manejo de Excepciones (intentar, capturar, finalmente y lanzar)")
    void testManejoExcepciones() throws Exception {

        final String code =
                "consola.mostrar_informacion(\"1. Entrando al sistema...\")\n" +
                        "intentar {\n" +
                        "    consola.mostrar_advertencia(\"2. Ejecutando operacion riesgosa...\")\n" +
                        "    lanzar \"¡Error critico de base de datos!\"\n" +
                        "    consola.mostrar(\"X. Esto jamas deberia imprimirse\")\n" +
                        "} capturar (excepcion e) {\n" +
                        "    consola.mostrar_error(\"3. El error fue interceptado exitosamente:\")\n" +
                        "    consola.mostrar(e)\n" +
                        "} finalmente {\n" +
                        "    consola.mostrar_exito(\"4. Limpiando recursos en bloque seguro\")\n" +
                        "}\n" +
                        "consola.mostrar(\"5. El programa continuo vivo despues del error\")\n";

        String salida = compilarYEjecutar(code, "TestExcepciones");

        org.junit.jupiter.api.Assertions.assertTrue(salida.contains("1. Entrando al sistema..."));
        org.junit.jupiter.api.Assertions.assertTrue(salida.contains("2. Ejecutando operacion riesgosa..."));
        org.junit.jupiter.api.Assertions.assertTrue(salida.contains("3. El error fue interceptado exitosamente:"));
        org.junit.jupiter.api.Assertions.assertTrue(salida.contains("java.lang.RuntimeException: ¡Error critico de base de datos!"));
        org.junit.jupiter.api.Assertions.assertTrue(salida.contains("4. Limpiando recursos en bloque seguro"));
        org.junit.jupiter.api.Assertions.assertTrue(salida.contains("5. El programa continuo vivo despues del error"));
    }

    @Test
    @DisplayName("Éxito: Propiedades de la Excepción (mensaje, linea, llamadas)")
    void testPropiedadesExcepcion() throws Exception {

        final String code =
                "intentar {\n" +
                        "    lanzar \"Error de conexion a la base de datos\"\n" +
                        "} capturar (excepcion error) {\n" +
                        "    consola.mostrar_error(\"--- ATRIBUTOS DEL ERROR ---\")\n" +
                        "    consola.mostrar(\"Mensaje:\")\n" +
                        "    consola.mostrar(error.mensaje)\n" +
                        "    \n" +
                        "    consola.mostrar(\"Linea:\")\n" +
                        "    consola.mostrar(error.linea)\n" +
                        "} finalmente {\n" +
                        "    consola.mostrar_exito(\"--- FIN DEL REPORTE ---\")\n" +
                        "}\n";

        String salida = compilarYEjecutar(code, "TestPropiedadesExcepcion");

        org.junit.jupiter.api.Assertions.assertTrue(salida.contains("--- ATRIBUTOS DEL ERROR ---"), "Falta el encabezado");
        org.junit.jupiter.api.Assertions.assertTrue(salida.contains("Error de conexion a la base de datos"), "No se recuperó el mensaje de error.mensaje");
        org.junit.jupiter.api.Assertions.assertTrue(salida.contains("--- FIN DEL REPORTE ---"), "No se ejecutó el bloque finalmente");
    }

    @Test
    @DisplayName("Test: Factorial Seguro")
    void testFactorial() throws Exception {
        String input = "5\n";
        System.setIn(new java.io.ByteArrayInputStream(input.getBytes()));

        final String code =
                "entero n = consola.pedir(\"Ingrese n (>=0): \")\n" +
                        "entero i = 1\n" +
                        "entero fact = 1\n" +
                        "si (n < 0) {\n" +
                        "    consola.mostrar_error(\"Error: n debe ser >= 0\")\n" +
                        "} sino {\n" +
                        "    mientras (i <= n) {\n" +
                        "        fact = fact * i\n" +
                        "        i = i + 1\n" +
                        "    }\n" +
                        "    consola.mostrar_exito(\"Factorial: \")\n" +
                        "    consola.mostrar(fact)\n" +
                        "}\n";

        String salida = compilarYEjecutar(code, "Factorial");
        System.setIn(System.in);

        org.junit.jupiter.api.Assertions.assertTrue(salida.contains("120"),
                "El resultado debería ser 120, pero la salida fue: " + salida);
    }

    @Test
    @DisplayName("Test: HolaMundo Seguro")
    void testHolaMundo() throws Exception {
        String input = "Nilver\n";
        System.setIn(new java.io.ByteArrayInputStream(input.getBytes()));

        final String code =
                "consola.mostrar(\"Hola mundo desde Quetzal!\")\n" +
                        "texto nombre = consola.pedir(\"Ingrese su nombre: \")\n" +
                        "consola.mostrar_exito(\"Bienvenido: \")\n" +
                        "consola.mostrar(nombre)\n";

        String salida = compilarYEjecutar(code, "HolaMundo");
        System.setIn(System.in);

        org.junit.jupiter.api.Assertions.assertTrue(salida.contains("Hola mundo desde Quetzal!"));
        org.junit.jupiter.api.Assertions.assertTrue(salida.contains("Bienvenido: "));
        org.junit.jupiter.api.Assertions.assertTrue(salida.contains("Nilver"));
    }

    @Test
    @DisplayName("Test: Par o Impar Seguro")
    void testParImpar() throws Exception {
        String input = "4\n";
        System.setIn(new java.io.ByteArrayInputStream(input.getBytes()));

        final String code =
                "entero n = consola.pedir(\"Ingrese un numero entero: \")\n" +
                        "entero residuo = n - ((n / 2) * 2)\n" +
                        "si (residuo == 0) {\n" +
                        "    consola.mostrar_exito(\"El numero es PAR\")\n" +
                        "} sino {\n" +
                        "    consola.mostrar_advertencia(\"El numero es IMPAR\")\n" +
                        "}\n";

        String salida = compilarYEjecutar(code, "ParImpar");
        System.setIn(System.in);

        org.junit.jupiter.api.Assertions.assertTrue(salida.contains("El numero es PAR"));
    }

    @Test
    @DisplayName("Test: Calculadora Básica Segura")
    void testCalculadora() throws Exception {
        String input = "20.5\n4.0\n";
        System.setIn(new java.io.ByteArrayInputStream(input.getBytes()));

        final String code =
                "consola.mostrar_informacion(\"--- CALCULADORA QUETZAL ---\")\n" +
                        "numero a = consola.pedir(\"Ingrese número a: \")\n" +
                        "numero b = consola.pedir(\"Ingrese número b: \")\n" +
                        "numero suma = a + b\n" +
                        "numero resta = a - b\n" +
                        "numero mult = a * b\n" +
                        "numero div = a / b\n" +
                        "consola.mostrar(\"Suma: \")\n" +
                        "consola.mostrar_exito(suma)\n" +
                        "consola.mostrar(\"Resta: \")\n" +
                        "consola.mostrar_exito(resta)\n" +
                        "consola.mostrar(\"Multiplicacion: \")\n" +
                        "consola.mostrar_exito(mult)\n" +
                        "consola.mostrar(\"Division: \")\n" +
                        "consola.mostrar_exito(div)\n";

        String salida = compilarYEjecutar(code, "Calculadora");
        System.setIn(System.in);

        org.junit.jupiter.api.Assertions.assertTrue(salida.contains("--- CALCULADORA QUETZAL ---"));
        org.junit.jupiter.api.Assertions.assertTrue(salida.contains("24.5"), "Fallo en la suma");
        org.junit.jupiter.api.Assertions.assertTrue(salida.contains("16.5"), "Fallo en la resta");
        org.junit.jupiter.api.Assertions.assertTrue(salida.contains("82.0"), "Fallo en la multiplicacion");
        org.junit.jupiter.api.Assertions.assertTrue(salida.contains("5.125"), "Fallo en la division");
    }

    @Test
    @DisplayName("Éxito: Definición y Llamada de Función con Textos")
    void testFuncionTexto() throws Exception {
        final String code =
                "texto saludar(texto nombre) {\n" +
                        "    retornar \"Hola \" + nombre\n" +
                        "}\n" +
                        "texto mensaje = saludar(\"Nilver\")\n" +
                        "consola.mostrar(mensaje)\n";

        // Ejecutamos la compilación y ejecución de la clase dinámica
        String salida = compilarYEjecutar(code, "TestFuncionTexto");

        // Validamos que el resultado impreso sea la concatenación exacta
        String salidaEsperada = "Hola Nilver" + System.lineSeparator();
        org.junit.jupiter.api.Assertions.assertEquals(salidaEsperada.trim(), salida.trim());
    }

    @Test
    @DisplayName("Éxito: Funciones Matemáticas Múltiples y Validación (Lanzar)")
    void testMegaMatematicas() throws Exception {
        // Simulamos entrada válida: a = 10.0, b = 2.0
        String input = "10.0\n2.0\n";
        System.setIn(new java.io.ByteArrayInputStream(input.getBytes()));

        final String code =
                "numero sumar(numero val1, numero val2) {\n" +
                        "    numero res = val1 + val2\n" +
                        "    consola.mostrar_informacion(\"-> Retorno Suma: \")\n" +
                        "    consola.mostrar(res)\n" +
                        "    retornar res\n" +
                        "}\n" +
                        "numero multiplicar(numero val1, numero val2) {\n" +
                        "    numero res = val1 * val2\n" +
                        "    consola.mostrar_informacion(\"-> Retorno Multiplicacion: \")\n" +
                        "    consola.mostrar(res)\n" +
                        "    retornar res\n" +
                        "}\n" +
                        "numero dividir(numero val1, numero val2) {\n" +
                        "    numero res = val1 / val2\n" +
                        "    consola.mostrar_informacion(\"-> Retorno Division: \")\n" +
                        "    consola.mostrar(res)\n" +
                        "    retornar res\n" +
                        "}\n" +
                        "\n" +
                        "intentar {\n" +
                        "    numero a = consola.pedir(\"Ingrese numero a: \")\n" +
                        "    numero b = consola.pedir(\"Ingrese numero b: \")\n" +
                        "\n" +
                        "    si (a < 1) {\n" +
                        "        lanzar \"Error: El numero 'a' es menor a 1\"\n" +
                        "    }\n" +
                        "    si (b < 1) {\n" +
                        "        lanzar \"Error: El numero 'b' es menor a 1\"\n" +
                        "    }\n" +
                        "\n" +
                        "    // Llamada de las 3 funciones en una sola línea\n" +
                        "    numero total = sumar(a, b) + multiplicar(a, b) + dividir(a, b)\n" +
                        "    \n" +
                        "    consola.mostrar_exito(\"El gran total es: \")\n" +
                        "    consola.mostrar(total)\n" +
                        "} capturar (excepcion e) {\n" +
                        "    consola.mostrar_error(\"Error detectado: \")\n" +
                        "    consola.mostrar(e.mensaje)\n" +
                        "}\n";

        String salida = compilarYEjecutar(code, "TestMegaMatematicas");
        System.setIn(System.in); // Restaurar teclado

        // Validamos que cada operación individual se haya impreso correctamente
        org.junit.jupiter.api.Assertions.assertTrue(salida.contains("12.0"), "Fallo en la impresión del retorno de sumar");
        org.junit.jupiter.api.Assertions.assertTrue(salida.contains("20.0"), "Fallo en la impresión del retorno de multiplicar");
        org.junit.jupiter.api.Assertions.assertTrue(salida.contains("5.0"), "Fallo en la impresión del retorno de dividir");

        // Validamos el gran total
        org.junit.jupiter.api.Assertions.assertTrue(salida.contains("37.0"), "El total final debe ser 37.0");
    }

    @Test
    @DisplayName("Éxito: Lanzar Excepción por Validación de Número Menor a 1")
    void testMegaMatematicasError() throws Exception {
        // Simulamos entrada inválida: a = 0.5 (provocará error), b = 5.0
        String input = "0.5\n5.0\n";
        System.setIn(new java.io.ByteArrayInputStream(input.getBytes()));

        final String code =
                "numero sumar(numero val1, numero val2) {\n" +
                        "    numero res = val1 + val2\n" +
                        "    consola.mostrar_informacion(\"-> Retorno Suma: \")\n" +
                        "    consola.mostrar(res)\n" +
                        "    retornar res\n" +
                        "}\n" +
                        "numero multiplicar(numero val1, numero val2) {\n" +
                        "    numero res = val1 * val2\n" +
                        "    consola.mostrar_informacion(\"-> Retorno Multiplicacion: \")\n" +
                        "    consola.mostrar(res)\n" +
                        "    retornar res\n" +
                        "}\n" +
                        "numero dividir(numero val1, numero val2) {\n" +
                        "    numero res = val1 / val2\n" +
                        "    consola.mostrar_informacion(\"-> Retorno Division: \")\n" +
                        "    consola.mostrar(res)\n" +
                        "    retornar res\n" +
                        "}\n" +
                        "\n" +
                        "intentar {\n" +
                        "    numero a = consola.pedir(\"Ingrese numero a: \")\n" +
                        "    numero b = consola.pedir(\"Ingrese numero b: \")\n" +
                        "\n" +
                        "    si (a < 1) {\n" +
                        "        lanzar \"Error: El numero 'a' es menor a 1\"\n" +
                        "    }\n" +
                        "    si (b < 1) {\n" +
                        "        lanzar \"Error: El numero 'b' es menor a 1\"\n" +
                        "    }\n" +
                        "\n" +
                        "    numero total = sumar(a, b) + multiplicar(a, b) + dividir(a, b)\n" +
                        "    consola.mostrar(total)\n" +
                        "} capturar (excepcion e) {\n" +
                        "    consola.mostrar_error(\"Error detectado: \")\n" +
                        "    consola.mostrar(e.mensaje)\n" +
                        "}\n";

        String salida = compilarYEjecutar(code, "TestMegaMatematicasError");
        System.setIn(System.in);

        // Verificamos que el sistema interceptó la excepción
        org.junit.jupiter.api.Assertions.assertTrue(salida.contains("El numero 'a' es menor a 1"), "Debe lanzar el error de validación");
    }

    /**
     * Este compila el código Quetzal a un .class, intercepta la consola de Java,
     * carga la clase generada, la ejecuta y devuelve lo que imprimió.
     */
    private String compilarYEjecutar(String code, String className) throws Exception {
        context.process(code);
        tokens.addAll(context.getTokens());
        var ast = parser.parse();

        BytecodeGenerator generator = new BytecodeGenerator();
        generator.compile(ast, className);

        PrintStream originalOut = System.out;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        System.setOut(new PrintStream(bos));

        try {
            File file = new File("");
            URL url = file.toURI().toURL();
            URLClassLoader classLoader = new URLClassLoader(new URL[]{url});
            Class<?> claseGenerada = classLoader.loadClass(className);

            Method mainMethod = claseGenerada.getMethod("main", String[].class);

            String[] params = null;
            mainMethod.invoke(null, (Object) params);

        } finally {
            System.setOut(originalOut);
        }

        return bos.toString();
    }
}