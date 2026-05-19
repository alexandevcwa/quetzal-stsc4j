package com.stsc4j.generator;

import com.stsc4j.parser.v1.ast.*;

public abstract class BytecodeAbstractGenerator implements Visitor<String> {

    @Override public String visit(ExpressionLiteral expr) { return null; }
    @Override public String visit(ExpressionVariable expr) { return null; }
    @Override public String visit(ExpressionBinary expr) { return null; }
    @Override public String visit(StatementVariable stmt) { return null; }
    @Override public String visit(StatementConsolaOut stmt) { return null; }
    @Override public String visit(StatementBlock stmt) { return null; }
    @Override public String visit(StatementIf stmt) { return null; }
    @Override public String visit(StatementExpression stmtExpr) { return null; }
    @Override public String visit(StatementList stmt) { return null; }
    @Override public String visit(TypeList stmt) { return null; }
    @Override public String visit(TypePrimitive stmt) { return null; }
    @Override public String visit(StatementJsn stmt) { return null; }
    @Override public String visit(StatementFunctionParameter stmt) { return null; }
    @Override public String visit(StatementFunction stmt) { return null; }
    @Override public String visit(StatementReturn stmt) { return null; }
    @Override public String visit(StatementLoopWhile stmt) { return null; }
    @Override public String visit(StatementLoopDoWhile stmt) { return null; }
    @Override public String visit(StatementLoopFor stmt) { return null; }
    @Override public String visit(StatementLoopForEach stmt) { return null; }
    @Override public String visit(StatementIncDec stmt) { return null; }
    @Override public String visit(StatementMatrixAssignation stmt) { return null; }
    @Override public String visit(StatementPropertyAssignation stmt) { return null; }
    @Override public String visit(ExpressionNull expr) { return null; }
    @Override public String visit(StatementTryCatchFinally stmt) { return null; }
    @Override public String visit(StatementContinue stmt) { return null; }
    @Override public String visit(StatementBreak stmt) { return null; }
    @Override public String visit(StatementThrow stmt) { return null; }
    @Override public String visit(ExpressionConsoleIn expr) { return null; }
    @Override public String visit(ExpressionTernary expr) { return null; }
    @Override public String visit(ExpressionMethodCall expr) { return null; }
    @Override public String visit(ExpressionIndexAccess expr) { return null; }
    @Override public String visit(ExpressionList expr) { return null; }
    @Override public String visit(ExpressionJsnBlock expr) { return null; }
    @Override public String visit(ExpressionJsn expr) { return null; }
    @Override public String visit(ExpressionPropertyAccess expr) { return null; }
    @Override public String visit(ExpressionIncDec expr) { return null; }
    @Override public String visit(ExpressionForEachVar expr) { return null; }
}