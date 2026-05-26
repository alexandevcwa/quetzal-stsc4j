package com.stsc4j.semantic.analyzer;

import com.stsc4j.lexer.TokenType;
import com.stsc4j.parser.v1.ast.Expression;
import com.stsc4j.parser.v1.ast.Statement;
import com.stsc4j.parser.v1.ast.StatementLoopFor;
import com.stsc4j.semantic.Environment;
import com.stsc4j.semantic.SemanticAbstractAnalyzer;
import com.stsc4j.semantic.SemanticAnalyzer;
import com.stsc4j.semantic.SemanticError;

public class SemanticStatementLoopFor extends SemanticAbstractAnalyzer {

    private final SemanticAnalyzer analyzer;

    public SemanticStatementLoopFor (SemanticAnalyzer analyzer) {
        this.analyzer = analyzer;
    }

    @Override
    public String visit(StatementLoopFor statementLoopFor) {
        Environment entornoAnterior = analyzer.getEnv();
        analyzer.setEnv(new Environment(entornoAnterior));

        try {
            if (statementLoopFor.declaration instanceof Statement) {
                ((Statement) statementLoopFor.declaration).accept(analyzer);
            } else if (statementLoopFor.declaration instanceof Expression) {
                ((Expression) statementLoopFor.declaration).accept(analyzer);
            }

            String tipoCondicion = statementLoopFor.condition.accept(analyzer);
            if (tipoCondicion != null && !tipoCondicion.equals(TokenType.PRIMITIVE_BOOLEAN.name())) {
                throw new SemanticError("Error Semántico: La condición del ciclo 'para' debe ser de tipo 'booleano'.");
            }

            if (statementLoopFor.increment != null) {
                statementLoopFor.increment.accept(analyzer);
            }

            // ¡ENCENDEMOS EL RADAR!
            analyzer.enterLoop();
            try {
                if (statementLoopFor.block != null) {
                    statementLoopFor.block.accept(analyzer);
                }
            } finally {
                analyzer.exitLoop(); // ¡APAGAMOS EL RADAR!
            }

        } finally {
            analyzer.setEnv(entornoAnterior);
        }

        return null;
    }
}