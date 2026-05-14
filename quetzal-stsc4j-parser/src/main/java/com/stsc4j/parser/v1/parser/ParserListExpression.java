package com.stsc4j.parser.v1.parser;

import com.stsc4j.lexer.TokenType;
import com.stsc4j.parser.v1.ast.Expression;
import com.stsc4j.parser.v1.ast.ExpressionList;

import java.util.ArrayList;
import java.util.List;

/**
 * Parseo de expresiones de lista, por ejemplo: [1, 2, 3] o [[1, 2], [3, 4]],
 * soporta listas anidadas de cualquier profundidad.
 * La profundidad se controla mediante el atributo 'depth', que se establece al construir el parser.
 * Si depth > 0, se esperan listas anidadas; si depth == 0
 */
public class ParserListExpression extends Parser {

    private final ParserExpression parserExpression;
    private final TokenStream tokenStream;

    /**
     * Profundidad de la lista, 0 significa que no hay listas anidadas.
     */
    private short depth = 0;

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

        public ParserListExpressionBuilder depth(short depth) {
            parserListExpression.depth = depth;
            return this;
        }

        public ParserListExpression build() {
            return parserListExpression;
        }
    }


    @Override
    public Expression parseExpression() {
        return parse(depth);
    }

    private Expression parse(short i) {
        tokenStream.consume(TokenType.BRACKETS_OPEN, "Se esperaba '[' para iniciar la declaración de la lista.");

        List<Expression> elements = new ArrayList<>();
        boolean isFirstElement = true;

        // Bucle principal para parsear elementos de la lista actual
        //TODO: Mejorar el manejo del bucle
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
            if (i > 0) {
                // Llamada recursiva: parsear sub-lista
                element = parse((short) (i - 1));
            } else {
                // Caso base: parsear expresión primitiva
                element = parserExpression.parseExpression();
            }

            elements.add(element);
        }

        // Crear y retornar la ExpressionList con los elementos parseados
        ExpressionList expressionList = new ExpressionList();
        expressionList.expressions = elements;
        return expressionList;
    }
}
