package com.stsc4j.semantic.analyzer;

import com.stsc4j.parser.v1.ast.StatementVariable;
import com.stsc4j.semantic.Environment;
import com.stsc4j.semantic.SemanticAbstractAnalyzer;
import com.stsc4j.semantic.SemanticError;

public class SemanticStatementVariable extends SemanticAbstractAnalyzer {

    private final Environment currentEnv;

    public SemanticStatementVariable(Environment currentEnv) {
        this.currentEnv = currentEnv;
    }

    @Override
    public String visit (StatementVariable statementVariable) {
        // Evaluamos lo que hay del lado derecho del igual
        String tipoReal = statementVariable.initialValue.accept(this);
        String nombreVariable = statementVariable.name.getLexeme();

        // TRUCO DEL PARSER:
        // Si el tipo es un IDENTIFIER, significa que es una reasignación (ej. a = 20)
        // porque no tiene palabra clave como 'entero' o 'cadena' al inicio.
        if (statementVariable.typo.getType().toString().equals("IDENTIFIER")) {

            // Tratamos de reasignar. Nuestro Environment se encargará de lanzar
            // error si la variable es inmutable o si los tipos no cuadran.
            currentEnv.assign(nombreVariable, tipoReal);

        } else {
            // ES UNA DECLARACIÓN NUEVA (ej. entero a = 20)
            String tipoEsperado = statementVariable.typo.getLexeme();

            if (tipoReal != null && !tipoEsperado.equals(tipoReal)) {
                throw new SemanticError("Trataste de guardar un dato tipo " + tipoReal + " en una variable de tipo " + tipoEsperado);
            }

            // Aquí le pasamos el booleano 'isMutable' que tu compañero preparó en el AST
            currentEnv.define(nombreVariable, tipoEsperado, statementVariable.mutable);
        }

        return null;
    }
}
