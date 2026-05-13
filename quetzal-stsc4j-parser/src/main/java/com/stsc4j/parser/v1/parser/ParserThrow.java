package com.stsc4j.parser.v1.parser;

import com.stsc4j.lexer.TokenType;
import com.stsc4j.parser.v1.ast.Expression;
import com.stsc4j.parser.v1.ast.ExpressionLiteral;
import com.stsc4j.parser.v1.ast.Statement;
import com.stsc4j.parser.v1.ast.StatementThrow;
import com.stsc4j.parser.v1.exception.ParserException;

public class ParserThrow extends Parser {

    private final TokenStream tokenStream;
    private final ParserExpressions parserExpressions;

    public ParserThrow(TokenStream tokenStream, ParserExpressions parserExpressions) {
        this.tokenStream = tokenStream;
        this.parserExpressions = parserExpressions;
    }

    @Override
    public Statement parseStatement() {
        tokenStream.consume(TokenType.THROW, "Se esperaba 'lanzar'");
        Expression message = parserExpressions.parseExpression();
        if (message instanceof ExpressionLiteral) {
            ExpressionLiteral literal = (ExpressionLiteral) message;
            if (literal.token.type.equals(TokenType.LIT_STRING)) {
                return new StatementThrow(literal);
            } else {
                throw new ParserException("Se esperaba un literal de tipo string en el mensaje del 'lanzar'");
            }
        }
        throw new ParserException("Se esperaba un literal de tipo string en el mensaje del 'lanzar'");
    }
}
