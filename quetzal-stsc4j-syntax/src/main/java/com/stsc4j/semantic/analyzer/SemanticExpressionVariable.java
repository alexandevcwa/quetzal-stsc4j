package com.stsc4j.semantic.analyzer;

import com.stsc4j.parser.v1.ast.ExpressionVariable;
import com.stsc4j.semantic.Environment;
import com.stsc4j.semantic.SemanticAbstractAnalyzer;
import com.stsc4j.semantic.SemanticAnalyzer;
import com.stsc4j.semantic.SemanticError;

public class SemanticExpressionVariable extends SemanticAbstractAnalyzer {

    private final SemanticAnalyzer analyzer;

    public SemanticExpressionVariable(SemanticAnalyzer analyzer) {
        this.analyzer = analyzer;
    }

    @Override
    public String visit(ExpressionVariable expr) {
        String name = expr.token.getLexeme();

        try {
            // Intentamos resolver como variable
            return analyzer.getEnv().resolveType(name);
        } catch (SemanticError e) {
            // Si falla, intentamos ver si es una función
            try {
                Environment.FunctionInfo info = analyzer.getEnv().resolveFunction(name);
                if (info != null) {
                    return "function"; // Retornamos un tipo especial para no fallar
                }
            } catch (SemanticError ignored) {
                // Si tampoco es función, entonces sí lanzamos el error original
            }
            throw e;
        }
    }
}