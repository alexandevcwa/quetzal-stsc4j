package com.stsc4j.semantic.analyzer;

import com.stsc4j.parser.v1.ast.*;
import com.stsc4j.semantic.Environment;
import com.stsc4j.semantic.SemanticAbstractAnalyzer;
import com.stsc4j.semantic.SemanticAnalyzer;
import java.util.ArrayList;
import java.util.List;

public class SemanticStatementFunction extends SemanticAbstractAnalyzer {

    private final SemanticAnalyzer analyzer;

    public SemanticStatementFunction(SemanticAnalyzer analyzer) {
        this.analyzer = analyzer;
    }

    // FASE 1: Solo registrar la firma
    public void register(StatementFunction stmt) {
        String funcName = stmt.identified.getLexeme();
        String returnType = stmt.returnValue.getType().name();
        List<String> paramTypes = new ArrayList<>();
        for (Statement paramStmt : stmt.parameters) {
            StatementFunctionParameter param = (StatementFunctionParameter) paramStmt;
            paramTypes.add(param.type.getType().name());
        }
        analyzer.getEnv().defineFunction(funcName, returnType, paramTypes);
    }

    // FASE 2: Analizar el bloque interno
    public void analyzeBody(StatementFunction stmt) {
        String returnType = stmt.returnValue.getType().name();

        Environment globalEnv = analyzer.getEnv();
        analyzer.setEnv(new Environment(globalEnv)); // Nuevo Scope

        try {
            analyzer.getEnv().define("@return", returnType, false);
            for (Statement paramStmt : stmt.parameters) {
                paramStmt.accept(analyzer);
            }
            stmt.block.accept(analyzer);
        } finally {
            analyzer.setEnv(globalEnv); // Restaurar Scope
        }
    }

    @Override
    public String visit(StatementFunction stmt) {
        return null; // Ya no hacemos nada aquí directamente
    }
}