package com.stsc4j.semantic;

import com.stsc4j.parser.v1.ast.*;
import java.util.List;

//Cambio importante: Ahora el visitor devuelve String para que me devuelva el tipo de dato
public class SemanticAnalyzer implements Visitor<String> {

    // Nuestra memoria que guarda las variables que vamos encontrando
    private Environment currentEnv;

    public SemanticAnalyzer() {
        this.currentEnv = new Environment();
    }

    // Método para iniciar a leer las sentencias
    public void analyze(List<Statement> statements) {
        for (Statement stmt : statements) {
            stmt.accept(this);
        }
    }

    // SENTENCIAS

    /*MODIFICO EL VISITOR DE STATEMENT VARIABLE PARA
    QUE ME DEVUELVA EL TIPO DE DATO REAL DE LA
    EXPRESION Y ASI PODER COMPARARLO CON EL TIPO
    DE DATO ESPERADO DE LA VARIABLE
     */
    @Override
    public String visit(StatementVariable statementVariable) {
        //Primero vemos que tipo de variable se crea
        String tipoEsperado = statementVariable.typo.getLexeme();

        //Luego vemos que tipo de dato se le asigna a la variable
        String tipoReal = statementVariable.initialValue.accept(this);

        //Si los tipos no son iguales, se detiene el proceso
        if (tipoReal != null && !tipoEsperado.equals(tipoReal)) {
            throw new SemanticError("Trataste de guardar un dato tipo " + tipoReal + " en una variable de tipo " + tipoEsperado);
        }

        String nombreVariable = statementVariable.name.getLexeme();
        currentEnv.define(nombreVariable, tipoEsperado);

        return null;
    }

    @Override
    public String visit(StatementBlock statementBlock) {
        // Cuando entramos a un { }, creamos una memoria temporal
        Environment previousEnv = this.currentEnv;
        this.currentEnv = new Environment(previousEnv);

        try {
            // Analizamos lo de adentro con el nuevo entorno
            for (Statement stmt : statementBlock.statements) {
                stmt.accept(this);
            }
        } finally {
            // Al salir del { }, destruimos el entorno local y regresamos al anterior
            this.currentEnv = previousEnv;
        }
        return null;
    }

//Verificacion que el if sigue la estructura correcta, que la condicion sea una expresion booleana y que las sentencias then y else sean validas
    @Override
    public String visit(StatementIf statementIf) {
        String tipoCondicion  = statementIf.condition.accept(this);
        if (tipoCondicion != null && !tipoCondicion.equals("booleano")) {
            throw new SemanticError("La condición del if debe ser de tipo booleano, pero se encontró: " + tipoCondicion);
        }

        // Analizamos la sentencia else con el mismo entorno
        statementIf.thenStatement.accept(this);
        if (statementIf.elseStatement != null) {
            statementIf.elseStatement.accept(this);
        }
        return null;
    }

    @Override
    public String visit(StatementExpression statementExpression) {
        statementExpression.expression.accept(this);
        return null;
    }

    @Override
    public String visit(StatementList statementList) {
        String tipoDeLaLista = statementList.type.elementType.accept(this);

        //revisa que todos los elementos de la lista sean del mismo tipo
        for (Expression expr : statementList.expressionList.expressions){
            String tipoElemento = expr.accept(this);
            if(tipoElemento != null && !tipoElemento.equals(tipoDeLaLista)){
                throw new SemanticError("La lista es de tipo " + tipoDeLaLista + " pero se encontró un elemento de tipo " + tipoElemento);
            }
        }

        //Registro la lista en la tabla de variables
        currentEnv.define(statementList.listName.getLexeme(), "lista<" + tipoDeLaLista + ">");
        return null;
    }

    @Override
    public String visit(TypeList typeList) {
        return "";
    }

    @Override
    public String visit(TypePrimitive type) {
        return "";
    }

    @Override
    public String visit(StatementJsn statementJsn) {
        return "";
    }

