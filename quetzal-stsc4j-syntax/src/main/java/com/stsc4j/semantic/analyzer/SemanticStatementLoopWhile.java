package com.stsc4j.semantic.analyzer;

import com.stsc4j.parser.v1.ast.StatementLoopWhile;
import com.stsc4j.semantic.Environment;
import com.stsc4j.semantic.SemanticAbstractAnalyzer;
import com.stsc4j.semantic.SemanticError;

public class SemanticStatementLoopWhile extends SemanticAbstractAnalyzer {

    private final Environment currentEnv;

    public SemanticStatementLoopWhile(Environment currentEnv) {
        this.currentEnv = currentEnv;
    }

    @Override
    public String visit(StatementLoopWhile statementLoopWhile) {
        // 1. Validamos que la condición sea estrictamente booleana
        String tipoCondicion = statementLoopWhile.condition.accept(this);
        if (tipoCondicion != null && !tipoCondicion.equals("booleano")) {
            throw new SemanticError("La condición del ciclo 'mientras' debe ser un 'booleano', pero se encontró un '" + tipoCondicion + "'.");
        }

        // 2. Analizamos las sentencias dentro del bloque
        // (El StatementBlock ya se encarga de crear su propio Environment interno)
        statementLoopWhile.block.accept(this);

        return null;
    }

}
