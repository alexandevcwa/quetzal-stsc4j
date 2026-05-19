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

    // 1. Listas blancas estructuradas
    private static final List<String> METODOS_NATIVOS_JSN = Arrays.asList(
            "establecer", "eliminar", "fusionar", "contiene_clave",
            "claves", "valores", "texto", "texto_formateado", "agregar"
    );

    // 2. Sub-lista para los métodos que ALTERAN el objeto
    private static final List<String> METODOS_MUTABLES_JSN = Arrays.asList(
            "establecer", "eliminar", "fusionar", "agregar"
    );

    public SemanticExpressionMethodCall(SemanticAnalyzer analyzer) {
        this.analyzer = analyzer;
    }

    private boolean esMetodoNativo(String nombreFuncion) {
        return METODOS_NATIVOS_JSN.contains(nombreFuncion);
    }

    @Override
    public String visit(ExpressionMethodCall expr) {
        String funcName = expr.methodName.getLexeme();

        // --- MANEJO DE MÉTODOS NATIVOS DE JSN ---
        if (esMetodoNativo(funcName)) {
            // Evaluamos los argumentos internamente para que no queden sin analizar
            if (expr.args != null) {
                for (var arg : expr.args) {
                    arg.accept(analyzer);
                }
            }

            // Validamos semánticamente el objeto que está llamando al método
            String tipoObjeto = expr.object.accept(analyzer);

            // Si es un destructivo/modificador, validamos inmutabilidad
            if (METODOS_MUTABLES_JSN.contains(funcName)) {

                // Comprobamos si el objeto es una variable directa casteando a tu clase
                if (expr.object instanceof ExpressionVariable) {
                    ExpressionVariable varExpr = (ExpressionVariable) expr.object;

                    // Extraemos el nombre usando '.token'
                    String nombreObjeto = varExpr.token.getLexeme();

                    // Buscamos la variable en la memoria para ver si tiene 'var'
                    Environment.VariableInfo info = analyzer.getEnv().getVariable(nombreObjeto);

                    if (info != null && !info.isMutable) {
                        throw new SemanticError("Error Semántico: No puedes modificar el objeto '" + nombreObjeto + "' porque es una constante. Decláralo con 'var'.");
                    }
                }
            }

            // Retornamos el tipo de dato que Quetzal espera según la documentación
            switch (funcName) {
                case "contiene_clave":
                    return TokenType.PRIMITIVE_BOOLEAN.name(); // Retorna 'log'
                case "texto":
                case "texto_formateado":
                    return TokenType.PRIMITIVE_STRING.name();  // Retorna 'texto'
                case "claves":
                case "valores":
                    return "lista"; // Retorna una 'lista'
                case "eliminar":
                    return TokenType.PRIMITIVE_STRING.name();
                default:
                    return "void"; // establecer, fusionar, agregar no retornan nada
            }
        }

        // --- MANEJO DE FUNCIONES DE USUARIO NORMALES ---
        Environment.FunctionInfo info = analyzer.getEnv().resolveFunction(funcName);

        // Validamos Aridad (cantidad de argumentos)
        if (expr.args.size() != info.paramTypes.size()) {
            throw new SemanticError("Error Semántico: La función '" + funcName + "' espera " + info.paramTypes.size() + " argumentos, pero enviaste " + expr.args.size() + ".");
        }

        // Validamos Tipos
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

        return info.returnType;
    }
}