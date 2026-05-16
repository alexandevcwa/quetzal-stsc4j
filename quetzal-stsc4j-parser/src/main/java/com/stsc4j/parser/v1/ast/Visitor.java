package com.stsc4j.parser.v1.ast;

public interface Visitor<T> {

    /**
     * Visita un nodo de tipo ExpressionVariable en la jerarquía del árbol de sintaxis abstracta.
     *
     * @param expressionVariable el nodo de tipo ExpressionVariable que se va a visitar
     * @return el resultado de la visita, cuyo tipo depende de la implementación del visitante
     */
    T visit(ExpressionVariable expressionVariable);

    /**
     * Visita un nodo de tipo ExpressionLiteral en la jerarquía del árbol de sintaxis abstracta.
     *
     * @param expressionLiteral el nodo de tipo ExpressionLiteral que se va a visitar
     * @return el resultado de la visita, cuyo tipo depende de la implementación del visitante
     */
    T visit(ExpressionLiteral expressionLiteral);

    /**
     * Visita un nodo de tipo ExpressionBinary en la jerarquía del árbol de sintaxis abstracta.
     *
     * @param expressionBinary el nodo de tipo ExpressionBinary que se va a visitar
     * @return el resultado de la visita, cuyo tipo depende de la implementación del visitante
     */
    T visit(ExpressionBinary expressionBinary);

    /**
     * Visita un nodo de tipo ExpressionTernary en la jerarquía del árbol de sintaxis abstracta.
     *
     * @param expressionTernary el nodo de tipo ExpressionTernary que se va a visitar
     * @return el resultado de la visita, cuyo tipo depende de la implementación del visitante
     */
    T visit(ExpressionTernary expressionTernary);

    /**
     * Visita un nodo de tipo ExpressionMethodCall en la jerarquía del árbol de sintaxis abstracta.
     *
     * @param expressionMethodCall el nodo de tipo ExpressionMethodCall que se va a visitar
     * @return el resultado de la visita, cuyo tipo depende de la implementación del visitante
     */
    T visit(ExpressionMethodCall expressionMethodCall);

    /**
     * Visita un nodo de tipo ExpressionIndexAccess en la jerarquía del árbol de sintaxis abstracta.
     *
     * @param expressionIndexAccess el nodo de tipo ExpressionIndexAccess que se va a visitar
     * @return el resultado de la visita, cuyo tipo depende de la implementación del visitante
     */
    T visit(ExpressionIndexAccess expressionIndexAccess);

    /**
     * Visita un nodo de tipo ExpressionList en la jerarquía del árbol de sintaxis abstracta.
     *
     * @param expressionList el nodo de tipo ExpressionList que se va a visitar
     * @return el resultado de la visita, cuyo tipo depende de la implementación del visitante
     */
    T visit(ExpressionList expressionList);

    /**
     * Visita un nodo de tipo ExpressionJsnBlock en la jerarquía del árbol de sintaxis abstracta.
     *
     * @param expressionJsnBlock el nodo de tipo ExpressionJsnBlock que se va a visitar
     * @return el resultado de la visita, cuyo tipo depende de la implementación del visitante
     */
    T visit(ExpressionJsnBlock expressionJsnBlock);

    /**
     * Visita un nodo de tipo ExpressionJsn en la jerarquía del árbol de sintaxis abstracta.
     *
     * @param expressionJsn el nodo de tipo ExpressionJsn que se va a visitar
     * @return el resultado de la visita, cuyo tipo depende de la implementación del visitante
     */
    T visit(ExpressionJsn expressionJsn);

    /**
     * Visita un nodo de tipo ExpressionPropertyAccess en la jerarquía del árbol de sintaxis abstracta.
     *
     * @param expressionPropertyAccess el nodo de tipo ExpressionPropertyAccess que se va a visitar
     * @return el resultado de la visita, cuyo tipo depende de la implementación del visitante
     */
    T visit(ExpressionPropertyAccess expressionPropertyAccess);

