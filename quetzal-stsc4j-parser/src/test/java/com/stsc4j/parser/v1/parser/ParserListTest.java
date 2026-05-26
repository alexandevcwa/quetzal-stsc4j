package com.stsc4j.parser.v1.parser;

import com.stsc4j.lexer.LexerContext;
import com.stsc4j.lexer.Token;
import com.stsc4j.parser.v1.ast.ASTPrinter;
import com.stsc4j.parser.v1.ast.Statement;
import com.stsc4j.parser.v1.ast.StatementList;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ParserList - Tests de Declaración de Listas")
class ParserListTest {

    private static LexerContext context;
    private static List<Token> tokens;
    private static TokenStream tokenStream;
    private static ParserList parser;
    private ASTPrinter astPrinter;

    @AfterEach
    void cleanTokens() {
        tokenStream.clear();
        context.cleanToken();
        tokens.clear();
    }

    @BeforeAll
    static void staticSetup() {
        context = new LexerContext();
        tokens = new ArrayList<>();
        tokenStream = new TokenStream(tokens);
        parser = new ParserList(tokenStream, new ParserExpression(tokenStream));
    }

    @BeforeEach
    void setup() {
        astPrinter = new ASTPrinter();
    }

    // ==================== LISTAS TIPADAS ====================

    @Test
    @DisplayName("Test - Lista tipada con tipo texto (entero)")
    void testListaTipadadEnteros() {
        // Arrange
        final String code = "lista<entero> numeros = [1, 2, 3, 4, 5]";
        context.process(code);
        tokens.addAll(context.getTokens());

        // Act
        final Statement statement = assertDoesNotThrow(() -> parser.parseStatement());

        // Assert
        assertThat(statement)
                .isNotNull()
                .isInstanceOf(StatementList.class);

        StatementList list = (StatementList) statement;
        assertThat(list.listName.getLexeme()).isEqualTo("numeros");
        assertThat(list.expressionList.expressions).hasSize(5);
        assertThat(list.mutable).isFalse();

        System.out.println("\n✅ Lista tipada (enteros):\n" + astPrinter.print(statement));
    }

    @Test
    @DisplayName("Test - Lista tipada con tipo texto (strings)")
    void testListaTipadadTextos() {
        // Arrange
        final String code = "lista<texto> nombres = [\"Ana\", \"Carlos\", \"Maria\"]";
        context.process(code);
        tokens.addAll(context.getTokens());

        // Act
        final Statement statement = assertDoesNotThrow(() -> parser.parseStatement());

        // Assert
        assertThat(statement)
                .isNotNull()
                .isInstanceOf(StatementList.class);

        StatementList list = (StatementList) statement;
        assertThat(list.listName.getLexeme()).isEqualTo("nombres");
        assertThat(list.expressionList.expressions).hasSize(3);

        System.out.println("\nLista tipada (textos):\n" + astPrinter.print(statement));
    }

    @Test
    @DisplayName("Test - Lista tipada con tipo número (decimales)")
    void testListaTipadadNumeros() {
        // Arrange
        final String code = "lista<número> precios = [10.5, 20.3, 15.0]";
        context.process(code);
        tokens.addAll(context.getTokens());

        // Act
        final Statement statement = assertDoesNotThrow(() -> parser.parseStatement());

        // Assert
        assertThat(statement)
                .isNotNull()
                .isInstanceOf(StatementList.class);

        StatementList list = (StatementList) statement;
        assertThat(list.listName.getLexeme()).isEqualTo("precios");
        assertThat(list.expressionList.expressions).hasSize(3);

        System.out.println("\nLista tipada (números):\n" + astPrinter.print(statement));
    }

    // ==================== LISTAS VACÍAS ====================

