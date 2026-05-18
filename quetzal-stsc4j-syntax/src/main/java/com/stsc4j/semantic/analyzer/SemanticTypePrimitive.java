package com.stsc4j.semantic.analyzer;

import com.stsc4j.parser.v1.ast.TypePrimitive;
import com.stsc4j.semantic.Environment;
import com.stsc4j.semantic.SemanticAbstractAnalyzer;

public class SemanticTypePrimitive extends SemanticAbstractAnalyzer {

    private final Environment currentEnv;

    public SemanticTypePrimitive(Environment currentEnv) {
        this.currentEnv = currentEnv;
    }

    @Override
    public String visit(TypePrimitive typePrimitive) {
        // Revisa tu clase TypePrimitive en la carpeta AST.
        return typePrimitive.primitiveType.getLexeme();
    }
}