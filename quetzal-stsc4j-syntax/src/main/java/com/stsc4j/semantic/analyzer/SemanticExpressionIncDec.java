package com.stsc4j.semantic.analyzer;

import com.stsc4j.parser.v1.ast.ExpressionIncDec;
import com.stsc4j.semantic.Environment;
import com.stsc4j.semantic.SemanticAbstractAnalyzer;
import com.stsc4j.semantic.SemanticError;

public class SemanticExpressionIncDec extends SemanticAbstractAnalyzer {


    private final Environment currentEnv;

    public SemanticExpressionIncDec (Environment currentEnv) {
        this.currentEnv = currentEnv;
    }

    @Override
    public String visit(ExpressionIncDec expressionIncDec) {
        // 1. Evaluamos la variable a la que le están aplicando el ++ o --
        String tipoVariable = expressionIncDec.identifier.accept(this);
        String nombreVariable = expressionIncDec.identifier.token.getLexeme();

        // 2. Validamos que sea estrictamente un tipo numérico
        if (tipoVariable != null && !tipoVariable.equals("entero") && !tipoVariable.equals("decimal")) {
            throw new SemanticError("El operador de incremento/decremento solo se puede aplicar a números, pero intentaste usarlo en la variable '" + nombreVariable + "' de tipo '" + tipoVariable + "'.");
        }

        // Retornamos el mismo tipo de la variable (si era entero, sigue siendo entero)
        return tipoVariable;
    }

}
