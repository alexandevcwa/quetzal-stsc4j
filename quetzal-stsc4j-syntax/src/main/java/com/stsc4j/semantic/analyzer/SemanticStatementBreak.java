package com.stsc4j.semantic.analyzer;

import com.stsc4j.parser.v1.ast.StatementBreak;
import com.stsc4j.semantic.SemanticAbstractAnalyzer;
import com.stsc4j.semantic.SemanticAnalyzer;
import com.stsc4j.semantic.SemanticError;

public class SemanticStatementBreak extends SemanticAbstractAnalyzer {

    private final SemanticAnalyzer analyzer;

    public SemanticStatementBreak(SemanticAnalyzer analyzer) {
        this.analyzer = analyzer;
    }

    @Override
    public String visit(StatementBreak statementBreak) {
        // Le preguntamos al director si estamos dentro de un ciclo
        if (!analyzer.isInLoop()) {
            throw new SemanticError("Error Semántico: La instrucción 'romper' (break) solo puede usarse dentro de un ciclo (para, mientras, hacer o por cada).");
        }
        return null;
    }
}