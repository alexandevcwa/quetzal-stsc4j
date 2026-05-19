package com.stsc4j.parser.v1.parser;

import com.stsc4j.lexer.Token;
import com.stsc4j.lexer.TokenType;
import com.stsc4j.parser.v1.ast.*;
import com.stsc4j.parser.v1.exception.ParserException;

import java.util.ArrayList;
import java.util.List;

public class ParserExpression extends Parser {

    private final TokenStream tokenStream;

    public ParserExpression(TokenStream tokenStream) {
        this.tokenStream = tokenStream;
    }

    // Punto de entrada para cualquier expresión
    @Override
    public Expression parseExpression() {
        return parseSymbolicExpression();
    }

    private Expression parseSymbolicExpression() {
        Expression expression = parseEspaniolExpression();
        while (tokenStream.match(TokenType.AND, TokenType.OR)) {
            Token operator = tokenStream.before();
            Token secondaryOperator = null;
            if ((tokenStream.match(TokenType.AND) && operator.getType() == TokenType.AND) ||
                    (tokenStream.match(TokenType.OR) && operator.getType() == TokenType.OR)
            ) {
                if (tokenStream.match(TokenType.AND, TokenType.OR)) {
                    throw new ParserException("No se pueden mezclar operadores lógicos '&&' y '||' sin paréntesis para definir la precedencia.");
                }
                secondaryOperator = tokenStream.before();
            } else {
                var logico = operator.getType() == TokenType.AND ? "&&" : "||";
                throw new ParserException("Se esperaba un operador lógico adicional para formar '" + logico + "'.");
            }
            if (secondaryOperator != null) {
                final Token[] operators = {operator, secondaryOperator};
                expression = new ExpressionBinary(expression, operators, parseEspaniolExpression());
            } else {
                Expression right = parseEspaniolExpression();
                expression = new ExpressionBinary(expression, operator, right);
            }
        }
        return expression;
    }

    private Expression parseEspaniolExpression() {
        Expression expression = parseEqualExpression();
        while (tokenStream.match(TokenType.AND_ESP, TokenType.OR_ESP)) {
            Token operator = tokenStream.before();
            Expression right = parseEqualExpression();
            expression = new ExpressionBinary(expression, operator, right);
        }
        return expression;
    }

    // Maneja ==, !=
    private Expression parseEqualExpression() {
        Expression expression = parseRelationalExpression();
        while (tokenStream.match(TokenType.EQUAL, TokenType.EXCLAMATION)) {
            Token operator = tokenStream.show();

            Token secondaryOperator;
            if (tokenStream.match(TokenType.EQUAL)) {
                secondaryOperator = tokenStream.before();
            } else {
                tokenStream.back();
                return expression;
            }

            Expression right = parseRelationalExpression();
            if (secondaryOperator != null) {
                final Token[] operators = {operator, secondaryOperator};
                expression = new ExpressionBinary(expression, operators, right);
            } else {
                expression = new ExpressionBinary(expression, operator, right);
            }
        }
        return expression;
    }

    // Maneja >, <, >=, <=
    private Expression parseRelationalExpression() {
        Expression expression = parseModuleExpression();
        while (tokenStream.match(TokenType.GREATER_THAN, TokenType.LESS_THAN)) {
            Token operator = tokenStream.before();
            Token secondaryOperator = null;
            if (tokenStream.match(TokenType.EQUAL)) {
                secondaryOperator = tokenStream.before();
            }
            Expression right = parseModuleExpression();
            if (secondaryOperator != null) {
                final Token[] operators = {operator, secondaryOperator};
                expression = new ExpressionBinary(expression, operators, right);
            } else {
                expression = new ExpressionBinary(expression, operator, right);
            }
        }
        return expression;
    }

    // Maneja %
    private Expression parseModuleExpression() {
        Expression expression = parseAddAndSubtractExpression();
        while (tokenStream.match(TokenType.MODULE)) {
            Token operator = tokenStream.before();
            Expression right = parseAddAndSubtractExpression();
            expression = new ExpressionBinary(expression, operator, right);
        }
        return expression;
    }

    // Maneja + y -
    private Expression parseAddAndSubtractExpression() {
        Expression expression = parseMultiplyAndDivideExpression();
        while (tokenStream.match(TokenType.PLUS, TokenType.MINUS)) {
            Token operator = tokenStream.before();
            Expression right = parseMultiplyAndDivideExpression();
            expression = new ExpressionBinary(expression, operator, right);
        }
        return expression;
    }

