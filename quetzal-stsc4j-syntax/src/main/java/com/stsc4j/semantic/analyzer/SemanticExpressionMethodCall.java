package com.stsc4j.semantic.analyzer;

import com.stsc4j.lexer.TokenType;
import com.stsc4j.parser.v1.ast.Expression;
import com.stsc4j.parser.v1.ast.ExpressionMethodCall;
import com.stsc4j.semantic.Environment;
import com.stsc4j.semantic.SemanticAbstractAnalyzer;
import com.stsc4j.semantic.SemanticAnalyzer;
import com.stsc4j.semantic.SemanticError;

public class SemanticExpressionMethodCall extends SemanticAbstractAnalyzer {

    private final SemanticAnalyzer analyzer;

    public SemanticExpressionMethodCall(SemanticAnalyzer analyzer) {
        this.analyzer = analyzer;
    }

    @Override
    public String visit(ExpressionMethodCall expr) {
        String funcName = expr.methodName.getLexeme();

        // 1. Buscamos la función en la memoria
        Environment.FunctionInfo info = analyzer.getEnv().resolveFunction(funcName);

        // 2. Validamos Aridad (cantidad de argumentos)
        if (expr.args.size() != info.paramTypes.size()) {
            throw new SemanticError("Error Semántico: La función '" + funcName + "' espera " + info.paramTypes.size() + " argumentos, pero enviaste " + expr.args.size() + ".");
        }

        // 3. Validamos los Tipos de cada argumento enviado vs esperado
        for (int i = 0; i < expr.args.size(); i++) {
            String tipoEnviado = expr.args.get(i).accept(analyzer);
            String tipoEsperado = info.paramTypes.get(i);

            boolean compatible = tipoEsperado.equals(tipoEnviado);

            // Coerción (Ensanchamiento seguro)
            if (tipoEsperado.equals(TokenType.PRIMITIVE_DECIMAL.name()) && tipoEnviado.equals(TokenType.PRIMITIVE_INTEGER.name())) {
                compatible = true;
            }

            if (!compatible) {
                throw new SemanticError("Error Semántico: Argumento inválido en la posición " + (i + 1) + " de '" + funcName + "'. Se esperaba '" + tipoEsperado + "', pero se envió '" + tipoEnviado + "'.");
            }
        }

        // 4. Si  es correcto, la expresión "se convierte" en el tipo de retorno de la función
        return info.returnType;
    }
}