package com.stsc4j.semantic.analyzer;

import com.stsc4j.parser.v1.ast.ExpressionIndexAccess;
import com.stsc4j.semantic.Environment;
import com.stsc4j.semantic.SemanticAbstractAnalyzer;
import com.stsc4j.semantic.SemanticError;

public class SemanticExpressionIndexAccess extends SemanticAbstractAnalyzer {

    private final Environment currentEnv;

    public SemanticExpressionIndexAccess (Environment currentEnv) {
        this.currentEnv = currentEnv;
    }

    @Override
    public String visit(ExpressionIndexAccess expressionIndexAccess) {
        // 1. Analizamos a qué le queremos sacar el índice (ej: la variable 'numeros')
        String tipoObjeto = expressionIndexAccess.objectList.accept(this);

        // 2. Analizamos qué hay dentro de los corchetes [ ] (ej: el 0)
        String tipoIndice = expressionIndexAccess.index.accept(this);

        // REGLA 1: El índice SIEMPRE debe ser un número entero
        if (tipoIndice != null && !tipoIndice.equals("entero")) {
            throw new SemanticError("El índice de una lista debe ser un número 'entero', pero me enviaste un '" + tipoIndice + "'.");
        }

        // REGLA 2: Solo podemos usar corchetes [] en variables tipo lista
        if (tipoObjeto != null && tipoObjeto.startsWith("lista<")) {
            // Si es una lista de enteros, al acceder a un elemento, el resultado es un entero.
            // Extraemos el tipo interno ("lista<entero>" -> "entero")
            return tipoObjeto.replace("lista<", "").replace(">", "");
        } else {
            throw new SemanticError("Intentaste usar corchetes [ ] en una variable de tipo '" + tipoObjeto + "', lo cual no es una lista.");
        }
    }

}
