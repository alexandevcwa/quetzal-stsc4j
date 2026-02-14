package com.stsc4j.parser.ast;

import com.stsc4j.lexer.Token;
import com.stsc4j.lexer.TokenType;
import com.stsc4j.lexer.strategy.Classifier;
import com.stsc4j.lexer.strategy.ClassifierKeywords;
import com.stsc4j.parser.declaration.BinaryExpression;
import com.stsc4j.parser.declaration.LiteralExpression;
import com.stsc4j.parser.declaration.VarDeclaration;
import com.stsc4j.parser.declaration.VariableExpression;

import java.util.ArrayList;
import java.util.List;

import static com.stsc4j.lexer.strategy.LexerDictionary.*;

public class Parser {

    /**
     * Lista de tokens utilizados por el analizador para procesar la entrada.
     * Actúa como una representación manejable y estructurada de los elementos
     * léxicos del código fuente, facilitando el análisis sintáctico.
     * <p>
     * La lista de tokens es inmutable (final) para garantizar que los elementos
     * léxicos no se modifiquen una vez obtenidos durante la etapa de análisis
     * léxico.
     */
    private final List<Token> tokens;

    /**
     * Índice actual que señala la posición del token que se está analizando dentro de la lista de tokens.
     * Es utilizado para el seguimiento y control del flujo en el proceso de análisis de sintaxis.
     */
    private int current = 0;

    /**
     * Constructor de la clase Parser.
     * Crea una nueva instancia de Parser utilizando una lista de tokens como entrada.
     *
     * @param tokens Lista de tokens que serán procesados por el analizador sintáctico (parser).
     */
    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    /**
     * Retorna el token actual en la lista de tokens sin avanzar el índice.
     * Este método es útil para inspeccionar el siguiente token que se procesará
     * sin modificar el estado del analizador.
     *
     * @return El token actual de la lista de tokens.
     */
    private Token peek() {
        return tokens.get(current);
    }

    /**
     * Indica si el token actual es el final de la lista de tokens.
     *
     * @return true si el token actual es el final de la lista de tokens, false en caso contrario.
     */
    private boolean isAtEnd() {
        return peek().getType() == TokenType.EOF;
    }

    /**
     * Consume un token del tipo especificado y avanza el índice de tokens si el token actual coincide
     * con el tipo esperado. Si no coincide, lanza una excepción con el mensaje proporcionado.
     *
     * @param type El tipo de token que se espera consumir.
     * @param msg  El mensaje de error que se incluirá en la excepción si el token no coincide.
     * @return El token actual sí coincide con el tipo especificado.
     * @throws RuntimeException Si el token actual no coincide con el tipo esperado, indicando
     *                          el error junto con el token encontrado.
     */
    private Token consume(TokenType type, String msg) {
        if (peek().getType() == type) return tokens.get(current++);
        throw new RuntimeException(msg + " se encontró " + peek());
    }

    /**
     * Verifica si el tipo del token actual coincide con el tipo especificado.
     * Si coincide, avanza al siguiente token y retorna true. Si no coincide,
     * no avanza en la lista de tokens y retorna false.
     *
     * @param type El tipo de token que se desea comparar con el token actual.
     * @return true si el tipo del token actual coincide con el tipo especificado,
     * false en caso contrario.
     */
    private boolean match(TokenType type) {
        if (peek().getType() == type) {
            current++;
            return true;
        }
        return false;
    }

    /**
     * Analiza e interpreta un tipo de dato a partir de los tokens disponibles.
     * Puede manejar tanto tipos primitivos como tipos genéricos (por ejemplo, List<T>).
     * En el caso de tipos genéricos, se realiza un análisis recursivo para procesar
     * el tipo contenido dentro del genérico.
     *
     * @return Una instancia de la clase TypeInfo que representa el tipo de dato analizado,
     * incluidas sus posibles características genéricas.
     */
    private TypeInfo parseType() {
        String typeName = consumeTypeKeyword();

        TypeInfo generic = null;
        if (typeName.equals(LIST) && match(TokenType.LESS_THAN)) {
            generic = parseType();
            consume(TokenType.GREATER_THAN, "Falta '>' cierre de genérico");
        }
        return new TypeInfo(typeName, generic);
    }


