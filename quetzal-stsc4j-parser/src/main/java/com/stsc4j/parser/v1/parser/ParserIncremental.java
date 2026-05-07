package com.stsc4j.parser.v1.parser;

import com.stsc4j.parser.v1.ast.Expression;
import com.stsc4j.parser.v1.ast.ExpressionIncDec;
import com.stsc4j.parser.v1.ast.Statement;
import com.stsc4j.parser.v1.ast.StatementIncDec;
import com.stsc4j.parser.v1.exception.ParserException;

public class ParserIncremental extends Parser {
    private final ParserExpressions parserExpressions;

    public ParserIncremental(ParserExpressions parserExpressions) {
        this.parserExpressions = parserExpressions;
    }

    @Override
    public Statement parseStatement() {
        Expression expression = parserExpressions.parseExpression();
        if (expression instanceof ExpressionIncDec) {
            return new StatementIncDec((ExpressionIncDec) expression);
        }
        throw new ParserException("Se esperaba una expresión de incremento o decremento");
    }
}
