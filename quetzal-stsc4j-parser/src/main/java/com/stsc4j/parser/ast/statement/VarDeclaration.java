package com.stsc4j.parser.ast.statement;


import com.stsc4j.parser.ast.TypeInfo;
import com.stsc4j.parser.ast.Visitor;
import com.stsc4j.parser.ast.expression.Expression;

public class VarDeclaration extends Statement {

    private final TypeInfo type;
    private final String name;
    private final boolean isMutable;
    private final Expression expression;

    public VarDeclaration(TypeInfo type, String name, boolean isMutable, Expression expression) {
        this.type = type;
        this.name = name;
        this.isMutable = isMutable;
        this.expression = expression;
    }

    @Override
    public <R> R accept(Visitor<R> visitor) {
        return visitor.visit(this);
    }

    public TypeInfo getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public boolean isMutable() {
        return isMutable;
    }

    public Expression getExpression() {
        return expression;
    }
}
