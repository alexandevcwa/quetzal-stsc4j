package com.stsc4j.semantic.analyzer;

import com.stsc4j.lexer.TokenType;
import com.stsc4j.parser.v1.ast.StatementReturn;
import com.stsc4j.semantic.SemanticAbstractAnalyzer;
import com.stsc4j.semantic.SemanticAnalyzer;
import com.stsc4j.semantic.SemanticError;

public class SemanticStatementReturn extends SemanticAbstractAnalyzer {

    private final SemanticAnalyzer analyzer;

    public SemanticStatementReturn(SemanticAnalyzer analyzer) {
        this.analyzer = analyzer;
    }

    @Override
    public String visit(StatementReturn stmt) {
        // 1. Preguntamos a la memoria qué tipo prometimos devolver
        String tipoEsperado;
        try {
            tipoEsperado = analyzer.getEnv().resolveType("@return");
        } catch (SemanticError e) {
            throw new SemanticError("Error Semántico: La instrucción 'retornar' no puede usarse fuera de una función.");
        }

        // 2. Evaluamos lo que realmente estamos retornando
        String tipoReal = stmt.returnExpression.accept(analyzer);

        // 3. Lógica de compatibilidad y coerción
        boolean compatible = tipoEsperado.equals(tipoReal);

        // Coerción: Si prometimos devolver un DECIMAL, pero retornamos un ENTERO, es válido.
        if (tipoEsperado.equals(TokenType.PRIMITIVE_DECIMAL.name()) && tipoReal.equals(TokenType.PRIMITIVE_INTEGER.name())) {
            compatible = true;
        }

        if (!compatible) {
            String expUser = tipoEsperado.replace("PRIMITIVE_", "").toLowerCase();
            String realUser = tipoReal.replace("PRIMITIVE_", "").toLowerCase();
            throw new SemanticError("Error Semántico: La función promete devolver '" + expUser + "', pero estás retornando '" + realUser + "'.");
        }

        return null;
    }
}