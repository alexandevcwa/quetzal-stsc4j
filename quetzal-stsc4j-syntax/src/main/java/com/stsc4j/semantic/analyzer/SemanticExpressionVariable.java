package com.stsc4j.semantic.analyzer;

import com.stsc4j.parser.v1.ast.ExpressionVariable;
import com.stsc4j.semantic.Environment;
import com.stsc4j.semantic.SemanticAbstractAnalyzer;
import com.stsc4j.semantic.SemanticAnalyzer;

public class SemanticExpressionVariable extends SemanticAbstractAnalyzer {

    private final Environment currentEnv;
    private final SemanticAnalyzer analyzer;

    public SemanticExpressionVariable(Environment currentEnv, SemanticAnalyzer analyzer) {
        this.currentEnv = currentEnv;
        this.analyzer = analyzer;
    }

    @Override
    public String visit (ExpressionVariable expressionVariable) {
        // Sacamos el nombre y buscamos en memoria que tipo de dato es
        String varName = expressionVariable.token.getLexeme();
        return currentEnv.resolveType(varName); // Lanza SemanticError si no existe
    }
}
