package com.stsc4j.semantic.analyzer;

import com.stsc4j.parser.v1.ast.ExpressionMethodCall;
import com.stsc4j.semantic.Environment;
import com.stsc4j.semantic.SemanticAbstractAnalyzer;
import com.stsc4j.semantic.SemanticError;

public class SemanticExpressionMethodCall extends SemanticAbstractAnalyzer {

    private final Environment currentEnv;

    public SemanticExpressionMethodCall (Environment currentEnv) {
        this.currentEnv = currentEnv;
    }

    @Override
    public String visit(ExpressionMethodCall expressionMethodCall) {
        String tipoObjeto = expressionMethodCall.object.accept(this);
        String nombreMetodo = expressionMethodCall.methodName.getLexeme();

        if (tipoObjeto != null && tipoObjeto.startsWith("lista<")) {

            String tipoInterno = tipoObjeto.replace("lista<", "").replace(">", "");

            if (nombreMetodo.equals("agregar")) {
                if (expressionMethodCall.args.size() != 1) {
                    throw new SemanticError("El método 'agregar' necesita exactamente 1 argumento.");
                }

                String tipoArgumento = expressionMethodCall.args.get(0).accept(this);

                if (tipoArgumento != null && !tipoArgumento.equals(tipoInterno)) {
                    throw new SemanticError("Intentaste agregar un dato tipo '" + tipoArgumento + "' a una lista estricta de '" + tipoInterno + "'.");
                }

                return tipoObjeto;
            }
        }

        return "desconocido";
    }

}
