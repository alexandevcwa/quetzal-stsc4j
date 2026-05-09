package com.stsc4j.semantic.analyzer;

import com.stsc4j.parser.v1.ast.ExpressionBinary;
import com.stsc4j.semantic.Environment;
import com.stsc4j.semantic.SemanticAbstractAnalyzer;
import com.stsc4j.semantic.SemanticAnalyzer;
import com.stsc4j.semantic.SemanticError;

public class SemanticExpressionBinary extends SemanticAbstractAnalyzer {

    private final Environment currentEnv;
    private final SemanticAnalyzer analyzer;

    public SemanticExpressionBinary(Environment currentEnv, SemanticAnalyzer analyzer) {
        this.currentEnv = currentEnv;
        this.analyzer = analyzer;
    }

    @Override
    public String visit(ExpressionBinary expressionBinary) {
        // Pedimos al director que evalúe ambos lados de la operación
        String tipoIzquierdo = expressionBinary.left.accept(analyzer);
        String tipoDerecho = expressionBinary.right.accept(analyzer);

        // 1. Si son exactamente iguales, bien (ej. entero + entero -> entero)
        if (tipoIzquierdo != null && tipoDerecho != null && tipoIzquierdo.equals(tipoDerecho)) {
            return tipoIzquierdo;
        }

        // 2. Lógica de flexibilidad (Coerción) para mezclar números
        if (tipoIzquierdo != null && tipoDerecho != null) {
            boolean izquierdoEsNum = tipoIzquierdo.equals("entero") || tipoIzquierdo.equals("número");
            boolean derechoEsNum = tipoDerecho.equals("entero") || tipoDerecho.equals("número");

            // Si ambos son números (es decir, uno es entero y el otro decimal)
            if (izquierdoEsNum && derechoEsNum) {
                // En una mezcla matemática, el resultado siempre "se ensancha" a decimal para no perder datos
                return "número";
            } else {
                // Si intentan operar un entero con una cadena (y no lo tienes permitido) o un booleano, estalla.
                throw new SemanticError("No puedes operar entre tipos diferentes incompatibles: " + tipoIzquierdo + " con un " + tipoDerecho);
            }
        }

        return null;
    }
}