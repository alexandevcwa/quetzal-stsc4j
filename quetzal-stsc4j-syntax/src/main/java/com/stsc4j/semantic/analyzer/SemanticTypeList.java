package com.stsc4j.semantic.analyzer;

import com.stsc4j.semantic.Environment;
import com.stsc4j.semantic.SemanticAbstractAnalyzer;

public class SemanticTypeList extends SemanticAbstractAnalyzer {

    private final Environment currentEnv;

    public SemanticTypeList (Environment currentEnv) {
        this.currentEnv = currentEnv;
    }


}
