package com.stsc4j.semantic.analyzer;

import com.stsc4j.lexer.TokenType;
import com.stsc4j.parser.v1.ast.StatementLoopWhile;
import com.stsc4j.semantic.Environment;
import com.stsc4j.semantic.SemanticAbstractAnalyzer;
import com.stsc4j.semantic.SemanticAnalyzer;
import com.stsc4j.semantic.SemanticError;

public class SemanticStatementLoopWhile extends SemanticAbstractAnalyzer {

    private final Environment currentEnv;
    private final SemanticAnalyzer analyzer;

    public SemanticStatementLoopWhile(Environment currentEnv, SemanticAnalyzer analyzer) {
        this.currentEnv = currentEnv;
        this.analyzer = analyzer;
    }

    @Override
    public String visit(StatementLoopWhile statementLoopWhile) {
        String tipoCondicion = statementLoopWhile.condition.accept(analyzer);
        String booleanoReal = TokenType.PRIMITIVE_BOOLEAN.name();

        if (tipoCondicion != null && !tipoCondicion.equals(booleanoReal)) {
            throw new SemanticError("Error Semántico: La condición del ciclo 'mientras' (while) debe ser booleana, pero se encontró: " + tipoCondicion);
        }

        analyzer.enterLoop();

        try {
            if (statementLoopWhile.block != null) {
                statementLoopWhile.block.accept(analyzer);
            }
        } finally {
            analyzer.exitLoop();
        }

        return null;
    }
}