    /**
     * Visita un nodo de tipo ExpressionIncDec en la jerarquía del árbol de sintaxis abstracta.
     *
     * @param expressionIncDec el nodo de tipo ExpressionIncDec que se va a visitar
     * @return el resultado de la visita, cuyo tipo depende de la implementación del visitante
     */
    T visit(ExpressionIncDec expressionIncDec);

    /**
     * Visita un nodo de tipo ExpressionForEachVar en la jerarquía del árbol de sintaxis abstracta.
     *
     * @param expressionForEachVar el nodo de tipo ExpressionForEachVar que se va a visitar
     * @return el resultado de la visita, cuyo tipo depende de la implementación del visitante
     */
    T visit(ExpressionForEachVar expressionForEachVar);

    /**
     * Visita un nodo de tipo StatementIf en la jerarquía del árbol de sintaxis abstracta.
     *
     * @param statementIf el nodo de tipo StatementIf que se va a visitar
     * @return el resultado de la visita, cuyo tipo depende de la implementación del visitante
     */
    T visit(StatementIf statementIf);

    /**
     * Visita un nodo de tipo StatementBlock en la jerarquía del árbol de sintaxis abstracta.
     *
     * @param statementBlock el nodo de tipo StatementBlock que se va a visitar
     * @return el resultado de la visita, cuyo tipo depende de la implementación del visitante
     */
    T visit(StatementBlock statementBlock);

    /**
     * Visita un nodo de tipo StatementExpression en la jerarquía del árbol de sintaxis abstracta.
     *
     * @param statementExpression el nodo de tipo StatementExpression que se va a visitar
     * @return el resultado de la visita, cuyo tipo depende de la implementación del visitante
     */
    T visit(StatementExpression statementExpression);

    /**
     * Visita un nodo de tipo StatementVariable en la jerarquía del árbol de sintaxis abstracta.
     *
     * @param statementVariable el nodo de tipo StatementVariable que se va a visitar
     * @return el resultado de la visita, cuyo tipo depende de la implementación del visitante
     */
    T visit(StatementVariable statementVariable);

    /**
     * Visita un nodo de tipo StatementList en la jerarquía del árbol de sintaxis abstracta.
     *
     * @param statementList el nodo de tipo StatementList que se va a visitar
     * @return el resultado de la visita, cuyo tipo depende de la implementación del visitante
     */
    T visit(StatementList statementList);

    /**
     * Visita un nodo de tipo TypeList en la jerarquía del árbol de sintaxis abstracta.
     *
     * @param typeList el nodo de tipo TypeList que se va a visitar
     * @return el resultado de la visita, cuyo tipo depende de la implementación del visitante
     */
    T visit(TypeList typeList);

    /**
     * Visita un nodo de tipo TypePrimitive en la jerarquía del árbol de sintaxis abstracta.
     *
     * @param type el nodo de tipo TypePrimitive que se va a visitar
     * @return el resultado de la visita, cuyo tipo depende de la implementación del visitante
     */
    T visit(TypePrimitive type);

    /**
     * Visita un nodo de tipo StatementJsn en la jerarquía del árbol de sintaxis abstracta.
     *
     * @param statementJsn el nodo de tipo StatementJsn que se va a visitar
     * @return el resultado de la visita, cuyo tipo depende de la implementación del visitante
     */
    T visit(StatementJsn statementJsn);

    /**
     * Visita un nodo de tipo StatementFunction en la jerarquía del árbol de sintaxis abstracta.
     *
     * @param statementFunction el nodo de tipo StatementFunction que se va a visitar
     * @return el resultado de la visita, cuyo tipo depende de la implementación del visitante
     */
    T visit(StatementFunction statementFunction);

    /**
     * Visita un nodo de tipo StatementFunctionParameter en la jerarquía del árbol de sintaxis abstracta.
     *
     * @param statementFunctionParameter el nodo de tipo StatementFunctionParameter que se va a visitar
     * @return el resultado de la visita, cuyo tipo depende de la implementación del visitante
     */
    T visit(StatementFunctionParameter statementFunctionParameter);

