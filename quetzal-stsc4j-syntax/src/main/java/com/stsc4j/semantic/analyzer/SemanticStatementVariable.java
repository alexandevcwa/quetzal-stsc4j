package com.stsc4j.semantic.analyzer;

import com.stsc4j.lexer.TokenType;
import com.stsc4j.parser.v1.ast.StatementVariable;
import com.stsc4j.semantic.SemanticAbstractAnalyzer;
import com.stsc4j.semantic.SemanticAnalyzer;
import com.stsc4j.semantic.SemanticError;

public class SemanticStatementVariable extends SemanticAbstractAnalyzer {

    // ¡Ya no guardamos el Environment aquí! Solo al director.
    private final SemanticAnalyzer analyzer;

    public SemanticStatementVariable(SemanticAnalyzer analyzer) {
        this.analyzer = analyzer;
    }

    @Override
    public String visit(StatementVariable statementVariable) {
        String tipoReal = null;
        if (statementVariable.initialValue != null) {
            tipoReal = statementVariable.initialValue.accept(analyzer);
        }

        String nombreVariable = statementVariable.name.getLexeme();
        TokenType tokenTipo = statementVariable.type.getType();

        if (tokenTipo == TokenType.IDENTIFIER) {
            // Usamos analyzer.getEnv()
            analyzer.getEnv().assign(nombreVariable, tipoReal);
        }
        else {
            String tipoEsperado = tokenTipo.name();
            boolean sonCompatibles = false;

            if (tipoReal == null) {
                sonCompatibles = true;
            }
            else if (tipoEsperado.equals(tipoReal)) {
                sonCompatibles = true;
            }
            else {
                String tokenNumero = TokenType.PRIMITIVE_DECIMAL.name();
                String tokenEntero = TokenType.PRIMITIVE_INTEGER.name();
                if (tipoEsperado.equals(tokenNumero) && tipoReal.equals(tokenEntero)) {
                    sonCompatibles = true;
                }
            }

            if (tipoReal != null && !sonCompatibles) {
                String tipoRealUser = tipoReal.replace("PRIMITIVE_", "").toLowerCase();
                String tipoEsperadoUser = tipoEsperado.replace("PRIMITIVE_", "").toLowerCase();
                throw new SemanticError("Error Semántico: Trataste de guardar un dato de tipo '" + tipoRealUser + "' en una variable definida como '" + tipoEsperadoUser + "'.");
            }

            // Usamos analyzer.getEnv() para guardar en la memoria temporal correcta
            analyzer.getEnv().define(nombreVariable, tipoEsperado, statementVariable.mutable);
        }

        return null;
    }
}