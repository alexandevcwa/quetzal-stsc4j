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
        // 1. Evaluamos la colección (debe devolver algo como "lista<PRIMITIVE_INTEGER>")
        String tipoColeccion = stmt.listVariable.accept(analyzer);

        if (tipoColeccion == null || !tipoColeccion.startsWith("lista<")) {
            throw new SemanticError("Error Semántico: La colección a iterar en el ciclo 'por cada' (foreach) debe ser una lista válida, pero se encontró: " + tipoColeccion);
        }

        // 2. CREAMOS EL SCOPE LOCAL PARA LA VARIABLE ITERADORA
        Environment entornoAnterior = analyzer.getEnv();
        analyzer.setEnv(new Environment(entornoAnterior));

        try {
            // 3. Evaluamos la declaración (ej. 'entero elemento'). Esto la guarda en el nuevo entorno temporal.
            String tipoIterador = stmt.declaration.accept(analyzer);

            // String tipoInternoLista = tipoColeccion.replace("lista<", "").replace(">", "");
            // if (!tipoIterador.equals(tipoInternoLista)) throw SemanticError...

            // 4. Evaluamos el bloque de código interno del ciclo
            if (stmt.block != null) {
                stmt.block.accept(analyzer);
            }
        } finally {
            // 5. ¡DESTRUIMOS EL SCOPE! La variable iteradora deja de existir.
            analyzer.setEnv(entornoAnterior);
        }

        return null;
    }
}