    @Test
    @DisplayName("Test - Lista vacía tipada con tipo entero")
    void testListaVacia() {
        // Arrange
        final String code = "lista<entero> vacia = []";
        context.process(code);
        tokens.addAll(context.getTokens());

        // Act
        final Statement statement = assertDoesNotThrow(() -> parser.parseStatement());

        // Assert
        assertThat(statement)
                .isNotNull()
                .isInstanceOf(StatementList.class);

        StatementList list = (StatementList) statement;
        assertThat(list.listName.getLexeme()).isEqualTo("vacia");
        assertThat(list.expressionList.expressions).isEmpty();

        System.out.println("\nLista vacía:\n" + astPrinter.print(statement));
    }

    // ==================== LISTAS MUTABLES (var) ====================

    @Test
    @DisplayName("Test - Lista mutable con palabra clave var")
    void testListaMutableConVar() {
        // Arrange
        final String code = "lista<entero> var numeros = [3, 1, 4]";
        context.process(code);
        tokens.addAll(context.getTokens());

        // Act
        final Statement statement = assertDoesNotThrow(() -> parser.parseStatement());

        // Assert
        assertThat(statement)
                .isNotNull()
                .isInstanceOf(StatementList.class);

        StatementList list = (StatementList) statement;
        assertThat(list.listName.getLexeme()).isEqualTo("numeros");
        assertThat(list.mutable).isTrue();
        assertThat(list.expressionList.expressions).hasSize(3);

        System.out.println("\nLista mutable (var):\n" + astPrinter.print(statement));
    }

    // ==================== LISTAS BOOLEANAS ====================

    @Test
    @DisplayName("Test - Lista tipada con tipo booleano")
    void testListaTipadadBooleanos() {
        // Arrange
        final String code = "lista<log> banderas = [verdadero, falso, verdadero]";
        context.process(code);
        tokens.addAll(context.getTokens());

        // Act
        final Statement statement = assertDoesNotThrow(() -> parser.parseStatement());

        // Assert
        assertThat(statement)
                .isNotNull()
                .isInstanceOf(StatementList.class);

        StatementList list = (StatementList) statement;
        assertThat(list.listName.getLexeme()).isEqualTo("banderas");
        assertThat(list.expressionList.expressions).hasSize(3);

        System.out.println("\nLista tipada (booleanos):\n" + astPrinter.print(statement));
    }

    // ==================== LISTAS CON EXPRESIONES ====================

    @Test
    @DisplayName("Test - Lista con expresiones aritméticas")
    void testListaConExpresiones() {
        // Arrange
        final String code = "lista<entero> calculos = [1 + 2, 3 * 4, 5 - 1]";
        context.process(code);
        tokens.addAll(context.getTokens());

        // Act
        final Statement statement = assertDoesNotThrow(() -> parser.parseStatement());

        // Assert
        assertThat(statement)
                .isNotNull()
                .isInstanceOf(StatementList.class);

        StatementList list = (StatementList) statement;
        assertThat(list.expressionList.expressions).hasSize(3);

        System.out.println("\nLista con expresiones:\n" + astPrinter.print(statement));
    }

    // ==================== CASOS DE ERROR ====================

