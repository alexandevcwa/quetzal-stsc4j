package com.stsc4j.parser.v1.parser;

import com.stsc4j.lexer.Token;
import com.stsc4j.lexer.TokenType;
import com.stsc4j.parser.v1.ast.*;

public class ParserList extends Parser {

    private final TokenStream tokenStream;
    private final ParserExpression parserExpression;

    public ParserList(TokenStream tokenStream, ParserExpression parserExpression) {
        this.tokenStream = tokenStream;
        this.parserExpression = parserExpression;
    }

    @Override
    public Statement parseStatement() {
        tokenStream.consume(TokenType.LIST, "Se esperaba 'lista'");

        // Verificar si la lista tiene tipo especificado (lista<tipo>) o sin tipo (lista)
        TypeList typeList = null;
        if (tokenStream.matchNotAdvance(TokenType.LESS_THAN)) {
            typeList = (TypeList) parseListType();
        }

        boolean isMutable = tokenStream.match(TokenType.MUTABLE_VARIABLE);
        Token identified = tokenStream.consume(TokenType.IDENTIFIER, "Se esperaba el identificador de la lista.");
        tokenStream.consume(TokenType.EQUAL, "Se esperaba '=' después del identificador de la lista.");

        short depth = typeList != null ? listDepth(typeList) : 0;

        // Usar la función recursiva para parsear listas multidimensionales
        ExpressionList expressionList = (ExpressionList) ParserListExpression.builder(tokenStream, parserExpression)
                .depth(depth).build()
                .parseExpression();

        return new StatementList(typeList, isMutable, identified, expressionList);
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
