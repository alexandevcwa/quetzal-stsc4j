package com.stsc4j.parser.v1.parser;

import com.stsc4j.lexer.LexerContext;
import com.stsc4j.lexer.Token;
import com.stsc4j.parser.v1.ast.ASTPrinter;
import com.stsc4j.parser.v1.ast.StatementIf;
import com.stsc4j.parser.v1.exception.ParserException;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ParserIf - Tests")
class ParserIfTest {

    private static LexerContext context;
    private static List<Token> tokens;
    private static TokenStream tokenStream;
    private final static ASTPrinter astPrinter = new ASTPrinter();

    private static ParserIf parser;

    @AfterEach
    void cleanTokens() {
        tokenStream.clear();
        context.cleanToken();
    }

    @BeforeAll
    static void staticSetup() {
        context = new LexerContext();
        tokens = new ArrayList<>();
        tokenStream = new TokenStream(tokens);
        parser = new ParserIf(
                tokenStream,
                new ParserExpressions(tokenStream),
                new ParserBlock(tokenStream, new ParserPrincipal(tokenStream))
        );
    }

    @Test
    @DisplayName("Test - Declaración de if correcta")
    void testDeclaracionIfCorrecta() {
        final String code = "si (edad == 18) { edad = 3 } sino { edad = 4 }";
        context.process(code);
        tokens.addAll(context.getTokens());
        var ast = parser.parseStatement();
        assertThat(ast)
                .matches(s -> s instanceof StatementIf);
        System.out.println(astPrinter.print(ast));
    }

    @Test
    @DisplayName("Test - Declaración de if sin expresión")
    void testDeclaracionIfSinExpresion() {
        final String code = "si () { edad = 3 } sino { edad = 4 }";
        context.process(code);
        tokens.addAll(context.getTokens());
        var exception = assertThrows(ParserException.class, () -> parser.parseStatement());
        System.out.println(exception.getMessage());
    }

    @Test
    @DisplayName("Test - Declaración de if sin bloque")
    void testDeclaracionIfSinElse() {
        final String code = "si (edad >= 18)";
        context.process(code);
        tokens.addAll(context.getTokens());
        var exception = assertThrows(ParserException.class, () -> parser.parseStatement());
        System.out.println(exception.getMessage());
    }

    @Test
    @DisplayName("Test - Declaración de else sin if")
    void testDeclaracionElseSinIf() {
        final String code = "sino { edad = 4 }";
        context.process(code);
        tokens.addAll(context.getTokens());
        var exception = assertThrows(ParserException.class, () -> parser.parseStatement());
        System.out.println(exception.getMessage());
    }

    @Test
    @DisplayName("Test - Declaración de else sin bloque")
    void testDeclaracionElseSinBloque() {
        final String code = "si(a==b){ a=1 } sino";
        context.process(code);
        tokens.addAll(context.getTokens());
        var exception = assertThrows(ParserException.class, () -> parser.parseStatement());
        System.out.println(exception.getMessage());
    }

    @Test
    @DisplayName("Test - Declaración de if sin bloque y con else")
    void testDeclaracionIfSinBloque() {
        final String code = "si(a==b) sino { a = 1}";
        context.process(code);
        tokens.addAll(context.getTokens());
        var exception = assertThrows(ParserException.class, () -> parser.parseStatement());
        System.out.println(exception.getMessage());
    }

    @Test
    @DisplayName("Test - Declaración de if con múltiples expresiones")
    void testDeclaracionIfConMultiplesExpresiones(){
        final String code = "si ((a>2 y b<=3 o a==c)|| (a!=4 && 3>2)) { a=1 } sino { a=2 } sino { a=3 }";
        context.process(code);
        System.out.println("Tokens:");
        context.getTokens().forEach(System.out::println);
        tokens.addAll(context.getTokens());
        var ast = parser.parseStatement();
        assertThat(ast)
                .matches(s -> s instanceof StatementIf);
        System.out.println(astPrinter.print(ast));
    }

    @Test
    @DisplayName("Test - Declaración de if con múltiples operadores or '|||'")
    void testDeclaracionIfConMultiplesOperadoresOr(){
        final String code = "si (a>1 ||| b>4){ a = 1}";
        context.process(code);
        tokens.addAll(context.getTokens());
        var exception = assertThrows(ParserException.class, () -> parser.parseStatement());
        System.out.println(exception.getMessage());
    }

    @Test
    @DisplayName("Test - Declaración de if con múltiples operadores and '&&&'")
    void testDeclaracionIfConMultiplesOperadoresAnd(){
        final String code = "si (a>1 &&& b>4){ a = 1}";
        context.process(code);
        tokens.addAll(context.getTokens());
        var exception = assertThrows(ParserException.class, () -> parser.parseStatement());
        System.out.println(exception.getMessage());
    }

    @Test
    @DisplayName("Test - Declaración de if con else if")
    void testDeclaracionIfElseIf(){
        final String code = "si (a>1) { a = 1}sino si (b>4) { b = 2 } sino si (b>4) { b = 2 } sino { c = 3 }";
        context.process(code);
        tokens.addAll(context.getTokens());
        var ast = parser.parseStatement();
        assertThat(ast)
                .matches(s -> s instanceof StatementIf);
        System.out.println(astPrinter.print(ast));
    }

    @Test
    @DisplayName("Test - Declaración de if con else if anudados")
    void testDeclaracionIfElseIfAnudados(){
        final String code =
                "si (a==1){" +
                    "si (a>1) {" +
                        "a = 1" +
                    "} sino {a = 2}" +
                "} sino si(b==2){" +
                    "si(b>1){" +
                        "b = 1" +
                    "} sino {" +
                    "b=2 " +
                    "}" +
                "} sino si (c==3) {" +
                    "si(c>3){" +
                        "c=1" +
                    "} sino {" +
                    "c=4 }" +
                "} sino { d = 5 }";
        context.process(code);
        tokens.addAll(context.getTokens());
        var ast = parser.parseStatement();
        assertThat(ast)
                .matches(s -> s instanceof StatementIf);
        System.out.println(astPrinter.print(ast));
    }

    @Test
    @DisplayName("Test - Declaración de if con acceso a atributo JSON")
    void testDeclaracionIfAccesoAAtributoJsn(){
        final String code = "si (jsnObj.edad.adulta == 18) { edad = 3 } sino { edad = 4 }";
        context.process(code);
        tokens.addAll(context.getTokens());
        var ast = parser.parseStatement();
        assertThat(ast)
                .matches(s -> s instanceof StatementIf);
        System.out.println(astPrinter.print(ast));
    }
}