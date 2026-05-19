package com.stsc4j.semantic.analyzer;

import com.stsc4j.parser.v1.ast.StatementFunctionParameter;
import com.stsc4j.semantic.SemanticAbstractAnalyzer;
import com.stsc4j.semantic.SemanticAnalyzer;

public class SemanticStatementFunctionParameter extends SemanticAbstractAnalyzer {

    private final SemanticAnalyzer analyzer;

    public SemanticStatementFunctionParameter(SemanticAnalyzer analyzer) {
        this.analyzer = analyzer;
    }

    @Override
    public String visit(StatementFunctionParameter stmt) {
        // 1. Extraemos el tipo oficial (ej. PRIMITIVE_INTEGER) y el nombre
        String tipoParametro = stmt.type.getType().name();
        String nombreParametro = stmt.identified.getLexeme();

        // 2. Inyectamos el parámetro en la memoria local actual respetando si tiene 'var' (mutable)
        analyzer.getEnv().define(nombreParametro, tipoParametro, stmt.mutable);

        // 3. Devolvemos el tipo del parámetro (por si el nodo padre lo necesita)
        return tipoParametro;
    }
}