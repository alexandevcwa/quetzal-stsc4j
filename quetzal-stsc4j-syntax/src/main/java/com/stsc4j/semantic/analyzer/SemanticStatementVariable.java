package com.stsc4j.semantic.analyzer;

import com.stsc4j.parser.v1.ast.StatementVariable;
import com.stsc4j.semantic.Environment;
import com.stsc4j.semantic.SemanticAbstractAnalyzer;
import com.stsc4j.semantic.SemanticAnalyzer;
import com.stsc4j.semantic.SemanticError;

public class SemanticStatementVariable extends SemanticAbstractAnalyzer {

    private final Environment currentEnv;
    private final SemanticAnalyzer analyzer;

    public SemanticStatementVariable(Environment currentEnv, SemanticAnalyzer analyzer) {
        this.currentEnv = currentEnv;
        this.analyzer = analyzer;
    }

    @Override
    public String visit(StatementVariable statementVariable) {
        // 1. Evaluamos lo que hay del lado derecho del igual (delegando al director)
        String tipoReal = null;
        if (statementVariable.initialValue != null) {
            tipoReal = statementVariable.initialValue.accept(analyzer);
        }

        String nombreVariable = statementVariable.name.getLexeme();

        // 2. TRUCO DEL PARSER:
        // Si el tipo es un IDENTIFIER, significa que es una reasignación (ej. a = 20)
        if (statementVariable.type.getType().toString().equals("IDENTIFIER")) {

            // La memoria (Environment) se encargará de validar si se puede reasignar
            currentEnv.assign(nombreVariable, tipoReal);

        } else {
            // 3. ES UNA DECLARACIÓN NUEVA (ej. entero a = 20 o decimal b = 15)
            String tipoEsperado = statementVariable.type.getLexeme();

            // Lógica de flexibilidad de tipos (Coerción implícita)
            boolean sonCompatibles = false;

            if (tipoReal == null) {
                // Si la declaran sin valor (ej. entero a;), es válido
                sonCompatibles = true;
            } else if (tipoEsperado.equals(tipoReal)) {
                // Son exactamente iguales (entero=entero, cadena=cadena)
                sonCompatibles = true;
            } else if (tipoEsperado.equals("número") && tipoReal.equals("entero")) {
                // Ensanchamiento seguro: Permitimos guardar un entero dentro de un decimal
                sonCompatibles = true;
            } else if (tipoEsperado.equals("número") && (tipoReal.equals("entero") || tipoReal.equals("decimal") || tipoReal.equals("número"))) {
                // Ahora sí: Una variable 'número' acepta enteros, decimales y otros números.
                sonCompatibles = true;
            }

            // Si detectamos que los tipos chocan y no son compatibles, lanzamos el error
            if (tipoReal != null && !sonCompatibles) {
                throw new SemanticError("Error Semántico: Trataste de guardar un dato tipo '" + tipoReal + "' en una variable de tipo '" + tipoEsperado + "'.");
            }

            // Guardamos la nueva variable en la memoria con su tipo y su bandera de mutabilidad
            currentEnv.define(nombreVariable, tipoEsperado, statementVariable.mutable);
        }

        return null;
    }
}