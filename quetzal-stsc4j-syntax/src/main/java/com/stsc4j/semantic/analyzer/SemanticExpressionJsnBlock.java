package com.stsc4j.semantic.analyzer;

import com.stsc4j.parser.v1.ast.ExpressionJsn;
import com.stsc4j.parser.v1.ast.ExpressionJsnBlock;
import com.stsc4j.semantic.Environment;
import com.stsc4j.semantic.SemanticAbstractAnalyzer;

public class SemanticExpressionJsnBlock extends SemanticAbstractAnalyzer {

    private final Environment currentEnv;

    public SemanticExpressionJsnBlock (Environment currentEnv) {
        this.currentEnv = currentEnv;
    }

    @Override
    public String visit(ExpressionJsnBlock expressionJsnBlock) {
        //Un bloque JSN es solo un contenedor.
        //Su trabajo es iterar sobre todas las propiedades y se validan

        if (expressionJsnBlock.expressions != null){
            for (ExpressionJsn exprJsn : expressionJsnBlock.expressions){
                exprJsn.accept(this);
            }
        }
        // Informamos al nivel superior que es tipo jsn
        return "jsn";
    }
}