    // Maneja * y /
    private Expression parseMultiplyAndDivideExpression() {
        Expression expression = parsePostfixExpression();
        while (tokenStream.match(TokenType.MULTIPLY, TokenType.DIVIDE)) {
            Token operator = tokenStream.before();
            Expression right = parsePostfixExpression();
            expression = new ExpressionBinary(expression, operator, right);
        }
        return expression;
    }

    // Maneja ++ y -- como operadores de postfijo
    private Expression parsePostfixExpression() {
        Expression expression = parseMethodCall();
        if (expression instanceof ExpressionVariable) {
            boolean isOk1 = tokenStream.match(TokenType.PLUS, TokenType.MINUS);
            if (!isOk1) {
                return expression;
            }
            Token operator1 = tokenStream.before();
            boolean isOk2 = tokenStream.match(TokenType.PLUS, TokenType.MINUS);
            if (!isOk2) {
                tokenStream.back();
                return expression;
            }
            Token operator2 = tokenStream.before();
            if (operator1.getType().equals(operator2.getType())) {
                return new ExpressionIncDec((ExpressionVariable) expression, new Token[]{operator1, operator2});
            }
            tokenStream.back(2);
            throw new ParserException("Operadores de incremento/decremento deben ser iguales para formar '++' o '--'.");
        }
        return expression;
    }

    /**
     * Analiza y procesa una lista de argumentos a partir de la secuencia de tokens actual.
     * Los argumentos son interpretados como expresiones y se agregan a una lista en el
     * orden en que se presentan en la entrada.
     *
     * @return Una lista de objetos {@code Expression} que representan las expresiones
     * extraídas como argumentos. Si no hay argumentos presentes, retorna una lista vacía.
     */
    private List<Expression> parseArgs() {
        List<Expression> args = new ArrayList<>();
        if (!tokenStream.matchNotAdvance(TokenType.RIGHT_PARENT)) {
            do {
                args.add(parseExpression());
            } while (tokenStream.match(TokenType.COMMA));
        }
        return args;
    }

    /**
     * Punto de entrada para parsear llamadas a métodos y acceso a propiedades.
     * Delega la responsabilidad a métodos especializados.
     */
    private Expression parseMethodCall() {
        Expression expression = parseIndexAccess();
        Token before = tokenStream.before();

        // Controlar llamada directa a función: mifuncion()
        if (tokenStream.match(TokenType.LEFT_PARENT)) {
            expression = parseDirectMethodCall(expression, before);
        }


        // Controlar acceso mediante punto: obj.prop o obj.metodo()
        expression = parsePropertyAndMethodAccess(expression);
        return expression;
    }

    /**
     * Parsea una llamada a método directo con argumentos.
     * Responsabilidad única: procesar la invocación de métodos.
     *
     * @param object     La expresión que representa el objeto/función a invocar
     * @param methodName El token que representa el nombre de la función
     * @return Una ExpressionMethodCall con los argumentos parseados
     */
    private Expression parseDirectMethodCall(Expression object, Token methodName) {
        List<Expression> args = parseArgs();
        tokenStream.consume(TokenType.RIGHT_PARENT, "Se esperaba ')' después de los argumentos.");
        return new ExpressionMethodCall(object, methodName, args);
    }

    /**
     * Parsea encadenamiento de propiedades y métodos mediante notación de punto.
     * Responsabilidad única: procesar acceso a propiedades y métodos encadenados.
     * <p>
     * Ejemplos:
     * - obj.propiedad → ExpressionPropertyAccess
     * - obj.metodo() → ExpressionMethodCall
     * - obj.prop1.prop2.metodo() → Anidamiento de expresiones
     *
     * @param expression La expresión inicial (objeto base)
     * @return La expresión resultante después de procesar todos los accesos
     */
    private Expression parsePropertyAndMethodAccess(Expression expression) {
        while (tokenStream.match(TokenType.DOT)) {
            Token accessName = tokenStream.consume(TokenType.IDENTIFIER,
                    "Se esperaba el nombre de propiedad o método después del '.'");

            if (tokenStream.match(TokenType.LEFT_PARENT)) {
                // Es un método: obj.metodo()
                expression = parseDirectMethodCall(expression, accessName);
            } else {
                // Es una propiedad JSN: obj.propiedad
                expression = new ExpressionPropertyAccess(expression, accessName);
            }
        }
        return expression;
    }

