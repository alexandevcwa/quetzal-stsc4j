package com.stsc4j.semantic.analyzer;

import com.stsc4j.semantic.Environment;
import com.stsc4j.semantic.SemanticAbstractAnalyzer;

public class SemanticStatementTryCatchFinally extends SemanticAbstractAnalyzer {

    private final Environment currentEnv;

    public SemanticStatementTryCatchFinally (Environment currentEnv) {
        this.currentEnv = currentEnv;
    }
}
