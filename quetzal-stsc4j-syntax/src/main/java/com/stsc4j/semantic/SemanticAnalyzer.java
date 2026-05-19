package com.stsc4j.semantic;

import com.stsc4j.parser.v1.ast.*;
import com.stsc4j.semantic.analyzer.*;

import java.util.List;

//Cambio importante: Ahora el visitor devuelve String para que me devuelva el tipo de dato
public class SemanticAnalyzer implements Visitor<String> {

    // Nuestra memoria que guarda las variables que vamos encontrando
    private Environment currentEnv = new Environment();

    private final SemanticExpressionVariable semanticExpressionVariable = new SemanticExpressionVariable(this);
    private final SemanticStatementVariable semanticStatementVariable = new SemanticStatementVariable(this);
    private final SemanticStatementBlock semanticStatementBlock = new SemanticStatementBlock(this);
    private final SemanticStatementIf semanticStatementIf = new SemanticStatementIf(currentEnv, this);
    private final SemanticStatementList semanticStatementList = new SemanticStatementList(this);
    private final SemanticStatementJsn semanticStatementJsn = new SemanticStatementJsn(this);
    private final SemanticStatementFunctionParameter semanticStatementFunctionParameter = new SemanticStatementFunctionParameter(this);
    private final SemanticStatementFunction semanticStatementFunction = new SemanticStatementFunction(this);
    private final SemanticStatementReturn semanticStatementReturn = new SemanticStatementReturn(this);
    private final SemanticStatementLoopWhile semanticStatementLoopWhile = new SemanticStatementLoopWhile(currentEnv, this);
    private final SemanticStatementLoopDoWhile semanticStatementLoopDoWhile = new SemanticStatementLoopDoWhile(this);
    private final SemanticStatementLoopFor semanticStatementLoopFor = new SemanticStatementLoopFor(this);
    private final SemanticExpressionBinary semanticExpressionBinary = new SemanticExpressionBinary(currentEnv, this);
    private final SemanticExpressionLiteral semanticExpressionLiteral = new SemanticExpressionLiteral(this);
    private final SemanticExpressionTernary semanticExpressionTernary = new SemanticExpressionTernary(this);
    private final SemanticExpressionMethodCall semanticExpressionMethodCall = new SemanticExpressionMethodCall(this);
    private final SemanticExpressionIndexAccess semanticExpressionIndexAccess = new SemanticExpressionIndexAccess(this);
    private final SemanticExpressionList semanticExpressionList = new SemanticExpressionList(this);
    private final SemanticExpressionJsnBlock semanticExpressionJsnBlock = new SemanticExpressionJsnBlock(this);
    private final SemanticExpressionJsn semanticExpressionJsn = new SemanticExpressionJsn(this);
    private final SemanticExpressionIncDec semanticExpressionIncDec = new SemanticExpressionIncDec(this);
    private final SemanticStatementLoopForEach semanticStatementLoopForEach = new SemanticStatementLoopForEach(this);
    private final SemanticStatementIncDec semanticStatementIncDec = new SemanticStatementIncDec(this);
    private final SemanticExpressionPropertyAccess semanticExpressionPropertyAccess = new SemanticExpressionPropertyAccess(currentEnv, this);
    private final SemanticExpressionForEachVar semanticExpressionForEachVar = new SemanticExpressionForEachVar(this);
    private final SemanticStatementConsoleOut semanticStatementConsoleOut = new SemanticStatementConsoleOut(this);
    private final SemanticTypeList semanticTypeList = new SemanticTypeList(this);
    private final SemanticTypePrimitive semanticTypePrimitive = new SemanticTypePrimitive(this);
    private final SemanticStatementMatrxiAssignation semanticStatementMatrxiAssignation = new SemanticStatementMatrxiAssignation(this);
    private final SemanticExpressionNull semanticExpressionNull = new SemanticExpressionNull(this);
    private final SemanticStatementTryCatchFinally semanticStatementTryCatchFinally = new SemanticStatementTryCatchFinally(this);
    private final SemanticStatementContinue semanticStatementContinue = new SemanticStatementContinue(this);
    private final SemanticStatementBreak semanticStatementBreak = new SemanticStatementBreak(this);
    private final SemanticStatementThrow semanticStatementThrow = new SemanticStatementThrow(this);
    private final SemanticExpressionConsoleIn semanticExpressionConsoleIn = new SemanticExpressionConsoleIn(this);
    private final SemanticStatementPropertyAssignation semanticStatementPropertyAssignation = new SemanticStatementPropertyAssignation(this);



    // Metod para iniciar a leer las sentencias
    public void analyze(List<Statement> statements) {
        for (Statement stmt : statements) {
            stmt.accept(this);
        }
    }

    // SENTENCIAS

    /*MODIFICO EL VISITOR DE STATEMENT VARIABLE PARA
    QUE ME DEVUELVA EL TIPO DE DATO REAL DE LA
    EXPRESION Y ASI PODER COMPARARLO CON EL TIPO
    DE DATO ESPERADO DE LA VARIABLE
     */
    @Override
    public String visit(StatementVariable statementVariable) {
        return semanticStatementVariable.visit(statementVariable);
    }

    @Override
    public String visit(StatementBlock statementBlock) {
        return semanticStatementBlock.visit(statementBlock);
    }

    //Verificacion que el if sigue la estructura correcta, que la condicion sea una expresion booleana y que las sentencias then y else sean validas
    @Override
    public String visit(StatementIf statementIf) {
        return semanticStatementIf.visit(statementIf);
    }

