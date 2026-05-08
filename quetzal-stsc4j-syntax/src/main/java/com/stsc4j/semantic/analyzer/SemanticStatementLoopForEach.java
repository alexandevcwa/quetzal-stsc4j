package com.stsc4j.semantic.analyzer;

import com.stsc4j.parser.v1.ast.StatementLoopForEach;
import com.stsc4j.semantic.Environment;
import com.stsc4j.semantic.SemanticAbstractAnalyzer;
import com.stsc4j.semantic.SemanticAnalyzer;
import com.stsc4j.semantic.SemanticError;

public class SemanticStatementLoopForEach extends SemanticAbstractAnalyzer {

    private final Environment currentEnv;
    private final SemanticAnalyzer analyzer;

    public SemanticStatementLoopForEach(Environment currentEnv, SemanticAnalyzer analyzer) {
        this.currentEnv = currentEnv;
        this.analyzer = analyzer;
    }

    @Override
    public String visit(StatementLoopForEach stmt) {
        // 1. Verificamos que lo que estamos iterando exista
        String tipoColeccion = stmt.declaration.accept(analyzer);

        if (tipoColeccion == null) {
            throw new SemanticError("La colección a iterar en el ciclo 'por cada' no es válida o no existe.");
        }

        // 2. Evaluamos la variable iteradora (ej: 'elemento')
        stmt.listVariable.accept(analyzer);

        // 3. Evaluamos el bloque de código interno del ciclo
        if (stmt.block != null) {
            stmt.block.accept(analyzer);
        }

        return "void";
    }
}