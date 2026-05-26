package com.stsc4j.semantic.analyzer;

import com.stsc4j.lexer.TokenType;
import com.stsc4j.parser.v1.ast.ExpressionMethodCall;
import com.stsc4j.parser.v1.ast.ExpressionVariable;
import com.stsc4j.semantic.Environment;
import com.stsc4j.semantic.SemanticAbstractAnalyzer;
import com.stsc4j.semantic.SemanticAnalyzer;
import com.stsc4j.semantic.SemanticError;

import java.util.Arrays;
import java.util.List;

public class SemanticExpressionMethodCall extends SemanticAbstractAnalyzer {

    private final SemanticAnalyzer analyzer;

    private static final List<String> METODOS_NATIVOS_JSN = Arrays.asList(
            "establecer", "eliminar", "fusionar", "contiene_clave",
            "claves", "valores", "texto", "texto_formateado", "agregar"
    );

    // ESTA LISTA ES CRUCIAL PARA TUS TESTS
    private static final List<String> METODOS_MUTABLES_JSN = Arrays.asList(
            "establecer", "eliminar", "fusionar", "agregar"
    );

    public SemanticExpressionMethodCall(SemanticAnalyzer analyzer) {
        this.analyzer = analyzer;
    }

    @Override
    public String visit(ExpressionMethodCall expr) {
        String funcName = expr.methodName.getLexeme();

        // 1. INTENTAR RESOLVER COMO FUNCIÓN GLOBAL
        Environment.FunctionInfo info = analyzer.getEnv().resolveFunction(funcName);
        if (info != null) {
            // Validar Aridad
            if (expr.args.size() != info.paramTypes.size()) {
                throw new SemanticError("La función '" + funcName + "' espera " + info.paramTypes.size() + " argumentos");
            }
            // Validar Tipos
            for (int i = 0; i < expr.args.size(); i++) {
                String tipoEnviado = expr.args.get(i).accept(analyzer);
                String tipoEsperado = info.paramTypes.get(i);
                boolean compatible = tipoEsperado.equals(tipoEnviado) ||
                        (tipoEsperado.equals(TokenType.PRIMITIVE_DECIMAL.name()) && tipoEnviado.equals(TokenType.PRIMITIVE_INTEGER.name()));

                if (!compatible) {
                    throw new SemanticError("Argumento inválido en la posición " + (i + 1));
                }
            }
            return info.returnType;
        }

        // 2. INTENTAR COMO OBJETO (JSN)
        if (expr.object != null) {
            expr.object.accept(analyzer);

            if (METODOS_NATIVOS_JSN.contains(funcName)) {

                // --- AQUÍ ESTABA EL BLOQUE QUE FALTABA ---
                // Si el método intenta modificar el objeto, validamos la inmutabilidad
                if (METODOS_MUTABLES_JSN.contains(funcName)) {
                    if (expr.object instanceof ExpressionVariable) {
                        ExpressionVariable varExpr = (ExpressionVariable) expr.object;
                        String nombreObjeto = varExpr.token.getLexeme();
                        Environment.VariableInfo varInfo = analyzer.getEnv().getVariable(nombreObjeto);

                        if (varInfo != null && !varInfo.isMutable) {
                            throw new SemanticError("La variable '" + nombreObjeto + "' es una constante.");
                        }
                    }
                }
                // ------------------------------------------

                if (expr.args != null) {
                    for (var arg : expr.args) arg.accept(analyzer);
                }

                switch (funcName) {
                    case "contiene_clave": return TokenType.PRIMITIVE_BOOLEAN.name();
                    case "texto": case "texto_formateado": return TokenType.PRIMITIVE_STRING.name();
                    case "claves": case "valores": return "lista";
                    case "eliminar": return TokenType.PRIMITIVE_STRING.name();
                    default: return "void";
                }
            }
            throw new SemanticError("El método '" + funcName + "' no existe");
        }

        throw new SemanticError("La variable '" + funcName + "' no ha sido definida.");
    }
}