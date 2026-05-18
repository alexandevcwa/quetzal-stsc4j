package com.stsc4j.semantic.analyzer;

import com.stsc4j.semantic.Environment;
import com.stsc4j.semantic.SemanticAbstractAnalyzer;

public class SemanticStatementContinue extends SemanticAbstractAnalyzer {

    private final Environment currentEnv;

    public SemanticStatementContinue (Environment currentEnv) {
        this.currentEnv = currentEnv;
    }


}
