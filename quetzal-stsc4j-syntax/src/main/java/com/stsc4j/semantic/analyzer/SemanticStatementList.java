package com.stsc4j.semantic.analyzer;

import com.stsc4j.parser.v1.ast.Expression;
import com.stsc4j.parser.v1.ast.StatementList;
import com.stsc4j.semantic.Environment;
import com.stsc4j.semantic.SemanticAbstractAnalyzer;
import com.stsc4j.semantic.SemanticError;

public class SemanticStatementList extends SemanticAbstractAnalyzer {

    private final Environment currentEnv;

    public SemanticStatementList (Environment currentEnv) {
        this.currentEnv = currentEnv;
    }

    @Override
    public String visit(StatementList statementList) {
        String tipoDeLaLista = statementList.type.elementType.accept(this);

        // Revisa que todos los elementos de la lista sean del mismo tipo
        for (Expression expr : statementList.expressionList.expressions){
            String tipoElemento = expr.accept(this);
            if(tipoElemento != null && !tipoElemento.equals(tipoDeLaLista)){
                throw new SemanticError("La lista es de tipo " + tipoDeLaLista + " pero se encontró un elemento de tipo " + tipoElemento);
            }
        }

        // Registro la lista en la tabla de variables
        // SOLUCION: Pasamos el tercer argumento (isMutable) que pide el Environment
        currentEnv.define(statementList.listName.getLexeme(), "lista<" + tipoDeLaLista + ">", statementList.mutable);

        return null;
    }

}
