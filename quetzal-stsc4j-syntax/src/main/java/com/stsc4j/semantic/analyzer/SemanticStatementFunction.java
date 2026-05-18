package com.stsc4j.semantic.analyzer;

import com.stsc4j.parser.v1.ast.Statement;
import com.stsc4j.parser.v1.ast.StatementFunction;
import com.stsc4j.parser.v1.ast.StatementFunctionParameter;
import com.stsc4j.semantic.Environment;
import com.stsc4j.semantic.SemanticAbstractAnalyzer;
import com.stsc4j.semantic.SemanticAnalyzer;

import java.util.ArrayList;
import java.util.List;

public class SemanticStatementFunction extends SemanticAbstractAnalyzer {

    private final SemanticAnalyzer analyzer;

    public SemanticStatementFunction(SemanticAnalyzer analyzer) {
        this.analyzer = analyzer;
    }

    @Override
    public String visit(StatementFunction stmt) {
        String funcName = stmt.identified.getLexeme();
        String returnType = stmt.returnValue.getType().name();

        // 1. Extraemos los tipos de los parámetros para la "Firma" de la función
        List<String> paramTypes = new ArrayList<>();
        for (Statement paramStmt : stmt.parameters) {
            StatementFunctionParameter param = (StatementFunctionParameter) paramStmt;
            paramTypes.add(param.type.getType().name());
        }

        // 2. Registramos la función en el entorno actual (Global) ANTES de analizar el bloque
        // Esto permite la recursividad (que la función se llame a sí misma)
        analyzer.getEnv().defineFunction(funcName, returnType, paramTypes);

        // 3. Creamos el Scope Local de la función
        Environment globalEnv = analyzer.getEnv();
        analyzer.setEnv(new Environment(globalEnv));

        try {
            // 4. Guardamos qué tipo debe retornar usando un nombre inválido para el usuario
            analyzer.getEnv().define("@return", returnType, false);

            // 5. Inyectamos los parámetros como variables locales
            for (Statement paramStmt : stmt.parameters) {
                StatementFunctionParameter param = (StatementFunctionParameter) paramStmt;
                analyzer.getEnv().define(param.identified.getLexeme(), param.type.getType().name(), param.mutable);
            }

            // 6. Analizamos el cuerpo de la función
            stmt.block.accept(analyzer);

        } finally {
            // 7. Destruimos el scope local
            analyzer.setEnv(globalEnv);
        }

        return null;
    }
}