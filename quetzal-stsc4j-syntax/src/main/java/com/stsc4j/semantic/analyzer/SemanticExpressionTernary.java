package com.stsc4j.semantic.analyzer;

import com.stsc4j.lexer.TokenType;
import com.stsc4j.parser.v1.ast.ExpressionTernary;
import com.stsc4j.semantic.SemanticAbstractAnalyzer;
import com.stsc4j.semantic.SemanticAnalyzer;
import com.stsc4j.semantic.SemanticError;

public class SemanticExpressionTernary extends SemanticAbstractAnalyzer {

    private final SemanticAnalyzer analyzer;

    public SemanticExpressionTernary(SemanticAnalyzer analyzer) {
        this.analyzer = analyzer;
    }

    @Override
    public String visit(ExpressionTernary expr) {
        // 1. Validamos la condición a través del director central
        String tipoCondicion = expr.binary.accept(analyzer);

        if (tipoCondicion != null && !tipoCondicion.equals(TokenType.PRIMITIVE_BOOLEAN.name())) {
            throw new SemanticError("Error Semántico: La condición del operador ternario debe ser de tipo lógico (booleano), pero es '" + tipoCondicion + "'.");
        }

        // 2. Evaluamos qué tipo de dato devuelve cada rama
        String tipoIzquierdo = expr.left.accept(analyzer);
        String tipoDerecho = expr.right.accept(analyzer);

        // 3. Validar que ambas ramas devuelvan cosas compatibles
        if (tipoIzquierdo != null && tipoDerecho != null) {

            // Si son exactamente iguales (ej. texto y texto), todo está perfecto
            if (tipoIzquierdo.equals(tipoDerecho)) {
                return tipoIzquierdo;
            }

            // Coerción Numérica: Si uno es entero y el otro decimal, se ensancha a decimal sin lanzar error
            boolean izqEsNum = tipoIzquierdo.equals(TokenType.PRIMITIVE_INTEGER.name()) || tipoIzquierdo.equals(TokenType.PRIMITIVE_DECIMAL.name());
            boolean derEsNum = tipoDerecho.equals(TokenType.PRIMITIVE_INTEGER.name()) || tipoDerecho.equals(TokenType.PRIMITIVE_DECIMAL.name());

            if (izqEsNum && derEsNum) {
                return TokenType.PRIMITIVE_DECIMAL.name(); // Retornamos el tipo más amplio
            }

            // Si son cosas completamente distintas (ej. texto y entero), explota
            throw new SemanticError("Error Semántico: El ternario devuelve tipos incompatibles: '" + tipoIzquierdo + "' y '" + tipoDerecho + "'.");
        }

        return tipoIzquierdo;
    }
}