    /**
     * Visita un nodo de tipo StatementReturn en la jerarquía del árbol de sintaxis abstracta.
     *
     * @param statementReturn el nodo de tipo StatementReturn que se va a visitar
     * @return el resultado de la visita, cuyo tipo depende de la implementación del visitante
     */
    T visit(StatementReturn statementReturn);

    /**
     * Visita un nodo de tipo StatementLoopWhile en la jerarquía del árbol de sintaxis abstracta.
     *
     * @param statementLoopWhile el nodo de tipo StatementLoopWhile que se va a visitar
     * @return el resultado de la visita, cuyo tipo depende de la implementación del visitante
     */
    T visit(StatementLoopWhile statementLoopWhile);

    /**
     * Visita un nodo de tipo StatementLoopDoWhile y realiza una operación específica.
     *
     * @param statementLoopDoWhile el nodo de tipo StatementLoopDoWhile que será visitado.
     * @return el resultado de la operación realizada tras visitar el nodo.
     */
    T visit(StatementLoopDoWhile statementLoopDoWhile);

    /**
     * Visita una instancia de StatementLoopFor y realiza una operación definida.
     *
     * @param statementLoopFor La instancia de StatementLoopFor que será visitada.
     * @return Un resultado de tipo T generado a partir de la operación realizada sobre statementLoopFor.
     */
    T visit(StatementLoopFor statementLoopFor);

    /**
     * Visita un nodo de tipo StatementLoopForEach.
     *
     * @param statementLoopForEach el nodo de declaración de bucle "for-each" que se va a visitar
     * @return un valor del tipo T resultante de la visita al nodo
     */
    T visit(StatementLoopForEach statementLoopForEach);

    /**
     * Visita un nodo de tipo StatementIncDec y realiza la lógica correspondiente.
     *
     * @param statementIncDec el nodo StatementIncDec que será visitado
     * @return un objeto de tipo T que representa el resultado de la visita
     */
    T visit(StatementIncDec statementIncDec);

    /**
     * Visits a StatementMatrixAssignation and processes it.
     *
     * @param statementMatrixAssignation the StatementMatrixAssignation object to be visited
     * @return an object of type T resulting from the processing of the statementMatrixAssignation
     */
    T visit(StatementMatrixAssignation statementMatrixAssignation);

    /**
     * Visits the provided ExpressionNull instance and processes it according to the implementation.
     *
     * @param expressionNull an instance of ExpressionNull to be visited
     * @return the result of processing the ExpressionNull instance
     */
    T visit(ExpressionNull expressionNull);

    /**
     * Visits the provided StatementTryCatchFinally object and performs operations
     * defined in the implementation of this method.
     *
     * @param statementTryCatchFinally the try-catch-finally statement node to visit
     * @return a result of type T after processing the provided statement
     */
    T visit(StatementTryCatchFinally statementTryCatchFinally);

    /**
     * Visits the specified StatementConsolaOut instance and performs an operation or computation.
     *
     * @param statementConsolaOut the StatementConsolaOut instance to be visited
     * @return a result of type T produced by visiting the statement
     */
    T visit(StatementConsolaOut statementConsolaOut);

    /**
     * Processes the given StatementContinue object and performs a specific action or computation.
     *
     * @param statementContinue the StatementContinue object to be visited and processed
     * @return the result of processing the StatementContinue object
     */
    T visit(StatementContinue statementContinue);

    /**
     * Visits the specified StatementBreak and allows custom processing or operations
     * to be performed on the StatementBreak instance.
     *
     * @param statementBreak the StatementBreak instance to be visited
     * @return a result of the type T based on the processing of the StatementBreak
     */
    T visit(StatementBreak statementBreak);

    T visit(StatementThrow statementThrow);

    T visit(ExpressionConsoleIn expressionConsoleIn);
}
