package com.stsc4j.semantic.analyzer;

import com.stsc4j.semantic.Environment;
import com.stsc4j.semantic.SemanticAbstractAnalyzer;

public class SemanticStatementThrow extends SemanticAbstractAnalyzer {

    private final Environment currentEnv;

    public SemanticStatementThrow (Environment currentEnv) {
        this.currentEnv = currentEnv;
    }
}
