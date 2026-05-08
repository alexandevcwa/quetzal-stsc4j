package com.stsc4j.semantic.analyzer;

import com.stsc4j.parser.v1.ast.ExpressionList;
import com.stsc4j.semantic.Environment;
import com.stsc4j.semantic.SemanticAbstractAnalyzer;
import com.stsc4j.semantic.SemanticError;

public class SemanticExpressionList extends SemanticAbstractAnalyzer {

    private final Environment currentEnv;

    public SemanticExpressionList (Environment currentEnv) {
        this.currentEnv = currentEnv;
    }

    @Override
    public String visit(ExpressionList expressionList) {
        //Si la lista esta vacia, se le da un tipo desconocido
        if (expressionList.expressions == null || expressionList.expressions.isEmpty()) {
            return "lista<desconocido>";
        }

        //Tomamos el tipo del primer elemento como identificador
        String tipoReferencia = expressionList.expressions.get(0).accept(this);

        //Comparamos todos los demas elementos con esta regla
        for (int i = 1; i < expressionList.expressions.size(); i++) {
            String tipoActual = expressionList.expressions.get(i).accept(this);
            if (tipoActual != null && !tipoActual.equals(tipoReferencia)) {
                throw new SemanticError("Todos los elementos de la lista deben ser del mismo tipo. Se detectó un '" + tipoActual + "' pero esperaba un '" + tipoReferencia + "'.");
            }
        }
        //Si todos pasaron la prueba devolvemos el tipo de la lista
        return "lista<" + tipoReferencia + ">";
    }

}
