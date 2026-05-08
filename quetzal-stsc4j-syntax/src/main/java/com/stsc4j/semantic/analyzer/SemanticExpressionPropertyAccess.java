package com.stsc4j.semantic.analyzer;

import com.stsc4j.parser.v1.ast.ExpressionPropertyAccess;
import com.stsc4j.semantic.Environment;
import com.stsc4j.semantic.SemanticAbstractAnalyzer;
import com.stsc4j.semantic.SemanticAnalyzer;
import com.stsc4j.semantic.SemanticError;

public class SemanticExpressionPropertyAccess extends SemanticAbstractAnalyzer {

    private final Environment currentEnv;
    private final SemanticAnalyzer analyzer;

    public SemanticExpressionPropertyAccess(Environment currentEnv, SemanticAnalyzer analyzer) {
        this.currentEnv = currentEnv;
        this.analyzer = analyzer;
    }

    @Override
    public String visit(ExpressionPropertyAccess expr) {
        // Evaluamos que el objeto exista
        String tipoObjeto = expr.object.accept(analyzer);

        if (tipoObjeto == null) {
            throw new SemanticError("Se intentó acceder a la propiedad '" + expr.propertyName.getLexeme() + "' de un objeto inexistente.");
        }

        // Por ahora, al acceder a una propiedad dinámica, devolvemos 'dinamico' o 'cualquiera'
        return "dinamico";
    }
}