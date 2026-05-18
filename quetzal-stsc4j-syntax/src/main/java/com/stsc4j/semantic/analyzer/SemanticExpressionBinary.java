package com.stsc4j.semantic.analyzer;

import com.stsc4j.lexer.Token;
import com.stsc4j.lexer.TokenType;
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
        // 1. Pedimos al director que evalúe ambos lados
        String tipoIzq = expressionBinary.left.accept(analyzer);
        String tipoDer = expressionBinary.right.accept(analyzer);

        // 2. Extraemos el símbolo del operador (sea un token simple como "+" o doble como "==")
        String operador = "";
        if (expressionBinary.operator != null) {
            operador = expressionBinary.operator.getLexeme();
        } else if (expressionBinary.operators != null) {
            StringBuilder sb = new StringBuilder();
            for (Token t : expressionBinary.operators) sb.append(t.getLexeme());
            operador = sb.toString();
        }

        // Nombres técnicos para fácil lectura
        String ENTERO = TokenType.PRIMITIVE_INTEGER.name();
        String DECIMAL = TokenType.PRIMITIVE_DECIMAL.name();
        String TEXTO = TokenType.PRIMITIVE_STRING.name();
        String LOGICO = TokenType.PRIMITIVE_BOOLEAN.name();

        boolean izqEsNum = tipoIzq.equals(ENTERO) || tipoIzq.equals(DECIMAL);
        boolean derEsNum = tipoDer.equals(ENTERO) || tipoDer.equals(DECIMAL);

        // 3. REGLAS SEMÁNTICAS POR OPERADOR
        switch (operador) {
            // --- MATEMÁTICAS ---
            case "+":
                // Concatenación: Si alguno de los dos lados es un texto, el resultado es texto.
                if (tipoIzq.equals(TEXTO) || tipoDer.equals(TEXTO)) return TEXTO;

                // Suma numérica
                if (izqEsNum && derEsNum) {
                    return (tipoIzq.equals(DECIMAL) || tipoDer.equals(DECIMAL)) ? DECIMAL : ENTERO;
                }
                throw new SemanticError("Error Semántico: El operador '+' solo puede usarse entre números o para concatenar textos.");

            case "-": case "*": case "/": case "%":
                // Matemáticas puras (Ensanchamos a decimal si hay mezcla)
                if (izqEsNum && derEsNum) {
                    return (tipoIzq.equals(DECIMAL) || tipoDer.equals(DECIMAL)) ? DECIMAL : ENTERO;
                }
                throw new SemanticError("Error Semántico: El operador '" + operador + "' solo puede usarse con números.");

                // --- RELACIONALES (Devuelven Booleano) ---
            case ">": case "<": case ">=": case "<=":
                if (izqEsNum && derEsNum) return LOGICO;
                throw new SemanticError("Error Semántico: Los operadores '" + operador + "' solo sirven para comparar números.");

                // --- IGUALDAD (Devuelven Booleano) ---
            case "==": case "!=":
                // Puedes comparar números cruzados (ej. 10 == 10.0)
                if (izqEsNum && derEsNum) return LOGICO;
                // O puedes comparar cosas exactamente iguales (texto == texto, logico == logico)
                if (tipoIzq.equals(tipoDer)) return LOGICO;
                throw new SemanticError("Error Semántico: No puedes comparar igualdad entre tipos incompatibles (" + tipoIzq + " y " + tipoDer + ").");

                // --- LÓGICOS (Devuelven Booleano) ---
            case "&&": case "||":
            case "y": case "o": // Si tu lenguaje usa español para and/or
                if (tipoIzq.equals(LOGICO) && tipoDer.equals(LOGICO)) return LOGICO;
                throw new SemanticError("Error Semántico: Los operadores lógicos ('" + operador + "') solo pueden evaluar valores booleanos/lógicos.");

            default:
                throw new SemanticError("Operador binario desconocido: " + operador);
        }
    }
}