package com.stsc4j.semantic.analyzer;

import com.stsc4j.parser.v1.ast.StatementVariable;
import com.stsc4j.semantic.Environment;
import com.stsc4j.semantic.SemanticAbstractAnalyzer;
import com.stsc4j.semantic.SemanticAnalyzer;
import com.stsc4j.semantic.SemanticError;

public class SemanticStatementVariable extends SemanticAbstractAnalyzer {

    private final Environment currentEnv;
    // Agregamos al director
    private final SemanticAnalyzer analyzer;

    // Pedimos al director en el constructor
    public SemanticStatementVariable(Environment currentEnv, SemanticAnalyzer analyzer) {
        this.currentEnv = currentEnv;
        this.analyzer = analyzer;
    }

    @Override
    public String visit (StatementVariable statementVariable) {
        // ¡LA MAGIA! Le devolvemos el trabajo al director (analyzer) en lugar de a 'this'
        String tipoReal = null;
        if (statementVariable.initialValue != null) {
            tipoReal = statementVariable.initialValue.accept(analyzer);
        }

        String nombreVariable = statementVariable.name.getLexeme();

        // TRUCO DEL PARSER:
        if (statementVariable.typo.getType().toString().equals("IDENTIFIER")) {
            currentEnv.assign(nombreVariable, tipoReal);
        } else {
            String tipoEsperado = statementVariable.typo.getLexeme();

            if (tipoReal != null && !tipoEsperado.equals(tipoReal)) {
                throw new SemanticError("Trataste de guardar un dato tipo " + tipoReal + " en una variable de tipo " + tipoEsperado);
            }
            currentEnv.define(nombreVariable, tipoEsperado, statementVariable.mutable);
        }

        return null;
    }
}