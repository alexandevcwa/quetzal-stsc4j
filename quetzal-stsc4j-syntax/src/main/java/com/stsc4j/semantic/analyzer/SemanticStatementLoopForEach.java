package com.stsc4j.semantic.analyzer;

import com.stsc4j.parser.v1.ast.StatementLoopForEach;
import com.stsc4j.semantic.Environment;
import com.stsc4j.semantic.SemanticAbstractAnalyzer;
import com.stsc4j.semantic.SemanticAnalyzer;
import com.stsc4j.semantic.SemanticError;

public class SemanticStatementLoopForEach extends SemanticAbstractAnalyzer {

    private final SemanticAnalyzer analyzer;

    public SemanticStatementLoopForEach(SemanticAnalyzer analyzer) {
        this.analyzer = analyzer;
    }

    @Override
    public String visit(StatementLoopForEach stmt) {
        String tipoColeccion = stmt.listVariable.accept(analyzer);

        if (tipoColeccion == null || !tipoColeccion.startsWith("lista<")) {
            throw new SemanticError("Error Semántico: La colección a iterar debe ser una lista válida.");
        }

        Environment entornoAnterior = analyzer.getEnv();
        analyzer.setEnv(new Environment(entornoAnterior));

        try {
            stmt.declaration.accept(analyzer);

            // ¡ENCENDEMOS EL RADAR!
            analyzer.enterLoop();
            try {
                if (stmt.block != null) {
                    stmt.block.accept(analyzer);
                }
            } finally {
                analyzer.exitLoop(); // ¡APAGAMOS EL RADAR!
            }
        } finally {
            analyzer.setEnv(entornoAnterior);
        }

        return null;
    }
}