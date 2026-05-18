package com.stsc4j.semantic.analyzer;

import com.stsc4j.lexer.TokenType;
import com.stsc4j.parser.v1.ast.StatementLoopDoWhile;
import com.stsc4j.semantic.SemanticAbstractAnalyzer;
import com.stsc4j.semantic.SemanticAnalyzer;
import com.stsc4j.semantic.SemanticError;

public class SemanticStatementLoopDoWhile extends SemanticAbstractAnalyzer {

    private final SemanticAnalyzer analyzer;

    public SemanticStatementLoopDoWhile(SemanticAnalyzer analyzer) {
        this.analyzer = analyzer;
    }

    @Override
    public String visit(StatementLoopDoWhile statementLoopDoWhile) {
        // 1. Analizamos el bloque de código
        if (statementLoopDoWhile.block != null) {
            statementLoopDoWhile.block.accept(analyzer);
        }

        // 2. Validamos estrictamente la condición con el director
        String tipoCondicion = statementLoopDoWhile.condition.accept(analyzer);
        String booleanoReal = TokenType.PRIMITIVE_BOOLEAN.name();

        if (tipoCondicion != null && !tipoCondicion.equals(booleanoReal)) {
            throw new SemanticError("Error Semántico: La condición del ciclo 'hacer-mientras' (do-while) debe ser booleana, pero se encontró un '" + tipoCondicion + "'.");
        }

        return null;
    }
}