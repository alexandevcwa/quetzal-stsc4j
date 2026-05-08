package com.stsc4j.semantic.analyzer;

import com.stsc4j.parser.v1.ast.Expression;
import com.stsc4j.parser.v1.ast.ExpressionJsn;
import com.stsc4j.semantic.Environment;
import com.stsc4j.semantic.SemanticAbstractAnalyzer;

public class SemanticExpressionJsn extends SemanticAbstractAnalyzer {

    private final Environment currentEnv;

    public SemanticExpressionJsn (Environment currentEnv) {
        this.currentEnv = currentEnv;
    }

    @Override
    public String visit(ExpressionJsn expressionJsn) {
        //Aqui se analizan las propiedades del JSN. Por ejemplo, si tenemos { nombre: "Juan", edad: 30 }

        if (expressionJsn.value != null){
            //si es un valor simple (cadena, entero) lo evalua
            expressionJsn.value.accept(this);
        } else if (expressionJsn.values != null){
            for (Expression expr : expressionJsn.values){
                expr.accept(this);
            }
        }
        // El resultado de una propiedad JSN es simplemente "jsn", porque no nos interesa el tipo interno de cada propiedad, solo que es un bloque válido.
        return "jsn_field";
    }

}
