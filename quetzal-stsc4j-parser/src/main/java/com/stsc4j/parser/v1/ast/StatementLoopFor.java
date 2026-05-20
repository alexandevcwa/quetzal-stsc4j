package com.stsc4j.parser.v1.ast;

/**
 * Declaración de bucle for.
 */
public class StatementLoopFor extends Statement {

    /**
     * Declaración de variable o referencia a variable en la primera parte del bucle for.
     */
    public final ASTNode declaration;

    /**
     * Expresión de condición del bucle for.
     */
    public final ExpressionBinary condition;

    /**
     * Incremento o decremento de la variable en la segunda parte del bucle for.
     */
    public final ExpressionIncDec increment;

    /**
     * Bloque del ciclo for.
     */
    public final StatementBlock block;

    public StatementLoopFor(ASTNode astNode, ExpressionBinary condition, ExpressionIncDec expressionIncDec, StatementBlock block) {
        this.declaration = astNode;
        this.condition = condition;
        this.increment = expressionIncDec;
        this.block = block;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
