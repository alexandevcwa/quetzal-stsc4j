package com.stsc4j.parser.v1.ast;

public interface Visitor <T>{

    T visit(ExpressionVariable expressionVariable);
    T visit(ExpressionLiteral expressionLiteral);
    T visit(ExpressionBinary expressionBinary);
    T visit(ExpressionTernary expressionTernary);

    T visit(StatementIf statementIf);
    T visit(StatementBlock statementBlock);
    T visit(StatementExpression statementExpression);

    T visit(StatementVariable statementVariable);
    T visit(StatementList statementList);
}
