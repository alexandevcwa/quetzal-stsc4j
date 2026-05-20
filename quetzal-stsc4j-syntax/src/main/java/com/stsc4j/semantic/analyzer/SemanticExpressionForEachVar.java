package com.stsc4j.semantic.analyzer;

import com.stsc4j.parser.v1.ast.ExpressionForEachVar;
import com.stsc4j.semantic.SemanticAbstractAnalyzer;
import com.stsc4j.semantic.SemanticAnalyzer;

public class SemanticExpressionForEachVar extends SemanticAbstractAnalyzer {

    private final SemanticAnalyzer analyzer;

    public SemanticExpressionForEachVar(SemanticAnalyzer analyzer) {
        this.analyzer = analyzer;
    }

    @Override
    public String visit(ExpressionForEachVar expr) {
        // Obtenemos el nombre exacto de la variable (ej: "elemento")
        String nombreVar = expr.variable.getLexeme();

        // Obtenemos el nombre oficial del tipo (ej: "PRIMITIVE_INTEGER")
        String tipoDeclarado = expr.type.getType().name();

        // Guardamos la variable temporal en la memoria actual del director
        analyzer.getEnv().define(nombreVar, tipoDeclarado, expr.mutable);

        return tipoDeclarado;
    }
}