package com.stsc4j.semantic.analyzer;

import com.stsc4j.lexer.TokenType;
import com.stsc4j.parser.v1.ast.StatementIf;
import com.stsc4j.semantic.Environment;
import com.stsc4j.semantic.SemanticAbstractAnalyzer;
import com.stsc4j.semantic.SemanticAnalyzer;
import com.stsc4j.semantic.SemanticError;

public class SemanticStatementIf extends SemanticAbstractAnalyzer {

    private final Environment currentEnv;
    private final SemanticAnalyzer analyzer;

    public SemanticStatementIf(Environment currentEnv, SemanticAnalyzer analyzer) {
        this.currentEnv = currentEnv;
        this.analyzer = analyzer;
    }

    @Override
    public String visit(StatementIf statementIf) {
        // 1. Evaluamos la condición delegando al director
        String tipoCondicion = statementIf.condition.accept(analyzer);
        String booleanoReal = TokenType.PRIMITIVE_BOOLEAN.name();

        if (tipoCondicion != null && !tipoCondicion.equals(booleanoReal)) {
            throw new SemanticError("Error Semántico: La condición del 'si' (if) debe ser booleana, pero se encontró: " + tipoCondicion);
        }

        // 2. Evaluamos los bloques (SemanticStatementBlock se encargará de crear el entorno)
        if (statementIf.thenStatement != null) {
            statementIf.thenStatement.accept(analyzer);
        }

        if (statementIf.elseStatement != null) {
            statementIf.elseStatement.accept(analyzer);
        }

        return null;
    }
}