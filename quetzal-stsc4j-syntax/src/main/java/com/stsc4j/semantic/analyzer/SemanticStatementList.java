package com.stsc4j.semantic.analyzer;

import com.stsc4j.lexer.TokenType;
import com.stsc4j.parser.v1.ast.Expression;
import com.stsc4j.parser.v1.ast.StatementList;
import com.stsc4j.semantic.SemanticAbstractAnalyzer;
import com.stsc4j.semantic.SemanticAnalyzer;
import com.stsc4j.semantic.SemanticError;

public class SemanticStatementList extends SemanticAbstractAnalyzer {

    private final SemanticAnalyzer analyzer;

    public SemanticStatementList(SemanticAnalyzer analyzer) {
        this.analyzer = analyzer;
    }

    @Override
    public String visit(StatementList stmt) {
        // 1. Determinamos el tipo de lista con protección contra nulos
        String tipoLista = "lista";
        if (stmt.type != null) {
            String tipoRecuperado = stmt.type.accept(analyzer);
            if (tipoRecuperado != null) {
                tipoLista = tipoRecuperado;
            }
        }

        // 2. Extraemos el tipo que debe tener CADA elemento por dentro
        String tipoEsperadoDeElemento = "mixto";
        if (!tipoLista.equals("lista") && tipoLista.startsWith("lista<")) {
            tipoEsperadoDeElemento = tipoLista.substring(6, tipoLista.length() - 1);
        }

        // 3. Validamos que los elementos dentro de [...] cumplan el contrato
        if (stmt.expressionList != null && stmt.expressionList.expressions != null) {
            for (int i = 0; i < stmt.expressionList.expressions.size(); i++) {
                Expression expr = stmt.expressionList.expressions.get(i);
                String tipoElemento = expr.accept(analyzer);

                if (!tipoEsperadoDeElemento.equals("mixto") && tipoElemento != null) {
                    boolean compatible = tipoEsperadoDeElemento.equals(tipoElemento);

                    if (tipoEsperadoDeElemento.equals(TokenType.PRIMITIVE_DECIMAL.name()) &&
                            tipoElemento.equals(TokenType.PRIMITIVE_INTEGER.name())) {
                        compatible = true;
                    }

                    if (!compatible) {
                        String esperadoUsr = tipoEsperadoDeElemento.replace("PRIMITIVE_", "").toLowerCase();
                        String elementoUsr = tipoElemento.replace("PRIMITIVE_", "").toLowerCase();
                        throw new SemanticError("Error Semántico: La lista esperaba elementos de tipo '" + esperadoUsr + "', pero el elemento en la posición " + i + " es un '" + elementoUsr + "'.");
                    }
                }
            }
        }

        // 4. Guardamos la lista en la memoria de variables
        String nombreLista = stmt.listName.getLexeme();
        analyzer.getEnv().define(nombreLista, tipoLista, stmt.mutable);

        return null;
    }
}