package com.stsc4j.semantic.analyzer;

import com.stsc4j.parser.v1.ast.StatementThrow;
import com.stsc4j.semantic.SemanticAbstractAnalyzer;
import com.stsc4j.semantic.SemanticAnalyzer;

public class SemanticStatementThrow extends SemanticAbstractAnalyzer {

    private final SemanticAnalyzer analyzer;

    public SemanticStatementThrow(SemanticAnalyzer analyzer) {
        this.analyzer = analyzer;
    }

    @Override
    public String visit(StatementThrow stmt) {
        // Validamos la expresión que se está lanzando (ej. "Error: División por cero")
        if (stmt.message != null) {
            stmt.message.accept(analyzer);
        }
        return null;
    }
}