    /**
     * Este método procesa un token de tipo y devuelve su representación
     * categórica según el tipo primitivo encontrado. Se utiliza para interpretar
     * tipos como enteros, números, texto y valores booleanos.
     *
     * @return Una cadena de texto que representa la categoría del tipo procesado:
     * "ENTERO" para tipos enteros, "NUMERO" para números reales,
     * "TEXTO" para cadenas de texto y "LOG" para valores booleanos.
     * @throws RuntimeException Si no se encuentra un tipo válido en el token actual.
     */
    private String consumeTypeKeyword() {
        if (match(TokenType.PRIMITIVE_LONG)) return ENTERO;
        if (match(TokenType.PRIMITIVE_INT)) return ENTERO;
        if (match(TokenType.PRIMITIVE_SHORT)) return ENTERO;
        if (match(TokenType.PRIMITIVE_DOUBLE)) return NUMERO;
        if (match(TokenType.PRIMITIVE_FLOAT)) return NUMERO;
        if (match(TokenType.PRIMITIVE_STRING)) return TEXTO;
        if (match(TokenType.PRIMITIVE_BOOLEAN)) return LOG;
        throw new RuntimeException("Se esperaba un tipo válido");
    }

    /**
     * Analiza la entrada de tokens y los transforma en una lista de declaraciones (statements).
     * Este método recorre la lista de tokens para identificar y construir las declaraciones
     * correspondientes según las reglas sintácticas.
     *
     * @return Una lista de objetos Statement que representa las declaraciones analizadas.
     */
    public List<Statement> parse() {
        List<Statement> statements = new ArrayList<>();
        while (!isAtEnd()) {
            statements.add(parseDeclaration());
        }
        return statements;
    }

    /**
     * Analiza una declaración en el flujo de tokens y construye un objeto
     * de tipo Statement que representa la declaración interpretada.
     * El método identifica el tipo de dato de la declaración, verifica si
     * la variable es mutable y consume el token de identificador.
     * <p>
     * Si detecta una llamada a función (paréntesis izquierdo), retorna null.
     * En caso contrario, interpreta la declaración como una variable
     * utilizando el método parseVarDeclaration.
     *
     * @return Un objeto Statement que representa la declaración analizada,
     * o null si la declaración corresponde a una función.
     */
    private Statement parseDeclaration() {
        TypeInfo type = parseType();
        boolean isMutable = match(TokenType.MUTABLE_VARIABLE);
        Token id = consume(TokenType.IDENTIFIER, "Se esperaba un identificador");
        if (match(TokenType.LEFT_PARENT)) {
            return null;
        }
        return parseVarDeclaration(type, isMutable, id.getLexeme());
    }

    private VarDeclaration parseVarDeclaration(TypeInfo type, boolean isMutable, String name) {
        consume(TokenType.EQUAL, "Falta =");
        Expression init = parseExpression();
        return new VarDeclaration(type, isMutable, name, init);
    }

    private Expression parseExpression() {
        return parseTerm();
    }

    private Expression parseTerm() {
        Expression left = parsePrimary();
        while (match(TokenType.PLUS)) {
            Expression right = parsePrimary();
            left = new BinaryExpression(left, right, "+");
        }
        return left;
    }

    private Expression parsePrimary() {
        if (match(TokenType.LIT_LONG))
            return new LiteralExpression(Long.parseLong(tokens.get(current - 1).getLexeme()));
        if (match(TokenType.LIT_INT))
            return new LiteralExpression(Integer.parseInt(tokens.get(current - 1).getLexeme()));
        if (match(TokenType.LIT_SHORT))
            return new LiteralExpression(Short.parseShort(tokens.get(current - 1).getLexeme()));
        if (match(TokenType.LIT_DOUBLE))
            return new LiteralExpression(Double.parseDouble(tokens.get(current - 1).getLexeme()));
        if (match(TokenType.LIT_FLOAT))
            return new LiteralExpression(Float.parseFloat(tokens.get(current - 1).getLexeme()));
        if (match(TokenType.LIT_STRING)) return new LiteralExpression(tokens.get(current - 1).getLexeme());
        if (match(TokenType.LIT_TRUE)) return new LiteralExpression(true);
        if (match(TokenType.LIT_FALSE)) return new LiteralExpression(false);
        if (match(TokenType.IDENTIFIER)) return new VariableExpression(tokens.get(current - 1).getLexeme());

        if (match(TokenType.LEFT_BRACKET)) {
            List<Expression> elements = new ArrayList<>();
            if (!check(TokenType.LEFT_BRACKET)) {
                do {
                    elements.add(parseExpression());
                } while (match(TokenType.COMMA));
            }
        }

        throw new RuntimeException("Expresión no esperada: " + peek());
    }

    private boolean check(TokenType type) {
        return peek().getType() == type;
    }


}
