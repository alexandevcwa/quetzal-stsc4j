package com.stsc4j.semantic.analyzer;

import com.stsc4j.parser.v1.ast.ExpressionNull; // <-- Verifica que este sea el nombre correcto del AST
import com.stsc4j.semantic.SemanticAbstractAnalyzer;
import com.stsc4j.semantic.SemanticAnalyzer;

public class SemanticExpressionNull extends SemanticAbstractAnalyzer {

    private final SemanticAnalyzer analyzer;

    public SemanticExpressionNull(SemanticAnalyzer analyzer) {
        this.analyzer = analyzer;
    }

    @Override
    public String visit(ExpressionNull expr) {
        // Como 'nulo' no tiene operaciones internas ni variables que buscar,
        // simplemente retornamos su tipo de dato oficial.

        // Vi en tu clase Environment que aceptas "nulo" o "null".
        // Retornamos "nulo" (o si tienes un TokenType.PRIMITIVE_NULL, pon ese).
        return "nulo";
    }
}