package com.stsc4j.semantic.analyzer;

import com.stsc4j.parser.v1.ast.Statement;
import com.stsc4j.parser.v1.ast.StatementBlock;
import com.stsc4j.semantic.Environment;
import com.stsc4j.semantic.SemanticAbstractAnalyzer;
import com.stsc4j.semantic.SemanticAnalyzer;

public class SemanticStatementBlock extends SemanticAbstractAnalyzer {

    private final SemanticAnalyzer analyzer;

    // Solo necesitamos al director, él ya tiene la memoria adentro
    public SemanticStatementBlock(SemanticAnalyzer analyzer) {
        this.analyzer = analyzer;
    }

    @Override
    public String visit(StatementBlock statementBlock) {
        // 1. Extraemos la memoria actual del director
        Environment previousEnv = analyzer.getEnv();

        // 2. Le obligamos al director a usar una memoria nueva (hija de la anterior)
        analyzer.setEnv(new Environment(previousEnv));

        try {
            // 3. Analizamos el interior usando la nueva memoria
            for (Statement stmt : statementBlock.statements) {
                stmt.accept(analyzer);
            }
        } finally {
            // 4. ¡LA MUERTE DEL SCOPE! Al salir de las llaves, le devolvemos la memoria original al director.
            // Todas las variables locales declaradas adentro acaban de ser destruidas.
            analyzer.setEnv(previousEnv);
        }
        return null;
    }
}