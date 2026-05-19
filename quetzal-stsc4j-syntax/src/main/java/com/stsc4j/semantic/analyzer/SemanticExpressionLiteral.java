package com.stsc4j.semantic.analyzer;

import com.stsc4j.lexer.TokenType;
import com.stsc4j.parser.v1.ast.ExpressionLiteral;
import com.stsc4j.semantic.SemanticAbstractAnalyzer;
import com.stsc4j.semantic.SemanticAnalyzer;

public class SemanticExpressionLiteral extends SemanticAbstractAnalyzer {

    private final SemanticAnalyzer analyzer;

    public SemanticExpressionLiteral(SemanticAnalyzer analyzer) {
        this.analyzer = analyzer;
    }

    @Override
    public String visit(ExpressionLiteral expr) {
        TokenType tipoToken = expr.token.getType();

        switch (tipoToken) {
            case LIT_INTEGER:
                return TokenType.PRIMITIVE_INTEGER.name(); // "PRIMITIVE_INTEGER"
            case LIT_STRING:
                return TokenType.PRIMITIVE_STRING.name();  // "PRIMITIVE_STRING"
            case LIT_DECIMAL:
                return TokenType.PRIMITIVE_DECIMAL.name(); // "PRIMITIVE_DECIMAL"
            case LIT_TRUE:
            case LIT_FALSE:
                return TokenType.PRIMITIVE_BOOLEAN.name(); // "PRIMITIVE_BOOLEAN"
            default:
                // token de error
                return TokenType.UNKNOW.name();
        }
    }
}