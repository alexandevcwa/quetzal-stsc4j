package com.stsc4j.semantic.analyzer;

import com.stsc4j.lexer.TokenType;
import com.stsc4j.parser.v1.ast.StatementVariable;
import com.stsc4j.semantic.SemanticAbstractAnalyzer;
import com.stsc4j.semantic.SemanticAnalyzer;
import com.stsc4j.semantic.SemanticError;

public class SemanticStatementVariable extends SemanticAbstractAnalyzer {

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
            analyzer.getEnv().assign(nombreVariable, tipoReal);
        }
        else {
            String tipoEsperado = tokenTipo.name();
            boolean sonCompatibles = false;

            // EL PARCHE: Si no hay valor o el valor es literalmente "nulo" o "null", lo aceptamos
            if (tipoReal == null || tipoReal.equals("nulo") || tipoReal.equals("null")) {
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

            if (!sonCompatibles) {
                String tipoRealUser = tipoReal.replace("PRIMITIVE_", "").toLowerCase();
                String tipoEsperadoUser = tipoEsperado.replace("PRIMITIVE_", "").toLowerCase();
                throw new SemanticError("Error Semántico: Trataste de guardar un dato de tipo '" + tipoRealUser + "' en una variable definida como '" + tipoEsperadoUser + "'.");
            }

            analyzer.getEnv().define(nombreVariable, tipoEsperado, statementVariable.mutable);
        }

        return null;
    }
}