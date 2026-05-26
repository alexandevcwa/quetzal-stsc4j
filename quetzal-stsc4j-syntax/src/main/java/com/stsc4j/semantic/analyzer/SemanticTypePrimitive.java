package com.stsc4j.semantic.analyzer;

import com.stsc4j.parser.v1.ast.TypePrimitive;
import com.stsc4j.semantic.SemanticAbstractAnalyzer;
import com.stsc4j.semantic.SemanticAnalyzer;

public class SemanticTypePrimitive extends SemanticAbstractAnalyzer {

    private final SemanticAnalyzer analyzer;

    public SemanticTypePrimitive(SemanticAnalyzer analyzer) {
        this.analyzer = analyzer;
    }

    @Override
    public String visit(TypePrimitive typePrimitive) {
        // En lugar de devolver "entero" o "texto", devolvemos "PRIMITIVE_INTEGER"
        // que es lo que  nuestro motor semántico usa para comparar.
        return typePrimitive.primitiveType.getType().name();
    }
}