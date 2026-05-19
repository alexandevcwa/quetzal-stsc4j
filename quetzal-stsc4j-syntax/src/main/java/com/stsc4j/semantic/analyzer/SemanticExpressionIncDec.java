package com.stsc4j.semantic.analyzer;

import com.stsc4j.lexer.TokenType;
import com.stsc4j.parser.v1.ast.ExpressionIncDec;
import com.stsc4j.semantic.Environment;
import com.stsc4j.semantic.SemanticAbstractAnalyzer;
import com.stsc4j.semantic.SemanticAnalyzer;
import com.stsc4j.semantic.SemanticError;

public class SemanticExpressionIncDec extends SemanticAbstractAnalyzer {

    private final SemanticAnalyzer analyzer;

    public SemanticExpressionIncDec(SemanticAnalyzer analyzer) {
        this.analyzer = analyzer;
    }

    @Override
    public String visit(ExpressionIncDec expr) {
        // 1. Extraemos el nombre de la variable (Alex nos facilitó la vida aquí)
        String nombreVariable = expr.identifier.token.getLexeme();

        // 2. Buscamos la variable en la memoria (Environment)
        Environment.VariableInfo info = analyzer.getEnv().getVariable(nombreVariable);

        // 3. REGLA DE INMUTABILIDAD: ¿Tiene 'var'?
        if (!info.isMutable) {
            throw new SemanticError("Error Semántico: No puedes aplicar '++' o '--' a la variable '" + nombreVariable + "' porque es una constante. Declárala con 'var'.");
        }

        // 4. REGLA DE TIPO: ¿Es matemática? (entero o numero/decimal)
        boolean esEntero = info.Type.equals(TokenType.PRIMITIVE_INTEGER.name());
        boolean esDecimal = info.Type.equals(TokenType.PRIMITIVE_DECIMAL.name());

        if (!esEntero && !esDecimal) {
            throw new SemanticError("Error Semántico: Los operadores incrementales/decrementales solo se pueden aplicar a variables matemáticas ('entero' o 'numero'), pero '" + nombreVariable + "' es '" + info.Type + "'.");
        }

        // 5. Retornamos el mismo tipo de la variable (si era entero, sigue siendo entero)
        return info.Type;
    }
}