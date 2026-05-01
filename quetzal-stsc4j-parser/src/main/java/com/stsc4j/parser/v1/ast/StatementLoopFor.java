package com.stsc4j.parser.v1.ast;

public class StatementLoopFor extends Statement {

    // Controlan la primera parte de for (referencia a variable || declaración de variable; condición; incremento)
    public final ASTNode declaration;

    // Expresión condicional
    public final ExpressionBinary condition;

    // Incremento
    public final ExpressionIncDec increment;

    // Bloque de código a ejecutar en cada iteración
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
