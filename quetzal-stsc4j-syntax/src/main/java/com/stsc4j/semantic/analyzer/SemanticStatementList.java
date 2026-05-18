package com.stsc4j.semantic.analyzer;

import com.stsc4j.parser.v1.ast.Expression;
import com.stsc4j.parser.v1.ast.StatementList;
import com.stsc4j.semantic.Environment;
import com.stsc4j.semantic.SemanticAbstractAnalyzer;
import com.stsc4j.semantic.SemanticAnalyzer;
import com.stsc4j.semantic.SemanticError;

public class SemanticStatementList extends SemanticAbstractAnalyzer {

    private final Environment currentEnv;
    private final SemanticAnalyzer analyzer;

    public SemanticStatementList(Environment currentEnv, SemanticAnalyzer analyzer) {
        this.currentEnv = currentEnv;
        this.analyzer = analyzer;
    }

    @Override
    public String visit(StatementList statementList) {
        String nombreLista = statementList.listName.getLexeme();
        String tipoDeLaLista = "dinamico";

        // 1. Delegamos limpiamente al director. ¡Sin hacks!
        if (statementList.type != null && statementList.type.elementType != null) {
            tipoDeLaLista = statementList.type.elementType.accept(analyzer);
        }

        // 2. Fallback seguro en caso de que el tipo no se haya podido evaluar
        if (tipoDeLaLista == null) {
            tipoDeLaLista = "dinamico";
        }

        // 3. Validación de elementos
        if (!tipoDeLaLista.equals("dinamico") && statementList.expressionList != null) {
            for (Expression expr : statementList.expressionList.expressions) {

                String tipoElemento = expr.accept(analyzer);

                if (tipoElemento != null && !tipoElemento.equals(tipoDeLaLista)) {
                    throw new SemanticError("La lista '" + nombreLista + "' es de tipo " + tipoDeLaLista + " pero se encontró un elemento de tipo " + tipoElemento);
                }
            }
        }

        currentEnv.define(nombreLista, "lista<" + tipoDeLaLista + ">", statementList.mutable);

        return null;
    }
}