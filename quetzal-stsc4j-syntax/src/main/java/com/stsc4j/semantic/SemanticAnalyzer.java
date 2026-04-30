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

    // Metod para iniciar a leer las sentencias
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
        // Evaluamos lo que hay del lado derecho del igual
        String tipoReal = statementVariable.initialValue.accept(this);
        String nombreVariable = statementVariable.name.getLexeme();

        // TRUCO DEL PARSER:
        // Si el tipo es un IDENTIFIER, significa que es una reasignación (ej. a = 20)
        // porque no tiene palabra clave como 'entero' o 'cadena' al inicio.
        if (statementVariable.typo.getType().toString().equals("IDENTIFIER")) {

            // Tratamos de reasignar. Nuestro Environment se encargará de lanzar
            // error si la variable es inmutable o si los tipos no cuadran.
            currentEnv.assign(nombreVariable, tipoReal);

        } else {
            // ES UNA DECLARACIÓN NUEVA (ej. entero a = 20)
            String tipoEsperado = statementVariable.typo.getLexeme();

            if (tipoReal != null && !tipoEsperado.equals(tipoReal)) {
                throw new SemanticError("Trataste de guardar un dato tipo " + tipoReal + " en una variable de tipo " + tipoEsperado);
            }

            // Aquí le pasamos el booleano 'isMutable' que tu compañero preparó en el AST
            currentEnv.define(nombreVariable, tipoEsperado, statementVariable.mutable);
        }

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

        // Revisa que todos los elementos de la lista sean del mismo tipo
        for (Expression expr : statementList.expressionList.expressions){
            String tipoElemento = expr.accept(this);
            if(tipoElemento != null && !tipoElemento.equals(tipoDeLaLista)){
                throw new SemanticError("La lista es de tipo " + tipoDeLaLista + " pero se encontró un elemento de tipo " + tipoElemento);
            }
        }

        // Registro la lista en la tabla de variables
        // SOLUCION: Pasamos el tercer argumento (isMutable) que pide el Environment
        currentEnv.define(statementList.listName.getLexeme(), "lista<" + tipoDeLaLista + ">", statementList.mutable);

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