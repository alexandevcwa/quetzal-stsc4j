package com.stsc4j.parser.v1.parser;

import com.stsc4j.lexer.Token;
import com.stsc4j.lexer.TokenType;
import com.stsc4j.parser.v1.ast.*;

import java.util.ArrayList;
import java.util.List;

public class ParserListExpression {

    private final TokenStream tokenStream;
    private final ParserExpressions parserExpressions;

    public ParserListExpression(TokenStream tokenStream, ParserExpressions parserExpressions) {
        this.tokenStream = tokenStream;
        this.parserExpressions = parserExpressions;
    }

    public Statement parseListExpression() {
        TypeList typeList = (TypeList) parseListType();
        boolean isMutable = tokenStream.match(TokenType.MUTABLE_VARIABLE);
        Token identified = tokenStream.consume(TokenType.IDENTIFIER, "Se esperaba el identificador de la lista.");
        tokenStream.consume(TokenType.EQUAL, "Se esperaba '=' después del identificador de la lista.");
        short depth = listDepth(typeList);

        // Usar la función recursiva para parsear listas multidimensionales
        ExpressionList expressionList = parseMultidimensionalList(depth);

        return new StatementList(typeList, isMutable, identified, expressionList);
    }

    /**
     * Función recursiva auto-ejecutable para parsear matrices multidimensionales.
     * Maneja listas de cualquier profundidad: [1,2,3], [[1,2], [3,4]], [[[...]]]
     *
     * @param currentDepth Profundidad actual de recursión (0 = elementos primitivos)
     * @return ExpressionList que contiene todas las expresiones parseadas recursivamente
     */
    private ExpressionList parseMultidimensionalList(short currentDepth) {
        tokenStream.consume(TokenType.BRACKETS_OPEN, "Se esperaba '[' para iniciar la declaración de la lista.");

        List<Expression> elements = new ArrayList<>();
        boolean isFirstElement = true;

        // Bucle principal para parsear elementos de la lista actual
        while (true) {
            // Verificar si hemos llegado al final de la lista
            if (tokenStream.match(TokenType.BRACKETS_CLOSE)) {
                break;
            }

            // No permitir coma al inicio
            if (!isFirstElement) {
                tokenStream.consume(TokenType.COMMA, "Se esperaba ',' entre elementos de la lista.");
            }
            isFirstElement = false;

            Expression element;

            // Si la profundidad es mayor a 0, significa que necesitamos listas anidadas
            if (currentDepth > 0) {
                // Llamada recursiva: parsear sub-lista
                element = parseMultidimensionalList((short) (currentDepth - 1));
            } else {
                // Caso base: parsear expresión primitiva
                element = parserExpressions.parseExpression();
            }

            elements.add(element);
        }

        // Crear y retornar la ExpressionList con los elementos parseados
        ExpressionList expressionList = new ExpressionList();
        expressionList.expressions = elements;
        return expressionList;
    }

    /**
     * Obtiene la profundidad de la lista.
     *
     * @param typeList Lista de tipos
     * @return Profundidad de la lista
     */
    private short listDepth(TypeList typeList) {
        short depth = 0;
        while (typeList != null) {
            Type currentType = typeList.elementType;
            if (currentType instanceof TypeList) {
                depth++;
                typeList = (TypeList) currentType;
            } else {
                break;
            }
        }
        return depth;
    }

    /**
     * Parsea el tipo de lista.
     *
     * @return El tipo de lista
     */
    private Type parseListType() {
        if (tokenStream.notMatch(TokenType.LESS_THAN)) {
            throw new RuntimeException("Se esperaba '<' para iniciar la declaración del tipo de lista.");
        }
        if (tokenStream.match(TokenType.PRIMITIVE_INTEGER, TokenType.PRIMITIVE_DECIMAL, TokenType.PRIMITIVE_STRING,
                TokenType.PRIMITIVE_BOOLEAN)) {
            TypeList t = new TypeList(new TypePrimitive(tokenStream.before()));
            tokenStream.consume(TokenType.GREATER_THAN, "Se esperaba '>' para cierre de declaración del tipo de lista.");
            return t;
        } else if (tokenStream.match(TokenType.LIST)) {
            TypeList typeList = new TypeList(parseListType());
            tokenStream.consume(TokenType.GREATER_THAN, "Se esperaba '>' para cierre de declaración del tipo de lista.");
            return typeList;
        }
        throw new RuntimeException("Se esperaba un tipo primitivo o una lista anidada para la declaración del tipo de lista.");
    }
}
