package com.stsc4j.semantic.analyzer;

import com.stsc4j.parser.v1.ast.ExpressionVariable;
import com.stsc4j.parser.v1.ast.StatementPropertyAssignation;
import com.stsc4j.semantic.Environment;
import com.stsc4j.semantic.SemanticAbstractAnalyzer;
import com.stsc4j.semantic.SemanticAnalyzer;
import com.stsc4j.semantic.SemanticError;

public class SemanticStatementPropertyAssignation extends SemanticAbstractAnalyzer {

    private final SemanticAnalyzer analyzer;

    public SemanticStatementPropertyAssignation(SemanticAnalyzer analyzer) {
        this.analyzer = analyzer;
    }

    @Override
    public String visit(StatementPropertyAssignation stmt) {
        // 1. Evaluamos el lado derecho (el valor que se va a asignar) para buscar errores internos
        if (stmt.expression != null) {
            stmt.expression.accept(analyzer);
        }

        // 2. Evaluamos el lado izquierdo (el acceso a la propiedad) para asegurar que el objeto exista
        if (stmt.property != null) {
            stmt.property.accept(analyzer);

            // 3. REGLA DE INMUTABILIDAD PARA JSN:
            // Buscamos si el objeto raíz es una variable directa (ej. persona.nombre = ...)
            // *Asumimos que stmt.property tiene un campo llamado 'object'. Ajusta si Alex lo llamó distinto.*
            if (stmt.property.object instanceof ExpressionVariable) {
                ExpressionVariable varExpr = (ExpressionVariable) stmt.property.object;
                String nombreObjeto = varExpr.token.getLexeme();

                // Buscamos la variable en la memoria
                Environment.VariableInfo info = analyzer.getEnv().getVariable(nombreObjeto);

                // Si no es mutable (no tiene 'var'), lanzamos el error
                if (info != null && !info.isMutable) {
                    throw new SemanticError("Error Semántico: No puedes modificar la propiedad de '" + nombreObjeto + "' porque el objeto es una constante. Decláralo con 'var'.");
                }
            }
        }

        // Las asignaciones (Statements) no devuelven un tipo de dato
        return null;
    }
}