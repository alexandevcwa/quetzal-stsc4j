package com.stsc4j.semantic.analyzer;

import com.stsc4j.parser.v1.ast.TypeList;
import com.stsc4j.semantic.SemanticAbstractAnalyzer;
import com.stsc4j.semantic.SemanticAnalyzer;

public class SemanticTypeList extends SemanticAbstractAnalyzer {

    private final SemanticAnalyzer analyzer;

    public SemanticTypeList(SemanticAnalyzer analyzer) {
        this.analyzer = analyzer;
    }

    @Override
    public String visit(TypeList typeList) {
        if (typeList.elementType != null) {
            // Le pedimos al director que evalúe qué hay adentro de los < >
            // Puede ser un TypePrimitive o ¡otro TypeList!
            String innerType = typeList.elementType.accept(analyzer);
            return "lista<" + innerType + ">";
        }

        // Si no tiene tipo, es una lista dinámica mixta
        return "lista";
    }
}