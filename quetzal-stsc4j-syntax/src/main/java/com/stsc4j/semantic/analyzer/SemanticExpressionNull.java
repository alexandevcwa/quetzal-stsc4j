package com.stsc4j.semantic.analyzer;

import com.stsc4j.semantic.Environment;
import com.stsc4j.semantic.SemanticAbstractAnalyzer;

public class SemanticExpressionNull extends SemanticAbstractAnalyzer {

    private final Environment currentEnv;

    public SemanticExpressionNull (Environment currentEnv) {
        this.currentEnv = currentEnv;
    }



}
