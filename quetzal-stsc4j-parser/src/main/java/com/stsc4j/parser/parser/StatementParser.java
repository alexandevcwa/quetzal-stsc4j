package com.stsc4j.parser.parser;

import com.stsc4j.lexer.Token;
import com.stsc4j.lexer.TokenType;
import com.stsc4j.lexer.strategy.LexerDictionary;
import com.stsc4j.parser.ast.TypeInfo;
import com.stsc4j.parser.ast.expression.Expression;
import com.stsc4j.parser.ast.statement.*;
import com.stsc4j.parser.exception.ParseException;

import java.util.ArrayList;
import java.util.List;

/**
 * Parser de sentencias del lenguaje.
 * Maneja declaraciones de variables, if, while, for, do-while, return, break, continue.
 */
public class StatementParser {

    private final ParserContext context;
    private final ExpressionParser exprParser;

    public StatementParser(ParserContext context) {
        this.context = context;
        this.exprParser = new ExpressionParser(context);
    }

    /**
     * Verifica si el tipo de token corresponde a un tipo primitivo o compuesto.
     */
    private boolean isTypeStart(TokenType type) {
        return (type == TokenType.PRIMITIVE_LONG || type == TokenType.PRIMITIVE_INT
                || type == TokenType.PRIMITIVE_SHORT || type == TokenType.PRIMITIVE_DOUBLE
                || type == TokenType.PRIMITIVE_FLOAT || type == TokenType.PRIMITIVE_STRING
                || type == TokenType.PRIMITIVE_BOOLEAN || type == TokenType.LIST
        );
    }

    /**
     * Punto de entrada principal para parsear una declaración o sentencia.
     */
    public Statement parseDeclaration() {
        try {
            if (isTypeStart(context.peek().getType())) {
                return parseVarDeclaration();
            }
            return parseStatement();
        } catch (ParseException e) {
            synchronize();
            return null;
        }
    }

    /**
     * Parsea una declaración de variable.
     * Sintaxis: tipo [var] identificador = expresión
     */
    public VarDeclaration parseVarDeclaration() {
        TypeInfo type = parseType();
        boolean isMutable = context.match(TokenType.MUTABLE_VARIABLE);
        Token id = context.consume(TokenType.IDENTIFIER, "Se esperaba un identificador");

        context.consume(TokenType.EQUAL, "Falta =");
        Expression init = exprParser.parseExpression();

        return new VarDeclaration(type, id.getLexeme(), isMutable, init);
    }

    /**
     * Parsea un tipo de dato, incluyendo tipos genéricos.
     * Ejemplos: entero, lista<entero>, lista<lista<texto>>
     */
    private TypeInfo parseType() {
        if (!isTypeStart(context.peek().getType())) throw new ParseException("Tipo inválido");
        String typeName = getTypeName(context.peek().getType());
        context.advance();

        // Verificar si hay un tipo genérico
        TypeInfo genericType = null;
        if (context.match(TokenType.LESS_THAN)) {
            genericType = parseType(); // Recursivo para tipos anidados
            context.consume(TokenType.GREATER_THAN, "Se esperaba '>' después del tipo genérico");
        }

        return new TypeInfo(typeName, genericType);
    }

    /**
     * Obtiene el nombre del tipo basándose en el TokenType.
     */
    private String getTypeName(TokenType type) {
        switch (type) {
            case PRIMITIVE_LONG:
            case PRIMITIVE_INT:
            case PRIMITIVE_SHORT:
                return LexerDictionary.ENTERO;
            case PRIMITIVE_DOUBLE:
            case PRIMITIVE_FLOAT:
                return LexerDictionary.NUMERO;
            case PRIMITIVE_STRING:
                return LexerDictionary.TEXTO;
            case PRIMITIVE_BOOLEAN:
                return LexerDictionary.LOG;
            case LIST:
                return LexerDictionary.LIST;
            default:
                throw new ParseException("Tipo no reconocido: " + type);
        }
    }

    /**
     * Parsea una sentencia.
     * Detecta el tipo de sentencia basándose en el token actual.
     */
    public Statement parseStatement() {
        // Sentencia if
        if (context.match(TokenType.IF)) {
            return parseIfStatement();
        }

        // Sentencia while
        if (context.match(TokenType.LOOP_WHILE)) {
            return parseWhileStatement();
        }

        // Sentencia for
        if (context.match(TokenType.LOOP_FOR)) {
            return parseForStatement();
        }

        // Sentencia do-while
        if (context.match(TokenType.LOOP_DO)) {
            return parseDoWhileStatement();
        }

        // Sentencia return
        if (context.match(TokenType.RETURN)) {
            return parseReturnStatement();
        }

        // Sentencia break
        if (context.match(TokenType.BREAK)) {
            return new BreakStatement();
        }

        // Sentencia continue
        if (context.match(TokenType.CONTINUE)) {
            return new ContinueStatement();
        }

        // Bloque de código
        if (context.match(TokenType.BRACES_OPEN)) {
            return new BlockStatement(parseBlock());
        }

        // Si no es ninguna de las anteriores, es una expresión como sentencia
        return parseExpressionStatement();
    }

