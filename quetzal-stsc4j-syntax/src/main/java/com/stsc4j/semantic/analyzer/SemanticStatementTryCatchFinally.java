package com.stsc4j.semantic.analyzer;

import com.stsc4j.parser.v1.ast.StatementTryCatchFinally;
import com.stsc4j.semantic.Environment;
import com.stsc4j.semantic.SemanticAbstractAnalyzer;
import com.stsc4j.semantic.SemanticAnalyzer;

public class SemanticStatementTryCatchFinally extends SemanticAbstractAnalyzer {

    private final SemanticAnalyzer analyzer;

    public SemanticStatementTryCatchFinally(SemanticAnalyzer analyzer) {
        this.analyzer = analyzer;
    }

    @Override
    public String visit(StatementTryCatchFinally stmt) {
        // 1. Analizar el bloque 'intentar' (try) usando el nombre correcto 'tryBlock'
        if (stmt.tryBlock != null) {
            stmt.tryBlock.accept(analyzer);
        }

        // 2. Analizar el bloque 'capturar' (catch)
        // Verificamos que exista el bloque 'catchBlock' y la variable 'exception'
        if (stmt.catchBlock != null && stmt.exception != null) {

            // CREAMOS UN SCOPE TEMPORAL para la variable del error
            Environment entornoAnterior = analyzer.getEnv();
            analyzer.setEnv(new Environment(entornoAnterior));

            try {
                // Extraemos el nombre que el programador le dio al error (ej. 'e' o 'error')
                String nombreVariableError = stmt.exception.token.getLexeme();

                // Inyectamos la variable en la memoria como tipo "excepcion"
                // La marcamos como inmutable (false) para que no la puedan reasignar por accidente
                analyzer.getEnv().define(nombreVariableError, "excepcion", false);

                // Evaluamos el contenido del catch (ahora la variable existe para este bloque)
                stmt.catchBlock.accept(analyzer);
            } finally {
                // ¡DESTRUIMOS EL SCOPE TEMPORAL! La variable del error muere aquí.
                analyzer.setEnv(entornoAnterior);
            }
        }

        // 3. Analizar el bloque 'finalmente' (finally) si el programador lo incluyó
        if (stmt.finallyBlock != null) {
            stmt.finallyBlock.accept(analyzer);
        }

        return null;
    }
}