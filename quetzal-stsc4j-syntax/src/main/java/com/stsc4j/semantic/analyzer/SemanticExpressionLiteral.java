package com.stsc4j.semantic.analyzer;

import com.stsc4j.lexer.TokenType;
import com.stsc4j.parser.v1.ast.ExpressionLiteral;
import com.stsc4j.semantic.Environment;
import com.stsc4j.semantic.SemanticAbstractAnalyzer;

public class SemanticExpressionLiteral extends SemanticAbstractAnalyzer {

    private final Environment currentEnv;

    public SemanticExpressionLiteral(Environment currentEnv) {
        this.currentEnv = currentEnv;
    }

    @Override
    public String visit(ExpressionLiteral expressionLiteral) {
        TokenType tipoToken = expressionLiteral.token.getType();

        switch (tipoToken) {
            case LIT_INTEGER:
                return "entero";
            case LIT_STRING:
                // Ajustado al tipo oficial de Quetzal
                return "texto";
            case LIT_DECIMAL:
                // Ajustado al tipo oficial de Quetzal
                return "número";
            case LIT_TRUE:
            case LIT_FALSE:
                return "log";
            default:
                return "desconocido";
        }
    }
}