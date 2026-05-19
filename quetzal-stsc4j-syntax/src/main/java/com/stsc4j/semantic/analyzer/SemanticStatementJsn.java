package com.stsc4j.semantic.analyzer;

import com.stsc4j.parser.v1.ast.StatementJsn;
import com.stsc4j.semantic.SemanticAbstractAnalyzer;
import com.stsc4j.semantic.SemanticAnalyzer;

public class SemanticStatementJsn extends SemanticAbstractAnalyzer {

    private final SemanticAnalyzer analyzer;

    public SemanticStatementJsn(SemanticAnalyzer analyzer) {
        this.analyzer = analyzer;
    }

    @Override
    public String visit(StatementJsn statementJsn) {
        // 1. Analizamos el bloque interno {...} pasando el director central
        if (statementJsn.block != null) {
            statementJsn.block.accept(analyzer);
        }

        // 2. Extraemos los datos de la declaración del AST
        String nombreVariable = statementJsn.identifier.getLexeme();
        boolean esMutable = statementJsn.mutable; // ¡Usamos la mutabilidad real del código!

        // 3. Registramos el objeto en la tabla de símbolos actual como tipo "jsn"
        analyzer.getEnv().define(nombreVariable, "jsn", esMutable);

        return null;
    }
}