    @Test
    @DisplayName("Test - Error: Falta el símbolo '<' en la declaración de tipo")
    void testErrorFaltaMenorque() {
        // Arrange
        final String code = "lista entero> nombres = [1, 2, 3]";
        context.process(code);
        tokens.addAll(context.getTokens());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> parser.parseStatement(),
                "Debe lanzar excepción cuando falta '<'");
    }

    @Test
    @DisplayName("Test - Error: Falta el símbolo '>' en la declaración de tipo")
    void testErrorFaltaMayorque() {
        // Arrange
        final String code = "lista<entero nombres = [1, 2, 3]";
        context.process(code);
        tokens.addAll(context.getTokens());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> parser.parseStatement(),
                "Debe lanzar excepción cuando falta '>'");
    }

    @Test
    @DisplayName("Test - Error: Falta el identificador de la lista")
    void testErrorFaltaIdentificador() {
        // Arrange
        final String code = "lista<entero> = [1, 2, 3]";
        context.process(code);
        tokens.addAll(context.getTokens());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> parser.parseStatement(),
                "Debe lanzar excepción cuando falta el identificador");
    }

    @Test
    @DisplayName("Test - Error: Falta la asignación '='")
    void testErrorFaltaIgual() {
        // Arrange
        final String code = "lista<entero> numeros [1, 2, 3]";
        context.process(code);
        tokens.addAll(context.getTokens());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> parser.parseStatement(),
                "Debe lanzar excepción cuando falta '='");
    }

    @Test
    @DisplayName("Test - Error: Tipo de dato inválido en lista")
    void testErrorTipoDatoInvalido() {
        // Arrange
        final String code = "lista<invalido> nombres = [1, 2, 3]";
        context.process(code);
        tokens.addAll(context.getTokens());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> parser.parseStatement(),
                "Debe lanzar excepción cuando se proporciona un tipo inválido");
    }

    // ==================== LISTAS MULTIDIMENSIONALES ====================

    @Test
    @DisplayName("Test - Lista bidimensional (matriz)")
    void testListaBidimensional() {
        // Arrange
        final String code = "lista<lista<entero>> matriz = [[1, 2], [3, 4], [3, 4]]";
        context.process(code);
        tokens.addAll(context.getTokens());

        // Act
        final Statement statement = assertDoesNotThrow(() -> parser.parseStatement());

        // Assert
        assertThat(statement)
                .isNotNull()
                .isInstanceOf(StatementList.class);

        StatementList list = (StatementList) statement;
        assertThat(list.listName.getLexeme()).isEqualTo("matriz");

        System.out.println("\nLista bidimensional:\n" + astPrinter.print(statement));
    }

    @Test
    @DisplayName("Test - Lista mutable bidimensional")
    void testListaMutableBidimensional() {
        // Arrange
        final String code = "lista<lista<número>> var datos = [[1.5, 2.5], [3.5, 4.5]]";
        context.process(code);
        tokens.addAll(context.getTokens());

        // Act
        final Statement statement = assertDoesNotThrow(() -> parser.parseStatement());

        // Assert
        assertThat(statement)
                .isNotNull()
                .isInstanceOf(StatementList.class);
        
        StatementList list = (StatementList) statement;
        assertThat(list.mutable).isTrue();
        assertThat(list.expressionList.expressions).hasSize(2);
        
        System.out.println("\nLista mutable bidimensional:\n" + astPrinter.print(statement));
    }

    // ==================== LISTAS SIN TIPADO (MIXTAS) ====================

    @Test
    @DisplayName("Test - Lista sin tipado (mixta) con múltiples tipos")
    void testListaMixtaSinTipado() {
        // Arrange
        final String code = "lista mixta = [1, \"dos\", verdadero, 3.14]";
        context.process(code);
        tokens.addAll(context.getTokens());

        // Act
        final Statement statement = assertDoesNotThrow(() -> parser.parseStatement());

        // Assert
        assertThat(statement)
                .isNotNull()
                .isInstanceOf(StatementList.class);
        
        StatementList list = (StatementList) statement;
        assertThat(list.listName.getLexeme()).isEqualTo("mixta");
        assertThat(list.type).isNull(); // Sin tipo especificado
        assertThat(list.expressionList.expressions).hasSize(4);
        assertThat(list.mutable).isFalse();
        
        System.out.println("\nLista sin tipado (mixta):\n" + astPrinter.print(statement));
    }

    @Test
    @DisplayName("Test - Lista mutable sin tipado")
    void testListaMutableSinTipado() {
        // Arrange
        final String code = "lista var datos = [42, \"texto\", falso]";
        context.process(code);
        tokens.addAll(context.getTokens());

        // Act
        final Statement statement = assertDoesNotThrow(() -> parser.parseStatement());

        // Assert
        assertThat(statement)
                .isNotNull()
                .isInstanceOf(StatementList.class);
        
        StatementList list = (StatementList) statement;
        assertThat(list.listName.getLexeme()).isEqualTo("datos");
        assertThat(list.type).isNull(); // Sin tipo especificado
        assertThat(list.mutable).isTrue();
        assertThat(list.expressionList.expressions).hasSize(3);
        
        System.out.println("\nLista mutable sin tipado:\n" + astPrinter.print(statement));
    }

    @Test
    @DisplayName("Test - Lista vacía sin tipado")
    void testListaVaciaSinTipado() {
        // Arrange
        final String code = "lista contenedor = []";
        context.process(code);
        tokens.addAll(context.getTokens());

        // Act
        final Statement statement = assertDoesNotThrow(() -> parser.parseStatement());

        // Assert
        assertThat(statement)
                .isNotNull()
                .isInstanceOf(StatementList.class);
        
        StatementList list = (StatementList) statement;
        assertThat(list.listName.getLexeme()).isEqualTo("contenedor");
        assertThat(list.type).isNull(); // Sin tipo especificado
        assertThat(list.expressionList.expressions).isEmpty();
        
        System.out.println("\nLista vacía sin tipado:\n" + astPrinter.print(statement));
    }

    @Test
    @DisplayName("Test - Lista sin tipado con solo strings")
    void testListaSinTipadoSoloStrings() {
        // Arrange
        final String code = "lista textos = [\"hola\", \"mundo\", \"prueba\"]";
        context.process(code);
        tokens.addAll(context.getTokens());

        // Act
        final Statement statement = assertDoesNotThrow(() -> parser.parseStatement());

        // Assert
        assertThat(statement)
                .isNotNull()
                .isInstanceOf(StatementList.class);
        
        StatementList list = (StatementList) statement;
        assertThat(list.listName.getLexeme()).isEqualTo("textos");
        assertThat(list.type).isNull(); // Sin tipo especificado
        assertThat(list.expressionList.expressions).hasSize(3);
        
        System.out.println("\nLista sin tipado (solo strings):\n" + astPrinter.print(statement));
    }

    @Test
    @DisplayName("Test - Lista sin tipado con expresiones mixtas")
    void testListaSinTipadoConExpresiones() {
        // Arrange
        final String code = "lista var resultado = [1 + 2, \"texto\", 3.14 * 2]";
        context.process(code);
        tokens.addAll(context.getTokens());

        // Act
        final Statement statement = assertDoesNotThrow(() -> parser.parseStatement());

        // Assert
        assertThat(statement)
                .isNotNull()
                .isInstanceOf(StatementList.class);
        
        StatementList list = (StatementList) statement;
        assertThat(list.mutable).isTrue();
        assertThat(list.type).isNull(); // Sin tipo especificado
        assertThat(list.expressionList.expressions).hasSize(3);
        
        System.out.println("\nLista sin tipado con expresiones:\n" + astPrinter.print(statement));
    }

    @Test
    @DisplayName("Test - Lista sin tipado con valores nulos")
    void testListaSinTipadoConNulos() {
        // Arrange
        final String code = "lista flexible = [1, nulo, \"texto\", verdadero]";
        context.process(code);
        tokens.addAll(context.getTokens());

        // Act
        final Statement statement = assertDoesNotThrow(() -> parser.parseStatement());

        // Assert
        assertThat(statement)
                .isNotNull()
                .isInstanceOf(StatementList.class);
        
        StatementList list = (StatementList) statement;
        assertThat(list.listName.getLexeme()).isEqualTo("flexible");
        assertThat(list.type).isNull(); // Sin tipo especificado
        assertThat(list.expressionList.expressions).hasSize(4);
        
        System.out.println("\nLista sin tipado con nulos:\n" + astPrinter.print(statement));
    }
}