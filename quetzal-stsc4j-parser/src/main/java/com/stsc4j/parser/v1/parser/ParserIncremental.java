package com.stsc4j.parser.v1.parser;

import com.stsc4j.parser.v1.ast.Expression;
import com.stsc4j.parser.v1.ast.ExpressionIncDec;
import com.stsc4j.parser.v1.ast.Statement;
import com.stsc4j.parser.v1.ast.StatementIncDec;
import com.stsc4j.parser.v1.exception.ParserException;

public class ParserIncremental extends Parser {
    private final ParserExpression parserExpression;

    public ParserIncremental(ParserExpression parserExpression) {
        this.parserExpression = parserExpression;
    }

    @Override
    public Statement parseStatement() {
        Expression expression = parserExpression.parseExpression();
        if (expression instanceof ExpressionIncDec) {
            return new StatementIncDec((ExpressionIncDec) expression);
        }
        throw new ParserException("Se esperaba una expresión de incremento o decremento");
    }
}
