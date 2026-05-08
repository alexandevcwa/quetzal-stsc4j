package com.stsc4j.semantic.analyzer;

import com.stsc4j.parser.v1.ast.StatementIf;
import com.stsc4j.semantic.Environment;
import com.stsc4j.semantic.SemanticAbstractAnalyzer;
import com.stsc4j.semantic.SemanticError;

public class SemanticStatementIf extends SemanticAbstractAnalyzer {

    private final Environment currentEnv;

    public SemanticStatementIf(Environment currentEnv) {
        this.currentEnv = currentEnv;
    }

    @Override
    public String visit(StatementIf statementIf) {
        String tipoCondicion  = statementIf.condition.accept(this);
        if (tipoCondicion != null && !tipoCondicion.equals("booleano")) {
            throw new SemanticError("La condición del if debe ser de tipo booleano, pero se encontró: " + tipoCondicion);
        }

        // Analizamos la sentencia else con el mismo entorno
        statementIf.thenStatement.accept(this);
        if (statementIf.elseStatement != null) {
            statementIf.elseStatement.accept(this);
        }
        return null;
    }

}
