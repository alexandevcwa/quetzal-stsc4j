package com.stsc4j.parser.v1.parser;

import com.stsc4j.lexer.Token;
import com.stsc4j.lexer.TokenType;
import com.stsc4j.parser.v1.ast.*;
import com.stsc4j.parser.v1.parser.expression.ParseGenericExpression;

public class ParserDeclaration {
    private final TokenStream tokenStream;
    private final ParserExpressions parserExpressions;

    public ParserDeclaration(TokenStream tokenStream, ParserExpressions parserExpressions) {
        this.tokenStream = tokenStream;
        this.parserExpressions = parserExpressions;
    }

    public Statement parseVarDeclaration() {
        Token type = tokenStream.before();
        boolean isMutable = false;
        Token name = null;

        Expression initialValue = null;

        if (type.getType().equals(TokenType.IDENTIFIER)) {
            tokenStream.match(TokenType.EQUAL, "Se esperaba '=' después del nombre de la variable o luego de 'var'.");
            name = type;
        } else {
            isMutable = tokenStream.match(TokenType.MUTABLE_VARIABLE);
            name = tokenStream.consume(TokenType.IDENTIFIER, "Se esperaba el nombre de la variable.");
            tokenStream.match(TokenType.EQUAL, "Se esperaba '=' después del nombre de la variable o luego de 'var'.");
        }
        initialValue = parserExpressions.parseGenericExpression();

        Expression ternary = null;
        if (tokenStream.matchButNotAdvance(TokenType.QUESTION)) {
            tokenStream.advance();
            ternary = parserExpressions.parseTernaryExpression((ExpressionBinary) initialValue);
        }


        return new StatementVariable(type, isMutable, name,
                ternary != null ? ternary : initialValue
        );
    }
}