    //No lo he implementado porque no tengo claro que tipo de dato deberia devolver un statement expression, pero lo dejo preparado por si quiero agregarlo despues
    @Override
    public String visit(StatementExpression statementExpression) {
        return statementExpression.expression.accept(this);
    }

    @Override
    public String visit(StatementList statementList) {
        return semanticStatementList.visit(statementList);
    }

    @Override
    public String visit(TypeList typeList) {
        return semanticTypeList.visit(typeList);
    }

    @Override
    public String visit(TypePrimitive type) {
        return semanticTypePrimitive.visit(type);
    }

    @Override
    public String visit(StatementJsn statementJsn) {
        return semanticStatementJsn.visit(statementJsn);
    }

    @Override
    public String visit(StatementFunctionParameter statementFunctionParameter) {
        return semanticStatementFunctionParameter.visit(statementFunctionParameter);
    }

    @Override
    public String visit(StatementFunction statementFunction) {
        return semanticStatementFunction.visit(statementFunction);
    }

    @Override
    public String visit(StatementReturn statementReturn) {
        return semanticStatementReturn.visit(statementReturn);
    }

    @Override
    public String visit(StatementLoopWhile statementLoopWhile) {
        return semanticStatementLoopWhile.visit(statementLoopWhile);
    }

    @Override
    public String visit(StatementLoopDoWhile statementLoopDoWhile) {
        return semanticStatementLoopDoWhile.visit(statementLoopDoWhile);
    }

    @Override
    public String visit(StatementLoopFor statementLoopFor) {
        return semanticStatementLoopFor.visit(statementLoopFor);
    }

    @Override
    public String visit(StatementLoopForEach statementLoopForEach) {
        return semanticStatementLoopForEach.visit(statementLoopForEach);
    }

    @Override
    public String visit(StatementIncDec statementIncDec) {
        return semanticStatementIncDec.visit(statementIncDec);
    }

    @Override
    public String visit(StatementMatrixAssignation statementMatrixAssignation) {
        return semanticStatementMatrxiAssignation.visit(statementMatrixAssignation);
    }

    @Override
    public String visit(StatementPropertyAssignation statementPropertyAssignation) {
        return semanticStatementPropertyAssignation.visit(statementPropertyAssignation);
    }


    @Override
    public String visit(ExpressionNull expressionNull) {
        return semanticExpressionNull.visit(expressionNull);
    }

    @Override
    public String visit(StatementTryCatchFinally statementTryCatchFinally) {
        return semanticStatementTryCatchFinally.visit(statementTryCatchFinally);
    }

    @Override
    public String visit(StatementConsolaOut statementConsolaOut) {
        return semanticStatementConsoleOut.visit(statementConsolaOut);
    }

    @Override
    public String visit(StatementContinue statementContinue) {
        return semanticStatementContinue.visit(statementContinue);
    }

    @Override
    public String visit(StatementBreak statementBreak) {
        return semanticStatementBreak.visit(statementBreak);
    }

    @Override
    public String visit(StatementThrow statementThrow) {
        return semanticStatementThrow.visit(statementThrow);
    }

    @Override
    public String visit(ExpressionConsoleIn expressionConsoleIn) {
        return semanticExpressionConsoleIn.visit(expressionConsoleIn);
    }

    // EXPRESIONES

    @Override
    public String visit(ExpressionVariable expressionVariable) {
        return semanticExpressionVariable.visit(expressionVariable);
    }

    @Override
    public String visit(ExpressionBinary expressionBinary) {
        return semanticExpressionBinary.visit(expressionBinary);
    }

    @Override
    public String visit(ExpressionLiteral expressionLiteral) {
        return semanticExpressionLiteral.visit(expressionLiteral);
    }

    @Override
    public String visit(ExpressionTernary expressionTernary) {
        return semanticExpressionTernary.visit(expressionTernary);
    }

    @Override
    public String visit(ExpressionMethodCall expressionMethodCall) {
        return semanticExpressionMethodCall.visit(expressionMethodCall);
    }

    @Override
    public String visit(ExpressionIndexAccess expressionIndexAccess) {
        return semanticExpressionIndexAccess.visit(expressionIndexAccess);
    }

    @Override
    public String visit(ExpressionList expressionList) {
        return semanticExpressionList.visit(expressionList);
    }

    @Override
    public String visit(ExpressionJsnBlock expressionJsnBlock) {
        return semanticExpressionJsnBlock.visit(expressionJsnBlock);
    }

    @Override
    public String visit(ExpressionJsn expressionJsn) {
        return semanticExpressionJsn.visit(expressionJsn);
    }

    @Override
    public String visit(ExpressionIncDec expressionIncDec) {
        return semanticExpressionIncDec.visit(expressionIncDec);
    }

    @Override
    public String visit(ExpressionPropertyAccess expressionPropertyAccess) {
        return semanticExpressionPropertyAccess.visit(expressionPropertyAccess);
    }

    @Override
    public String visit(ExpressionForEachVar expressionForEachVar) {
        return semanticExpressionForEachVar.visit(expressionForEachVar);
    }

    // Permite leer el entorno actual
    public Environment getEnv() {
        return this.currentEnv;
    }

    // Permite cambiar el entorno (ideal para entrar y salir de bloques { })
    public void setEnv(Environment env) {
        this.currentEnv = env;
    }

    private int loopDepth = 0; // Nivel de profundidad de los ciclos anidados

    public void enterLoop() {
        this.loopDepth++;
    }

    public void exitLoop() {
        this.loopDepth--;
    }

    public boolean isInLoop() {
        return this.loopDepth > 0;
    }

}