package com.stsc4j.semantic.analyzer;

import com.stsc4j.lexer.TokenType;
import com.stsc4j.parser.v1.ast.ExpressionVariable;
import com.stsc4j.parser.v1.ast.StatementMatrixAssignation;
import com.stsc4j.semantic.Environment;
import com.stsc4j.semantic.SemanticAbstractAnalyzer;
import com.stsc4j.semantic.SemanticAnalyzer;
import com.stsc4j.semantic.SemanticError;

public class SemanticStatementMatrxiAssignation extends SemanticAbstractAnalyzer {

    private final SemanticAnalyzer analyzer;

    public SemanticStatementMatrxiAssignation(SemanticAnalyzer analyzer) {
        this.analyzer = analyzer;
    }

    @Override
    public String visit(StatementMatrixAssignation stmt) {
        // 1. Evaluar el lado derecho (el valor que se va a asignar a la celda)
        String tipoAsignado = stmt.expression.accept(analyzer);

        // 2. Evaluar el lado izquierdo (el acceso a la matriz/lista)
        // Esto debería ir a la clase SemanticExpressionIndexAccess y devolver
        // el tipo de dato que guarda la lista. ¡Esa clase ya debería validar que el índice sea entero!
        String tipoMatriz = stmt.matrix.accept(analyzer);

        // 3. REGLA DE INMUTABILIDAD:
        // Extraemos el nombre del arreglo para ver si tiene 'var'.
        if (stmt.matrix.objectList instanceof ExpressionVariable) {
            ExpressionVariable varExpr = (ExpressionVariable) stmt.matrix.objectList;
            String nombreArreglo = varExpr.token.getLexeme();

            // Buscamos el arreglo en la memoria
            Environment.VariableInfo info = analyzer.getEnv().getVariable(nombreArreglo);

            // Si es una constante (no tiene 'var'), bloqueamos la modificación
            if (info != null && !info.isMutable) {
                throw new SemanticError("Error Semántico: No puedes modificar la posición del arreglo '" + nombreArreglo + "' porque es una constante. Decláralo con 'var'.");
            }
        }

        // 4. REGLA DE TIPO: ¿El valor coincide con el tipo de la lista?
        boolean compatible = false;

        // Si la matriz devolvió 'null', significa que es de tipo 'mixto' (dinámico)
        if (tipoMatriz == null) {
            compatible = true; // Una lista dinámica acepta cualquier cosa
        } else {
            // Si tiene un tipo estricto, comparamos
            compatible = tipoMatriz.equals(tipoAsignado);

            // Coerción (Ensanchamiento seguro)
            if (tipoMatriz.equals(TokenType.PRIMITIVE_DECIMAL.name()) && tipoAsignado.equals(TokenType.PRIMITIVE_INTEGER.name())) {
                compatible = true;
            }
        }

        if (!compatible) {
            throw new SemanticError("Error Semántico: Conflicto de tipos. Intentas asignar un '" + tipoAsignado + "' a un arreglo que guarda '" + tipoMatriz + "'.");
        }

        // Como es un Statement (Instrucción), no retorna nada.
        return null;
    }
}