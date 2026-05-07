package com.stsc4j.semantic.analyzer;

import com.stsc4j.parser.v1.ast.StatementJsn;
import com.stsc4j.semantic.Environment;
import com.stsc4j.semantic.SemanticAbstractAnalyzer;

public class SemanticStatementJsn extends SemanticAbstractAnalyzer {

    private final Environment currentEnv;

    public SemanticStatementJsn (Environment currentEnv) {
        this.currentEnv = currentEnv;
    }

    @Override
    public String visit(StatementJsn statementJsn) {
        //Analizamos el bloque {...} para encontrar errores internos
        statementJsn.block.accept(this);

        //Extraemos el nombre de la variable
        String nombreVariable = statementJsn.identifier.getLexeme();

        //Lo guardamos en nuestra memoria environment usando el tipo "jsn"
        currentEnv.define(nombreVariable, "jsn", false); // Asumimos que los JSN son inmutables

        return null;
    }

}
