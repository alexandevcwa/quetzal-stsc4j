package com.stsc4j.semantic.analyzer;

import com.stsc4j.parser.v1.ast.ExpressionForEachVar;
import com.stsc4j.semantic.Environment;
import com.stsc4j.semantic.SemanticAbstractAnalyzer;
import com.stsc4j.semantic.SemanticAnalyzer;

public class SemanticExpressionForEachVar extends SemanticAbstractAnalyzer {

    private final Environment currentEnv;

    public SemanticExpressionForEachVar(Environment currentEnv, SemanticAnalyzer analyzer) {
        this.currentEnv = currentEnv;
    }

    @Override
    public String visit(ExpressionForEachVar expr) {
        String nombreVar = expr.type.getLexeme();

        // Guardamos la variable temporal en la memoria
        currentEnv.define(nombreVar, "dinamico", true);

        return "dinamico";
    }
}