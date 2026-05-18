package com.stsc4j.semantic.analyzer;

import com.stsc4j.parser.v1.ast.ExpressionList;
import com.stsc4j.semantic.Environment;
import com.stsc4j.semantic.SemanticAbstractAnalyzer;
import com.stsc4j.semantic.SemanticAnalyzer;

public class SemanticExpressionList extends SemanticAbstractAnalyzer {

    private final Environment currentEnv;
    private final SemanticAnalyzer analyzer;

    public SemanticExpressionList(Environment currentEnv, SemanticAnalyzer analyzer) {
        this.currentEnv = currentEnv;
        this.analyzer = analyzer;
    }

    @Override
    public String visit(ExpressionList expressionList) {
        // 1. Si la lista está vacía, devuelve un tipo comodín
        if (expressionList.expressions == null || expressionList.expressions.isEmpty()) {
            return "lista<vacia>";
        }

        // 2. Tomamos el primer elemento como referencia
        String tipoReferencia = expressionList.expressions.get(0).accept(analyzer);
        boolean esMixta = false;

        // 3. Revisamos los demás elementos
        for (int i = 1; i < expressionList.expressions.size(); i++) {
            String tipoActual = expressionList.expressions.get(i).accept(analyzer);

            // Flexibilidad numérica: si mezclan enteros y números/decimales, lo convertimos a número
            if (tipoReferencia.equals("entero") && (tipoActual.equals("número") || tipoActual.equals("decimal"))) {
                tipoReferencia = "número";
            } else if (tipoActual != null && !tipoActual.equals(tipoReferencia)) {
                // Si encontramos algo totalmente diferente (ej. entero y texto), marcamos la lista como mixta
                esMixta = true;
            }
        }

        // 4. Si se mezclaron tipos incompatibles, la lista completa se vuelve dinámica
        if (esMixta) {
            return "lista<dinamico>";
        }

        // 5. Si todos eran iguales (o eran números compatibles), devolvemos el tipo exacto
        return "lista<" + tipoReferencia + ">";
    }
}