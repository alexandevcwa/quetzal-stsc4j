package com.stsc4j.semantic.analyzer;

import com.stsc4j.parser.v1.ast.ExpressionTernary;
import com.stsc4j.semantic.Environment;
import com.stsc4j.semantic.SemanticAbstractAnalyzer;
import com.stsc4j.semantic.SemanticError;

public class SemanticExpressionTernary extends SemanticAbstractAnalyzer {

    private final Environment currentEnv;

    public SemanticExpressionTernary (Environment currentEnv) {
        this.currentEnv = currentEnv;
    }

    @Override
    public String visit(ExpressionTernary expressionTernary) {
        // Validamos la condicion (como si fuera un if)
        String tipoCondicion = expressionTernary.binary.accept(this);
        if (tipoCondicion != null && !tipoCondicion.equals("booleano")) {
            throw new SemanticError("La condicion del operador ternario debe ser booleano");
        }

        // Ambos lados del ternario deben devolver lo mismo
        String tipoIzquierdo = expressionTernary.left.accept(this);
        String tipoDerecho = expressionTernary.right.accept(this);

        if (tipoIzquierdo != null && tipoDerecho != null && !tipoIzquierdo.equals(tipoDerecho)) {
            throw new SemanticError("El ternario devuelve cosas distintas: " + tipoIzquierdo + " y " + tipoDerecho);
        }

        return tipoIzquierdo;
    }


}
