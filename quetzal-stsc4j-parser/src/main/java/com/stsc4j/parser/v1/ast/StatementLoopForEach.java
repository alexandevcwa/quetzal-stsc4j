package com.stsc4j.parser.v1.ast;

public class StatementLoopForEach extends Statement{

    // Controlan la primera parte de for (referencia a variable || declaración de variable; condición; incremento)
    public final ExpressionForEachVar declaration;

    public final ExpressionVariable listVariable;

    public final StatementBlock block;

    public StatementLoopForEach(ExpressionForEachVar declaration, ExpressionVariable listVariable, StatementBlock block) {
        this.declaration = declaration;
        this.listVariable = listVariable;
        this.block = block;
    }


    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
