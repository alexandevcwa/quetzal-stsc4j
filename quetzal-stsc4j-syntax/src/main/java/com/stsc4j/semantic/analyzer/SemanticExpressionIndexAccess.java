package com.stsc4j.semantic.analyzer;

import com.stsc4j.lexer.TokenType;
import com.stsc4j.parser.v1.ast.Expression;
import com.stsc4j.parser.v1.ast.ExpressionIndexAccess;
import com.stsc4j.semantic.SemanticAbstractAnalyzer;
import com.stsc4j.semantic.SemanticAnalyzer;
import com.stsc4j.semantic.SemanticError;

public class SemanticExpressionIndexAccess extends SemanticAbstractAnalyzer {

    private final SemanticAnalyzer analyzer;

    public SemanticExpressionIndexAccess(SemanticAnalyzer analyzer) {
        this.analyzer = analyzer;
    }

    @Override
    public String visit(ExpressionIndexAccess expr) {
        // 1. Evaluamos a qué objeto le estamos sacando el índice
        String tipoObjeto = expr.objectList.accept(analyzer);

        if (tipoObjeto == null || !tipoObjeto.startsWith("lista")) {
            throw new SemanticError("Error Semántico: Intento de acceso por índice [...] a una variable que no es una colección o lista.");
        }

        // 2. Validamos que TODOS los índices utilizados sean ENTEROS
        if (expr.index != null) {
            for (Expression indexExpr : expr.index) {
                String tipoIndice = indexExpr.accept(analyzer);
                if (!TokenType.PRIMITIVE_INTEGER.name().equals(tipoIndice)) {
                    throw new SemanticError("Error Semántico: Los índices de las listas deben ser estrictamente números enteros.");
                }
            }
        }

        // 3. Calculamos el tipo de dato que va a salir de la lista
        // (Sirve para matrices. Si es lista<lista<entero>> y accedemos [0], sale un lista<entero>)
        String tipoRetorno = tipoObjeto;
        if (expr.index != null) {
            for (int i = 0; i < expr.index.size(); i++) {
                if (tipoRetorno.startsWith("lista<")) {
                    // Quitamos la capa exterior de la cebolla
                    tipoRetorno = tipoRetorno.substring(6, tipoRetorno.length() - 1);
                } else if (tipoRetorno.equals("lista")) {
                    // Si era lista sin tipado, devuelve cualquier cosa
                    tipoRetorno = "mixto";
                } else {
                    throw new SemanticError("Error Semántico: Estás intentando acceder a más dimensiones (índices) de las que tiene la matriz original.");
                }
            }
        }

        // Si es mixto, retornamos null para que Quetzal sepa que es un tipo de dato dinámico
        return tipoRetorno.equals("mixto") ? null : tipoRetorno;
    }
}