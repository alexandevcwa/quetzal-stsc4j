package com.stsc4j.semantic.analyzer;

import com.stsc4j.parser.v1.ast.StatementIncDec;
import com.stsc4j.semantic.SemanticAbstractAnalyzer;
import com.stsc4j.semantic.SemanticAnalyzer;

public class SemanticStatementIncDec extends SemanticAbstractAnalyzer {

    private final SemanticAnalyzer analyzer;

    public SemanticStatementIncDec(SemanticAnalyzer analyzer) {
        this.analyzer = analyzer;
    }

    @Override
    public String visit(StatementIncDec stmt) {
        // Despachamos la expresión interna para que sea evaluada
        stmt.expression.accept(analyzer);

        // Las instrucciones no devuelven tipo de dato
        return null;
    }
}