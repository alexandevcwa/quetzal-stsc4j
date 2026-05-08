package com.stsc4j.semantic.analyzer;

import com.stsc4j.parser.v1.ast.StatementFunctionParameter;
import com.stsc4j.semantic.Environment;
import com.stsc4j.semantic.SemanticAbstractAnalyzer;

public class SemanticStatementFunctionParameter extends SemanticAbstractAnalyzer {

    private final Environment currentEnv;

    public SemanticStatementFunctionParameter(Environment currentEnv) {
        this.currentEnv = currentEnv;
    }

    @Override
    public String visit(StatementFunctionParameter statementFunctionParameter) {
        // Extraemos el tipo y nombre del parámetro
        String tipoParametro = statementFunctionParameter.type.getLexeme();
        String nombreParametro = statementFunctionParameter.identified.getLexeme();

        // Inyectamos el parámetro en la memoria local actual como una variable normal (mutable)
        currentEnv.define(nombreParametro, tipoParametro, true);

        // Devolvemos el tipo del parámetro (esto servirá luego para armar la firma)
        return tipoParametro;
    }

}