    // EXPRESIONES

    @Override
    public String visit(ExpressionVariable expressionVariable) {
        // Sacamos el nombre y buscamos en memoria que tipo de dato es
        String varName = expressionVariable.token.getLexeme();
        return currentEnv.resolveType(varName); // Lanza SemanticError si no existe
    }

    @Override
    public String visit(ExpressionBinary expressionBinary) {
        String tipoIzquierdo = expressionBinary.left.accept(this);
        String tipoDerecho = expressionBinary.right.accept(this);

        //Validación si intentan sumar dos enteros o dos cadenas
        if (tipoIzquierdo != null && tipoDerecho != null && !tipoIzquierdo.equals(tipoDerecho)) {
            throw new SemanticError("No puedes operar entre tipos diferentes: " + tipoIzquierdo + " con un " + tipoDerecho);
        }
        return tipoIzquierdo; // El resultado de la operación tendrá el mismo tipo que los operandos
    }

    @Override
    public String visit(ExpressionLiteral expressionLiteral) {
        String tipoToken = expressionLiteral.token.getType().toString();

        if (tipoToken.contains("INTEGER") || tipoToken.contains("ENTERO")) {
            return "entero";
        } else if (tipoToken.contains("STRING") || tipoToken.contains("CADENA")) {
            return "cadena";
        } else if (tipoToken.contains("DECIMAL") || tipoToken.contains("FLOAT")) {
            return "decimal";
        } else if (tipoToken.contains("BOOLEAN") || tipoToken.contains("BOOL") || tipoToken.contains("TRUE") || tipoToken.contains("FALSE")) {
            // ¡Aquí agregamos TRUE y FALSE para tu lexer!
            return "booleano";
        }

        return "desconocido";
    }

    @Override
    public String visit(ExpressionTernary expressionTernary) {
        // Validamos la condicion (como si fuera un if)
        String tipoCondicion = expressionTernary.binary.accept(this);
        if (tipoCondicion != null && !tipoCondicion.equals("booleano")) {
            throw new SemanticError("La condicion del operador ternario debe ser booleano");
        }

        // Ambos lados del ternario deben devolver lo mismo
        String tipoIzquierdo = expressionTernary.left.accept(this);
        String tipoDerecho = expressionTernary.right.accept(this);

        if (tipoIzquierdo != null && tipoDerecho != null && !tipoIzquierdo.equals(tipoDerecho)) {
            throw new SemanticError("El ternario devuelve cosas distintas: " + tipoIzquierdo + " y " + tipoDerecho);
        }

        return tipoIzquierdo;
    }

    @Override
    public String visit(ExpressionMethodCall expressionMethodCall) {
        String tipoObjeto = expressionMethodCall.object.accept(this);
        String nombreMetodo = expressionMethodCall.methodName.getLexeme();

        if (tipoObjeto != null && tipoObjeto.startsWith("lista<")) {

            String tipoInterno = tipoObjeto.replace("lista<", "").replace(">", "");

            if (nombreMetodo.equals("agregar")) {
                if (expressionMethodCall.args.size() != 1) {
                    throw new SemanticError("El método 'agregar' necesita exactamente 1 argumento.");
                }

                String tipoArgumento = expressionMethodCall.args.get(0).accept(this);

                if (tipoArgumento != null && !tipoArgumento.equals(tipoInterno)) {
                    throw new SemanticError("Intentaste agregar un dato tipo '" + tipoArgumento + "' a una lista estricta de '" + tipoInterno + "'.");
                }

                return tipoObjeto;
            }
        }

        return "desconocido";
    }

    @Override
    public String visit(ExpressionIndexAccess expressionIndexAccess) {
        return "";
    }

    @Override
    public String visit(ExpressionList expressionList) {
        return "";
    }

    @Override
    public String visit(ExpressionJsnBlock expressionJsnBlock) {
        return "";
    }

    @Override
    public String visit(ExpressionJsn expressionJsn) {
        return "";
    }
}