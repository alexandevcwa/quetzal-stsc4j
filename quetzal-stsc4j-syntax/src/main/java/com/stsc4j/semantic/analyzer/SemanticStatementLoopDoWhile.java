package com.stsc4j.semantic.analyzer;

import com.stsc4j.parser.v1.ast.StatementLoopDoWhile;
import com.stsc4j.semantic.Environment;
import com.stsc4j.semantic.SemanticAbstractAnalyzer;
import com.stsc4j.semantic.SemanticError;

public class SemanticStatementLoopDoWhile extends SemanticAbstractAnalyzer {

    private final Environment currentEnv;

    public SemanticStatementLoopDoWhile (Environment currentEnv) {
        this.currentEnv = currentEnv;
    }

    @Override
    public String visit(StatementLoopDoWhile statementLoopDoWhile) {
        // En análisis semántico, el orden de ejecución no cambia cómo validamos los tipos,
        // pero seguimos la lógica del "hacer": primero el bloque, luego la condición.

        statementLoopDoWhile.block.accept(this);

        String tipoCondicion = statementLoopDoWhile.condition.accept(this);
        if (tipoCondicion != null && !tipoCondicion.equals("booleano")) {
            throw new SemanticError("La condición del ciclo 'hacer mientras' debe ser un 'booleano', pero se encontró un '" + tipoCondicion + "'.");
        }

        return null;
    }

}
