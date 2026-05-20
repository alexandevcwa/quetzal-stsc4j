package com.stsc4j.semantic.analyzer;

import com.stsc4j.parser.v1.ast.StatementMethodCall;
import com.stsc4j.semantic.Environment;
import com.stsc4j.semantic.SemanticAbstractAnalyzer;
import com.stsc4j.semantic.SemanticAnalyzer;

public class SemanticStatementMethodCall extends SemanticAbstractAnalyzer {

    private final SemanticAnalyzer analyzer;

    public SemanticStatementMethodCall(SemanticAnalyzer analyzer) {
        this.analyzer = analyzer;
    }


    @Override
    public String visit(StatementMethodCall stmt) {
        // Delegamos la validación semántica a la expresión interna.
        // Esto verificará que la función exista y que los parámetros sean correctos.
        if (stmt.methodCall != null) {
            stmt.methodCall.accept(this);
        }

        // Un Statement nunca retorna un tipo de dato en el análisis semántico.
        return null;
    }
}
