package com.stsc4j.semantic.analyzer;

import com.stsc4j.parser.v1.ast.StatementIncDec;
import com.stsc4j.semantic.Environment;
import com.stsc4j.semantic.SemanticAbstractAnalyzer;
import com.stsc4j.semantic.SemanticAnalyzer;
import com.stsc4j.semantic.SemanticError;

public class SemanticStatementIncDec extends SemanticAbstractAnalyzer {

    private final Environment currentEnv;
    private final SemanticAnalyzer analyzer;

    public SemanticStatementIncDec(Environment currentEnv, SemanticAnalyzer analyzer) {
        this.currentEnv = currentEnv;
        this.analyzer = analyzer;
    }

    @Override
    public String visit(StatementIncDec statementIncDec) {
        // Delegamos al analizador principal (NO a 'this')
        String tipoVariable = statementIncDec.expression.accept(analyzer);

        if (tipoVariable != null && !tipoVariable.equals("entero") && !tipoVariable.equals("decimal")) {
            throw new SemanticError("El operador de incremento/decremento como sentencia solo se aplica a números. Tipo actual: '" + tipoVariable + "'.");
        }

        return tipoVariable;
    }
}