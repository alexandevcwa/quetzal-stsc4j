package com.stsc4j.semantic;

import com.stsc4j.parser.v1.ast.*;
import com.stsc4j.semantic.analyzer.*;

import java.util.List;

//Cambio importante: Ahora el visitor devuelve String para que me devuelva el tipo de dato
public class SemanticAnalyzer implements Visitor<String> {

    // Nuestra memoria que guarda las variables que vamos encontrando
    private Environment currentEnv = new Environment();

    private final SemanticExpressionVariable semanticExpressionVariable = new SemanticExpressionVariable(currentEnv, this);
    private final SemanticStatementVariable semanticStatementVariable = new SemanticStatementVariable(currentEnv, this);
    private final SemanticStatementBlock semanticStatementBlock = new SemanticStatementBlock(currentEnv);
    private final SemanticStatementIf semanticStatementIf = new SemanticStatementIf(currentEnv);
    private final SemanticStatementList semanticStatementList = new SemanticStatementList(currentEnv);
    private final SemanticStatementJsn semanticStatementJsn = new SemanticStatementJsn(currentEnv);
    private final SemanticStatementFunctionParameter semanticStatementFunctionParameter = new SemanticStatementFunctionParameter(currentEnv);
    private final SemanticStatementFunction semanticStatementFunction = new SemanticStatementFunction(currentEnv);
    private final SemanticStatementReturn semanticStatementReturn = new SemanticStatementReturn(currentEnv);
    private final SemanticStatementLoopWhile semanticStatementLoopWhile = new SemanticStatementLoopWhile(currentEnv);
    private final SemanticStatementLoopDoWhile semanticStatementLoopDoWhile = new SemanticStatementLoopDoWhile(currentEnv);
    private final SemanticStatementLoopFor semanticStatementLoopFor = new SemanticStatementLoopFor(currentEnv);
    private final SemanticExpressionBinary semanticExpressionBinary = new SemanticExpressionBinary(currentEnv, this);
    private final SemanticExpressionLiteral semanticExpressionLiteral = new SemanticExpressionLiteral(currentEnv);
    private final SemanticExpressionTernary semanticExpressionTernary = new SemanticExpressionTernary(currentEnv);
    private final SemanticExpressionMethodCall semanticExpressionMethodCall = new SemanticExpressionMethodCall(currentEnv);
    private final SemanticExpressionIndexAccess semanticExpressionIndexAccess = new SemanticExpressionIndexAccess(currentEnv);
    private final SemanticExpressionList semanticExpressionList = new SemanticExpressionList(currentEnv);
    private final SemanticExpressionJsnBlock semanticExpressionJsnBlock = new SemanticExpressionJsnBlock(currentEnv);
    private final SemanticExpressionJsn semanticExpressionJsn = new SemanticExpressionJsn(currentEnv);
    private final SemanticExpressionIncDec semanticExpressionIncDec = new SemanticExpressionIncDec(currentEnv);
    private final SemanticStatementLoopForEach semanticStatementLoopForEach = new SemanticStatementLoopForEach(currentEnv, this);
    private final SemanticStatementIncDec semanticStatementIncDec = new SemanticStatementIncDec(currentEnv, this);
    private final SemanticExpressionPropertyAccess semanticExpressionPropertyAccess = new SemanticExpressionPropertyAccess(currentEnv, this);
    private final SemanticExpressionForEachVar semanticExpressionForEachVar = new SemanticExpressionForEachVar(currentEnv, this);



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
        return "";
    }

    @Override
    public String visit(TypePrimitive type) {
        return "";
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
}