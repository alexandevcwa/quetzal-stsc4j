package com.stsc4j.semantic.analyzer;

import com.stsc4j.parser.v1.ast.ExpressionBinary;
import com.stsc4j.semantic.Environment;
import com.stsc4j.semantic.SemanticAbstractAnalyzer;
import com.stsc4j.semantic.SemanticError;

public class SemanticExpressionBinary extends SemanticAbstractAnalyzer {

    private final Environment currentEnv;

    public SemanticExpressionBinary (Environment currentEnv) {
        this.currentEnv = currentEnv;
    }
    @Override
    public String visit(ExpressionBinary expressionBinary) {
        String tipoIzquierdo = expressionBinary.left.accept(this);
        String tipoDerecho = expressionBinary.right.accept(this);

        //Validación si intentan sumar dos enteros o dos cadenas
        if (tipoIzquierdo != null && tipoDerecho != null && !tipoIzquierdo.equals(tipoDerecho)) {
            throw new SemanticError("No puedes operar entre tipos diferentes: " + tipoIzquierdo + " con un " + tipoDerecho);
        }
        return tipoIzquierdo; // El resultado de la operación tendrá el mismo tipo que los operandos
    }

}
