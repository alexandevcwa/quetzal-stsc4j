package com.stsc4j.semantic;

import com.stsc4j.parser.v1.ast.*;

public abstract class SemanticAbstractAnalyzer implements Visitor <String> {

    @Override
    public String visit(ExpressionVariable expressionVariable) {
        return null;
    }

    @Override
    public String visit(ExpressionLiteral expressionLiteral) {
        return null;
    }

    @Override
    public String visit(ExpressionBinary expressionBinary) {
        return null;
    }

    @Override
    public String visit(ExpressionTernary expressionTernary) {
        return null;
    }

    @Override
    public String visit(ExpressionMethodCall expressionMethodCall) {
        return null;
    }

    @Override
    public String visit(ExpressionIndexAccess expressionIndexAccess) {
        return null;
    }

    @Override
    public String visit(ExpressionList expressionList) {
        return null;
    }

    @Override
    public String visit(ExpressionJsnBlock expressionJsnBlock) {
        return null;
    }

    @Override
    public String visit(ExpressionJsn expressionJsn) {
        return null;
    }

    @Override
    public String visit(ExpressionPropertyAccess expressionPropertyAccess) {
        return null;
    }

    @Override
    public String visit(ExpressionIncDec expressionIncDec) {
        return null;
    }

    @Override
    public String visit(ExpressionForEachVar expressionForEachVar) {
        return null;
    }
    @Override
    public String visit(StatementIf statementIf) {
        return null;
    }

    @Override
    public String visit(StatementBlock statementBlock) {
        return null;
    }

    @Override
    public String visit(StatementExpression statementExpression) {
        return null;
    }

    @Override
    public String visit(StatementVariable statementVariable) {
        return null;
    }

    @Override
    public String visit(StatementList statementList) {
        return null;
    }

    @Override
    public String visit(TypeList typeList) {
        return null;
    }

    @Override
    public String visit(TypePrimitive type) {
        return null;
    }

    @Override
    public String visit(StatementJsn statementJsn) {
        return null;
    }

    @Override
    public String visit(StatementFunction statementFunction) {
        return null;
    }

    @Override
    public String visit(StatementFunctionParameter statementFunctionParameter) {
        return null;
    }

    @Override
    public String visit(StatementReturn statementReturn) {
        return null;
    }

    @Override
    public String visit(StatementLoopWhile statementLoopWhile) {
        return null;
    }

    @Override
    public String visit(StatementLoopDoWhile statementLoopDoWhile) {
        return null;
    }

    @Override
    public String visit(StatementLoopFor statementLoopFor) {
        return null;
    }

    @Override
    public String visit(StatementLoopForEach statementLoopForEach) {
        return null;
    }
    @Override
    public String visit(StatementIncDec statementIncDec) {
        return null;
    }

    @Override
    public String visit(StatementMatrixAssignation statementMatrixAssignation) {
        return null;
    }

    @Override
    public String visit(ExpressionNull expressionNull) {
        return null;
    }

    @Override
    public String visit(StatementTryCatchFinally statementTryCatchFinally) {
        return null;
    }

    @Override
    public String visit(StatementConsolaOut statementConsolaOut) {
        return null;
    }

    @Override
    public String visit(StatementContinue statementContinue) {
        return null;
    }

    @Override
    public String visit(StatementBreak statementBreak) {
        return null;
    }

    @Override
    public String visit(StatementThrow statementThrow) {
        return null;
    }

    @Override
    public String visit(ExpressionConsoleIn expressionConsoleIn) {
        return null;
    }
}
