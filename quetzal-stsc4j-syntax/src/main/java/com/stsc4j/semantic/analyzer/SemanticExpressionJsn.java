package com.stsc4j.semantic.analyzer;

import com.stsc4j.parser.v1.ast.Expression;
import com.stsc4j.parser.v1.ast.ExpressionJsn;
import com.stsc4j.semantic.SemanticAbstractAnalyzer;
import com.stsc4j.semantic.SemanticAnalyzer;

public class SemanticExpressionJsn extends SemanticAbstractAnalyzer {

    private final SemanticAnalyzer analyzer;

    public SemanticExpressionJsn(SemanticAnalyzer analyzer) {
        this.analyzer = analyzer;
    }

    @Override
    public String visit(ExpressionJsn expressionJsn) {
        // Analizamos los valores internos asociados a la clave
        if (expressionJsn.value != null) {
            expressionJsn.value.accept(analyzer);
        } else if (expressionJsn.values != null) {
            for (Expression expr : expressionJsn.values) {
                expr.accept(analyzer);
            }
        }

        return "jsn_field";
    }
}