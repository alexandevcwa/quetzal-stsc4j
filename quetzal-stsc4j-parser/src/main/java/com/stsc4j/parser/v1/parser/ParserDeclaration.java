package com.stsc4j.parser.v1.parser;

import com.stsc4j.lexer.Token;
import com.stsc4j.lexer.TokenType;
import com.stsc4j.parser.v1.ast.*;

public class ParserDeclaration {
    private final TokenStream tokenStream;
    private final ParserExpressions parserExpressions;

    public ParserDeclaration(TokenStream tokenStream, ParserExpressions parserExpressions) {
        this.tokenStream = tokenStream;
        this.parserExpressions = parserExpressions;
    }

    public Statement parseVarDeclaration() {
        // Tipo de dato
        Token type = tokenStream.before();

        // Variable mutable o inmutable
        boolean isMutable = false;

        // Nombre de la variable
        Token name = null;

        // Valor inicial de la variable
        Expression initialValue = null;

        // Asignar nuevo valor a una variable ya declarada
        if (type.getType().equals(TokenType.IDENTIFIER)) {
            tokenStream.match(TokenType.EQUAL, "Se esperaba '=' después del nombre de la variable o luego de 'var'.");
            name = type;
        } else {
            // Nueva variable declarada
            isMutable = tokenStream.match(TokenType.MUTABLE_VARIABLE);
            name = tokenStream.consume(TokenType.IDENTIFIER, "Se esperaba el nombre de la variable.");
            tokenStream.match(TokenType.EQUAL, "Se esperaba '=' después del nombre de la variable o luego de 'var'.");
        }

        initialValue = parserExpressions.parseExpression();

        Expression ternary = null;
        // Verificar operador ternario
        if (tokenStream.matchNotAdvance(TokenType.QUESTION)) {
            tokenStream.advance();
            ternary = parserExpressions.parseTernaryExpression((ExpressionBinary) initialValue);
        }

        return new StatementVariable(type, isMutable, name,
                ternary != null ? ternary : initialValue
        );
    }
}
