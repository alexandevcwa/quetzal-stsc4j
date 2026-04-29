package com.stsc4j.parser.v1.ast;

public interface Visitor<T> {

    T visit(ExpressionVariable expressionVariable);

    T visit(ExpressionLiteral expressionLiteral);

    T visit(ExpressionBinary expressionBinary);

    T visit(ExpressionTernary expressionTernary);

    T visit(ExpressionMethodCall expressionMethodCall);

    T visit(ExpressionIndexAccess expressionIndexAccess);

    T visit(ExpressionList expressionList);

    T visit(ExpressionJsnBlock expressionJsnBlock);

    T visit(ExpressionJsn expressionJsn);

    T visit(ExpressionPropertyAccess expressionPropertyAccess);

    T visit(StatementIf statementIf);

    T visit(StatementBlock statementBlock);

    T visit(StatementExpression statementExpression);

    T visit(StatementVariable statementVariable);

    T visit(StatementList statementList);

    T visit(TypeList typeList);

    T visit(TypePrimitive type);

    T visit(StatementJsn statementJsn);
    T visit(StatementFunction statementFunction);
    T visit(StatementFunctionParameter statementFunctionParameter);
    T visit(StatementReturn statementReturn);
    T visit(StatementLoopWhile statementLoopWhile);
    T visit(StatementLoopDoWhile statementLoopDoWhile);
}
