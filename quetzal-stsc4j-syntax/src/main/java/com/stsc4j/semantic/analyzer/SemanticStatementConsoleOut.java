package com.stsc4j.semantic.analyzer;

import com.stsc4j.parser.v1.ast.StatementConsolaOut;
import com.stsc4j.semantic.Environment;
import com.stsc4j.semantic.SemanticAbstractAnalyzer;

public class SemanticStatementConsoleOut extends SemanticAbstractAnalyzer {

    private final Environment currentEnv;

    public SemanticStatementConsoleOut(Environment currentEnv) {
        this.currentEnv = currentEnv;
    }

    @Override
    public String visit(StatementConsolaOut statementConsolaOut) {
        statementConsolaOut.expression.accept(this);
        return super.visit(statementConsolaOut);
    }
}
