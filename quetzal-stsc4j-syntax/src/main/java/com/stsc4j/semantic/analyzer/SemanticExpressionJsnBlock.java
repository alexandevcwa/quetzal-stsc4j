package com.stsc4j.semantic.analyzer;

import com.stsc4j.parser.v1.ast.ExpressionJsn;
import com.stsc4j.parser.v1.ast.ExpressionJsnBlock;
import com.stsc4j.semantic.SemanticAbstractAnalyzer;
import com.stsc4j.semantic.SemanticAnalyzer;

public class SemanticExpressionJsnBlock extends SemanticAbstractAnalyzer {

    private final SemanticAnalyzer analyzer;

    public SemanticExpressionJsnBlock(SemanticAnalyzer analyzer) {
        this.analyzer = analyzer;
    }

    @Override
    public String visit(ExpressionJsnBlock expressionJsnBlock) {
        // Iteramos y validamos semánticamente cada elemento del bloque JSN
        if (expressionJsnBlock.expressions != null) {
            for (ExpressionJsn exprJsn : expressionJsnBlock.expressions) {
                exprJsn.accept(analyzer); // Despachado a través del director
            }
        }

        // Retornamos el tipo de dato unificado
        return "jsn";
    }
}