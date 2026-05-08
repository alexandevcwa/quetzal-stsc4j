package com.stsc4j.semantic.analyzer;

import com.stsc4j.parser.v1.ast.Statement;
import com.stsc4j.parser.v1.ast.StatementFunction;
import com.stsc4j.parser.v1.ast.StatementFunctionParameter;
import com.stsc4j.semantic.Environment;
import com.stsc4j.semantic.SemanticAbstractAnalyzer;

public class SemanticStatementFunction extends SemanticAbstractAnalyzer {

    private Environment currentEnv;

    public SemanticStatementFunction(Environment currentEnv) {
        this.currentEnv = currentEnv;
    }

    private String retornoEsperadoActual = "vacio";

    @Override
    public String visit(StatementFunction statementFunction) {
        String nombreFuncion = statementFunction.identified.getLexeme();
        // Si no tiene token de retorno (ej. es un void), asumimos "vacio"
        String tipoRetorno = statementFunction.returnValue != null ? statementFunction.returnValue.getLexeme() : "vacio";

        // 1. Armar la "Firma" de la función
        // Como nuestra memoria (Environment) guarda Strings, vamos a guardar la función así:
        // "funcion_entero_entero_retorna_entero" -> para sumar(entero a, entero b) -> entero
        StringBuilder firma = new StringBuilder("funcion");
        for(Statement paramStmt : statementFunction.parameters) {
            StatementFunctionParameter param = (StatementFunctionParameter) paramStmt;
            firma.append("_").append(param.type.getLexeme());
        }
        firma.append("_retorna_").append(tipoRetorno);

        // 2. Registrar la función en la memoria GLOBAL (antes de entrar a su bloque)
        // Esto es clave para que exista la recursividad (que la función se llame a sí misma)
        // Las funciones son inmutables (false), no queremos que alguien haga sumar = 5;
        currentEnv.define(nombreFuncion, firma.toString(), false);

        // 3. Crear el entorno (Scope) local aislado para la función
        Environment entornoAnterior = this.currentEnv;
        this.currentEnv = new Environment(entornoAnterior);

        // Guardamos la promesa de retorno por si hay funciones anidadas
        String retornoEsperadoAnterior = this.retornoEsperadoActual;
        this.retornoEsperadoActual = tipoRetorno;

        try {
            // 4. Inyectar los parámetros en esta nueva memoria local
            for (Statement param : statementFunction.parameters) {
                param.accept(this);
            }

            // 5. Analizar el contenido (bloque) de la función
            statementFunction.block.accept(this);

        } finally {
            // 6. Al terminar la función, destruimos la memoria local y regresamos a la normalidad
            this.currentEnv = entornoAnterior;
            this.retornoEsperadoActual = retornoEsperadoAnterior;
        }

        return null;
    }

}
