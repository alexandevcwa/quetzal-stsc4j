package com.stsc4j.parser.ast;

import com.stsc4j.parser.declaration.*;

public interface Visitor<R> {

    /**
     * Visita una declaración de variable y realiza la acción definida por el visitante.
     *
     * @param stmt La declaración de variable que será procesada.
     * @return Un valor de tipo genérico R que representa el resultado de la acción realizada
     * por el visitante sobre la declaración de variable.
     */
    R visit(VarDeclaration stmt);

    /**
     * Visita una declaración de función y realiza la acción definida por el visitante.
     *
     * @param stmt La declaración de función que será procesada. Contiene información
     *             sobre el nombre de la función, su tipo de retorno, parámetros y el cuerpo de la función.
     * @return Un valor de tipo genérico R que representa el resultado de la acción realizada
     * por el visitante sobre la declaración de función.
     */
    R visit(FunctionDeclaration stmt);

    /**
     * Visita una instrucción de retorno y realiza la acción definida por el visitante.
     *
     * @param expr La instrucción de retorno que será procesada. Contiene una expresión
     *             opcional que representa el valor que será devuelto.
     * @return Un valor de tipo genérico R que representa el resultado de la acción realizada
     * por el visitante sobre la instrucción de retorno.
     */
    R visit(ReturnStatement expr);

    /**
     * Visita un bloque de instrucciones y realiza la acción definida por el visitante.
     *
     * @param block El bloque de instrucciones que será procesado. Contiene una lista
     *              de declaraciones o sentencias que serán evaluadas o ejecutadas.
     * @return Un valor de tipo genérico R que representa el resultado de la acción realizada
     * por el visitante sobre el bloque de instrucciones.
     */
    R visit(Block block);

    /**
     * Visita una expresión literal y realiza la acción definida por el visitante.
     *
     * @param expr La expresión literal que será procesada. Contiene un valor constante
     *             que representa el contenido de la expresión.
     * @return Un valor de tipo genérico R que representa el resultado de la acción
     * realizada por el visitante sobre la expresión literal.
     */
    R visit(LiteralExpression expr);

    /**
     * Visita una expresión de lista y procesa sus elementos según la acción definida por el visitante.
     *
     * @param expr La expresión de lista que será procesada. Contiene una colección de expresiones que
     *             representan los elementos de la lista.
     * @return Un valor de tipo genérico R que representa el resultado de la acción realizada
     * por el visitante sobre la expresión de lista.
     */
    R visit(ListExpression expr);

    /**
     * Visita una expresión binaria y realiza la acción definida por el visitante.
     *
     * @param expr La expresión binaria que será procesada. Contiene dos subexpresiones (izquierda y derecha)
     *             y un operador que define la operación lógica o aritmética a realizar.
     * @return Un valor de tipo genérico R que representa el resultado de la acción realizada
     * por el visitante sobre la expresión binaria.
     */
    R visit(BinaryExpression expr);

    /**
     * Visita una expresión de variable y realiza la acción definida por el visitante.
     *
     * @param expr La expresión de variable que será procesada. Contiene el nombre
     *             de la variable que está siendo referenciada.
     * @return Un valor de tipo genérico R que representa el resultado de la acción
     * realizada por el visitante sobre la expresión de variable.
     */
    R visit(VariableExpression expr);

}
