package com.stsc4j.semantic.analyzer;

import com.stsc4j.parser.v1.ast.StatementConsolaOut;
import com.stsc4j.semantic.SemanticAbstractAnalyzer;
import com.stsc4j.semantic.SemanticAnalyzer;

public class SemanticStatementConsoleOut extends SemanticAbstractAnalyzer {

    private final SemanticAnalyzer analyzer;

    public SemanticStatementConsoleOut(SemanticAnalyzer analyzer) {
        this.analyzer = analyzer;
    }

    @Override
    public String visit(StatementConsolaOut statementConsolaOut) {
        if (statementConsolaOut.expression != null) {
            statementConsolaOut.expression.accept(analyzer);
        }
        return null;
    }
}