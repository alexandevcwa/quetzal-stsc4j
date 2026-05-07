package com.stsc4j.semantic.analyzer;

import com.stsc4j.parser.v1.ast.ExpressionLiteral;
import com.stsc4j.semantic.Environment;
import com.stsc4j.semantic.SemanticAbstractAnalyzer;

public class SemanticExpressionLiteral extends SemanticAbstractAnalyzer {

    private final Environment currentEnv;

    public SemanticExpressionLiteral (Environment currentEnv) {
        this.currentEnv = currentEnv;
    }

    @Override
    public String visit(ExpressionLiteral expressionLiteral) {
        String tipoToken = expressionLiteral.token.getType().toString();

        if (tipoToken.contains("INTEGER") || tipoToken.contains("ENTERO")) {
            return "entero";
        } else if (tipoToken.contains("STRING") || tipoToken.contains("CADENA")) {
            return "cadena";
        } else if (tipoToken.contains("DECIMAL") || tipoToken.contains("FLOAT")) {
            return "decimal";
        } else if (tipoToken.contains("BOOLEAN") || tipoToken.contains("BOOL") || tipoToken.contains("TRUE") || tipoToken.contains("FALSE")) {
            // ¡Aquí agregamos TRUE y FALSE para tu lexer!
            return "booleano";
        }

        return "desconocido";
    }

}
