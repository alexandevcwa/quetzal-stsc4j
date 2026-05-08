package com.stsc4j.semantic.analyzer;

import com.stsc4j.parser.v1.ast.StatementReturn;
import com.stsc4j.semantic.Environment;
import com.stsc4j.semantic.SemanticAbstractAnalyzer;
import com.stsc4j.semantic.SemanticError;

public class SemanticStatementReturn extends SemanticAbstractAnalyzer {

    private final Environment currentEnv;

    public SemanticStatementReturn (Environment currentEnv) {
        this.currentEnv = currentEnv;
    }

    @Override
    public String visit(StatementReturn statementReturn) {
        String tipoRealRetornado = "vacio";

        // 1. Si el return tiene algo a la derecha (ej: return 5 + 5)
        if (statementReturn.returnExpression != null){
            // Evaluamos esa expresión para saber su tipo real
            tipoRealRetornado = statementReturn.returnExpression.accept(this);
        }

        // 2. ¡LA VALIDACIÓN MAESTRA!
        // Comparamos lo que la función prometió devolver (que guardamos al entrar a ella)
        // contra lo que realmente está devolviendo este 'return'.
        String retornoEsperadoActual = "vacio";
        if (!retornoEsperadoActual.equals(tipoRealRetornado)) {
            throw new SemanticError("Conflicto de tipos: La función prometió devolver un '"
                    + retornoEsperadoActual + "', pero el return está devolviendo un '"
                    + tipoRealRetornado + "'.");
        }

        return tipoRealRetornado;
    }


}