    // Manejar expresiones de acceso a índices 'lista[1]'
    private Expression parseIndexAccess() {
        Expression expression = primaryParser();

        if (tokenStream.matchNotAdvance(TokenType.BRACKETS_OPEN)) {
            if (!(expression instanceof ExpressionVariable)) {
                throw new ParserException("El nombre de la lista no es una variable válida.");
            }
        }
        List<Expression> indexList = null;
        while (tokenStream.match(TokenType.BRACKETS_OPEN)) {
            if (indexList == null) {
                indexList = new ArrayList<>();
            }
            Expression idx = parseExpression();
            if (idx instanceof ExpressionVariable || (idx instanceof ExpressionLiteral)) {
                if (idx instanceof ExpressionLiteral) {
                    ExpressionLiteral literal = (ExpressionLiteral) idx;
                    if (!literal.token.getType().equals(TokenType.LIT_INTEGER)) {
                        throw new ParserException("El índice de acceso debe ser un entero o una variable.");
                    }
                }
                indexList.add(idx);
                tokenStream.consume(TokenType.BRACKETS_CLOSE, "Se esperaba ']' después del índice.");
            } else {
                throw new ParserException("El índice de acceso debe ser una expresión válida (variable o literal numérica).");
            }
        }
        if (indexList != null) {
            expression = new ExpressionIndexAccess(expression, indexList);
        }
        return expression;
    }

    private Expression primaryParser() {
        // Controla literales enteros
        if (tokenStream.match(TokenType.LIT_INTEGER)) {
            Token token = tokenStream.before();
            return new ExpressionLiteral(token, token.getLexeme());
        }

        // Controla literales decimales
        if (tokenStream.match(TokenType.LIT_DECIMAL)) {
            Token token = tokenStream.before();
            return new ExpressionLiteral(token, token.getLexeme());
        }

        // Controla cadenas de texto
        if (tokenStream.match(TokenType.LIT_STRING)) {
            Token token = tokenStream.before();
            return new ExpressionLiteral(token, token.getLexeme());
        }

        // Controla booleanos
        if (tokenStream.match(TokenType.LIT_TRUE) || tokenStream.match(TokenType.LIT_FALSE)) {
            Token token = tokenStream.before();
            return new ExpressionLiteral(token, token.getLexeme());
        }

        // Controla variables y negación de variables
        if (tokenStream.match(TokenType.IDENTIFIER, TokenType.EXCLAMATION, TokenType.NEGATION_ESP)) {
            Token token = tokenStream.before();
            if (token.getType() == TokenType.EXCLAMATION || token.getType() == TokenType.NEGATION_ESP) {
                Token id = tokenStream.consume(TokenType.IDENTIFIER, "Se esperaba el nombre de la variable.");
                return new ExpressionVariable(id, true);
            }
            return new ExpressionVariable(token, false);
        }

        // Controla null
        if (tokenStream.match(TokenType.NULL)) {
            return new ExpressionNull(tokenStream.before());
        }

        // Controla apertura y cierre de paréntesis
        if (tokenStream.match(TokenType.LEFT_PARENT)) {
            Expression expression = parseExpression();
            tokenStream.consume(TokenType.RIGHT_PARENT, "Se esperaba ')' después de la expresión.");
            return expression;
        }

        // Controla números negativos
        if (tokenStream.match(TokenType.MINUS)) {
            if (tokenStream.match(TokenType.LIT_INTEGER, TokenType.LIT_DECIMAL)) {
                Token number = tokenStream.before();
                var token = new Token(number.getType(), ("-" + number.lexeme), number.line);
                return new ExpressionLiteral(token, token.getLexeme());
            } else {
                tokenStream.back();
            }
        }
        throw new ParserException("Se esperaba una expresión.");
    }

    /**
     * Analiza y construye una expresión ternaria a partir de una expresión binaria inicial
     * y las expresiones adicionales necesarias para completar la estructura ternaria.
     * <p>
     * La expresión ternaria tiene la forma: <condición> ? <expresión1> : <expresión2>.
     * Este método se encarga de procesar los componentes de la expresión y validarlos.
     *
     * @param expression La expresión binaria que representa la condición de la expresión ternaria.
     * @return Una instancia de {@code ExpressionTernary} que modela la expresión ternaria completa
     * construida a partir de la condición y las expresiones adicionales.
     * @throws RuntimeException Si no se encuentra el separador ':' o si las expresiones no son válidas.
     */
    public Expression parseTernaryExpression(ExpressionBinary expression) {
        Expression left = primaryParser();
        tokenStream.match(TokenType.DOUBLE_DOT, "Se esperaba ':' después de la expresión del medio en el operador ternario.");
        Expression right = primaryParser();
        return new ExpressionTernary(expression, left, right);
    }
}
