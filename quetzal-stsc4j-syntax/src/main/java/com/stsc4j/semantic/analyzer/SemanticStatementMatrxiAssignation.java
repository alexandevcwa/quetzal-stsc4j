package com.stsc4j.semantic.analyzer;

import com.stsc4j.semantic.Environment;
import com.stsc4j.semantic.SemanticAbstractAnalyzer;

public class SemanticStatementMatrxiAssignation extends SemanticAbstractAnalyzer {

    private final Environment currentEnv;

    public SemanticStatementMatrxiAssignation (Environment currentEnv) {
        this.currentEnv = currentEnv;
    }

}