    /**
     * Parsea una sentencia if-else.
     * Sintaxis: si (condición) { ... } [sino { ... }]
     */
    private IfStatement parseIfStatement() {
        context.consume(TokenType.LEFT_PARENT, "Se esperaba '(' después de 'si'");
        Expression condition = exprParser.parseExpression();
        context.consume(TokenType.RIGHT_PARENT, "Se esperaba ')' después de la condición");

        context.consume(TokenType.BRACES_OPEN, "Se esperaba '{' después de la condición del if");
        List<Statement> thenBranch = parseBlock();

        List<Statement> elseBranch = null;
        if (context.match(TokenType.ELSE)) {
            context.consume(TokenType.BRACES_OPEN, "Se esperaba '{' después de 'sino'");
            elseBranch = parseBlock();
        }

        return new IfStatement(condition, thenBranch, elseBranch);
    }

    /**
     * Parsea una sentencia while.
     * Sintaxis: mientras (condición) { ... }
     */
    private WhileStatement parseWhileStatement() {
        context.consume(TokenType.LEFT_PARENT, "Se esperaba '(' después de 'mientras'");
        Expression condition = exprParser.parseExpression();
        context.consume(TokenType.RIGHT_PARENT, "Se esperaba ')' después de la condición");

        context.consume(TokenType.BRACES_OPEN, "Se esperaba '{' después de la condición del while");
        List<Statement> body = parseBlock();

        return new WhileStatement(condition, body);
    }

    /**
     * Parsea una sentencia for.
     * Sintaxis: para (inicialización; condición; incremento) { ... }
     */
    private ForStatement parseForStatement() {
        context.consume(TokenType.LEFT_PARENT, "Se esperaba '(' después de 'para'");

        // Inicialización (puede ser declaración de variable o expresión)
        Statement initializer = null;
        if (!context.check(TokenType.DOUBLE_DOT)) {
            if (isTypeStart(context.peek().getType())) {
                initializer = parseVarDeclaration();
            } else {
                initializer = parseExpressionStatement();
            }
        }
        context.consume(TokenType.DOUBLE_DOT, "Se esperaba ';' después de la inicialización del for");

        // Condición
        Expression condition = null;
        if (!context.check(TokenType.DOUBLE_DOT)) {
            condition = exprParser.parseExpression();
        }
        context.consume(TokenType.DOUBLE_DOT, "Se esperaba ';' después de la condición del for");

        // Incremento
        Expression increment = null;
        if (!context.check(TokenType.RIGHT_PARENT)) {
            increment = exprParser.parseExpression();
        }
        context.consume(TokenType.RIGHT_PARENT, "Se esperaba ')' después del for");

        context.consume(TokenType.BRACES_OPEN, "Se esperaba '{' después del for");
        List<Statement> body = parseBlock();

        return new ForStatement(initializer, condition, increment, body);
    }

    /**
     * Parsea una sentencia do-while.
     * Sintaxis: hacer { ... } mientras (condición)
     */
    private DoWhileStatement parseDoWhileStatement() {
        context.consume(TokenType.BRACES_OPEN, "Se esperaba '{' después de 'hacer'");
        List<Statement> body = parseBlock();

        context.consume(TokenType.LOOP_WHILE, "Se esperaba 'mientras' después del bloque do");
        context.consume(TokenType.LEFT_PARENT, "Se esperaba '(' después de 'mientras'");
        Expression condition = exprParser.parseExpression();
        context.consume(TokenType.RIGHT_PARENT, "Se esperaba ')' después de la condición");

        return new DoWhileStatement(body, condition);
    }

    /**
     * Parsea una sentencia return.
     * Sintaxis: retornar [expresión]
     */
    private ReturnStatement parseReturnStatement() {
        Expression value = null;
        // Si no hay punto y coma inmediatamente, hay una expresión de retorno
        if (!context.check(TokenType.DOUBLE_DOT) && !context.check(TokenType.BRACES_CLOSE)) {
            value = exprParser.parseExpression();
        }
        return new ReturnStatement(value);
    }

    /**
     * Parsea un bloque de código (lista de sentencias entre llaves).
     * Nota: Se asume que ya se consumió el '{'.
     */
    private List<Statement> parseBlock() {
        List<Statement> statements = new ArrayList<>();

        while (!context.check(TokenType.BRACES_CLOSE) && !context.isAtEnd()) {
            Statement stmt = parseDeclaration();
            if (stmt != null) {
                statements.add(stmt);
            }
        }

        context.consume(TokenType.BRACES_CLOSE, "Se esperaba '}' al final del bloque");
        return statements;
    }

    /**
     * Parsea una expresión como sentencia.
     */
    private ExpressionStatement parseExpressionStatement() {
        Expression expr = exprParser.parseExpression();
        return new ExpressionStatement(expr);
    }

    /**
     * Método de recuperación de errores.
     * Avanza hasta encontrar un punto de sincronización (inicio de nueva sentencia).
     */
    private void synchronize() {
        context.advance();

        while (!context.isAtEnd()) {
            // Sincronizar en puntos de inicio de sentencias
            switch (context.peek().getType()) {
                case PRIMITIVE_LONG:
                case PRIMITIVE_INT:
                case PRIMITIVE_SHORT:
                case PRIMITIVE_DOUBLE:
                case PRIMITIVE_FLOAT:
                case PRIMITIVE_STRING:
                case PRIMITIVE_BOOLEAN:
                case IF:
                case LOOP_WHILE:
                case LOOP_FOR:
                case LOOP_DO:
                case RETURN:
                case BREAK:
                case CONTINUE:
                    return;
            }
            context.advance();
        }
    }
}
