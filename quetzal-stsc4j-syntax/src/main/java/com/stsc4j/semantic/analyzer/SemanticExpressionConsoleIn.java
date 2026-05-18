package com.stsc4j.semantic.analyzer;

import com.stsc4j.semantic.SemanticAbstractAnalyzer;
import com.stsc4j.semantic.SemanticAnalyzer;

public class SemanticExpressionConsoleIn extends SemanticAbstractAnalyzer {

    private final SemanticAnalyzer analyzer;

    public SemanticExpressionConsoleIn (SemanticAnalyzer analyzer) {
        this.analyzer = analyzer;
    }

}
