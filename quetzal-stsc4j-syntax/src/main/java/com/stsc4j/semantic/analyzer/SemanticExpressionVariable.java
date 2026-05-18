package com.stsc4j.semantic.analyzer;

import com.stsc4j.parser.v1.ast.ExpressionVariable;
import com.stsc4j.semantic.SemanticAbstractAnalyzer;
import com.stsc4j.semantic.SemanticAnalyzer;

public class SemanticExpressionVariable extends SemanticAbstractAnalyzer {

    private final SemanticAnalyzer analyzer;

    public SemanticExpressionVariable(SemanticAnalyzer analyzer) {
        this.analyzer = analyzer;
    }

    @Override
    public String visit (ExpressionVariable expressionVariable) {
        String varName = expressionVariable.token.getLexeme();
        // Le pedimos la memoria actual al director
        return analyzer.getEnv().resolveType(varName);
    }
}