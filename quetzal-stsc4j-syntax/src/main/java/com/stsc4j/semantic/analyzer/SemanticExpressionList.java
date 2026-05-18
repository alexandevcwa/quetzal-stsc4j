package com.stsc4j.semantic.analyzer;

import com.stsc4j.parser.v1.ast.Expression;
import com.stsc4j.parser.v1.ast.ExpressionList;
import com.stsc4j.semantic.SemanticAbstractAnalyzer;
import com.stsc4j.semantic.SemanticAnalyzer;

public class SemanticExpressionList extends SemanticAbstractAnalyzer {

    private final SemanticAnalyzer analyzer;

    public SemanticExpressionList(SemanticAnalyzer analyzer) {
        this.analyzer = analyzer;
    }

    @Override
    public String visit(ExpressionList expr) {
        if (expr.expressions == null || expr.expressions.isEmpty()) {
            return "lista"; // Lista vacía, no sabemos su contenido
        }

        // Adivinamos el tipo basándonos en el primer elemento
        String tipoPrimerElemento = expr.expressions.get(0).accept(analyzer);
        boolean esMixta = false;

        for (int i = 1; i < expr.expressions.size(); i++) {
            String tipoActual = expr.expressions.get(i).accept(analyzer);
            if (tipoActual != null && !tipoActual.equals(tipoPrimerElemento)) {
                esMixta = true;
                break;
            }
        }

        if (esMixta || tipoPrimerElemento == null) {
            return "lista"; // Si hay mezclas, es una lista sin tipar genérica
        }

        return "lista<" + tipoPrimerElemento + ">"; // Retornamos la firma completa
    }
}