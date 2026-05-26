package com.stsc4j.parser.v1.parser;

import com.stsc4j.lexer.TokenType;
import com.stsc4j.parser.v1.ast.Expression;
import com.stsc4j.parser.v1.ast.ExpressionList;

import java.util.ArrayList;
import java.util.List;

/**
 * Parseo de expresiones de lista, por ejemplo: [1, 2, 3] o [[1, 2], [3, 4]],
 * soporta listas anidadas de cualquier profundidad.
 * La profundidad se detecta automáticamente durante el parseo.
 */
public class ParserListExpression extends Parser {

    private final ParserExpression parserExpression;
    private final TokenStream tokenStream;

    private ParserListExpression(TokenStream tokenStream, ParserExpression parserExpression) {
        this.tokenStream = tokenStream;
        this.parserExpression = parserExpression;
    }

    /**
     * Builder para construir un ParserListExpression
     */
    public static ParserListExpressionBuilder builder(TokenStream tokenStream, ParserExpression parserExpression) {
        return new ParserListExpressionBuilder(tokenStream, parserExpression);
    }

    public static class ParserListExpressionBuilder {
        private final ParserListExpression parserListExpression;

        protected ParserListExpressionBuilder(TokenStream tokenStream, ParserExpression parserExpression) {
            this.parserListExpression = new ParserListExpression(tokenStream, parserExpression);
        }


        public ParserListExpression build() {
            return parserListExpression;
        }
    }


    @Override
    public Expression parseExpression() {
        return parse();
    }

    /**
     * Parsea una lista detectando automáticamente la profundidad de anidamiento.
     * La profundidad se determina por el tipo del primer elemento:
     * - Si el primer elemento es una expresión primitiva → profundidad = 0
     * - Si el primer elemento es una lista de profundidad X → profundidad = X + 1
     */
    private Expression parse() {
        tokenStream.consume(TokenType.BRACKETS_OPEN, "Se esperaba '[' para iniciar la declaración de la lista.");

        List<Expression> elements = new ArrayList<>();
        boolean isFirstElement = true;
        short depth = -1; // Iniciar en -1 para detectar la profundidad del primer elemento

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

            // Detectar dinámicamente si el próximo elemento es una lista anidada o una expresión primitiva
            if (tokenStream.matchNotAdvance(TokenType.BRACKETS_OPEN)) {
                // Llamada recursiva: parsear sub-lista
                element = parse();
                // El primer elemento es una sub-lista, la profundidad es su profundidad + 1
                if (depth == -1) {
                    depth = (short) (((ExpressionList) element).depth + 1);
                }
            } else {
                // Caso base: parsear expresión primitiva
                element = parserExpression.parseExpression();
                // El primer elemento es primitivo, la profundidad es 0
                if (depth == -1) {
                    depth = 0;
                }
            }

            elements.add(element);
        }


        // Crear y retornar la ExpressionList con los elementos parseados
        return new ExpressionList(elements, depth);
    }
}
