package com.stsc4j.semantic.analyzer;

import com.stsc4j.parser.v1.ast.Statement;
import com.stsc4j.parser.v1.ast.StatementBlock;
import com.stsc4j.semantic.Environment;
import com.stsc4j.semantic.SemanticAbstractAnalyzer;

public class SemanticStatementBlock extends SemanticAbstractAnalyzer {

    private Environment currentEnv;

    public SemanticStatementBlock (Environment currentEnv) {
        this.currentEnv = currentEnv;
    }

    @Override
    public String visit(StatementBlock statementBlock) {
        // Cuando entramos a un {}, creamos una memoria temporal
        Environment previousEnv = this.currentEnv;
        this.currentEnv = new Environment(previousEnv);

        try {
            // Analizamos lo de adentro con el nuevo entorno
            for (Statement stmt : statementBlock.statements) {
                stmt.accept(this);
            }
        } finally {
            // Al salir del {}, destruimos el entorno local y regresamos al anterior
            this.currentEnv = previousEnv;
        }
        return null;
    }

}
