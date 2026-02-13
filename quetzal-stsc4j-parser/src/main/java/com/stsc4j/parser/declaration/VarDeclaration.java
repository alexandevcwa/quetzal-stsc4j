package com.stsc4j.parser.declaration;

import com.stsc4j.parser.ast.Expression;
import com.stsc4j.parser.ast.Statement;
import com.stsc4j.parser.ast.TypeInfo;
import com.stsc4j.parser.ast.Visitor;

public class VarDeclaration extends Statement {

    final TypeInfo type;
    final boolean isMutable;
    final String name;
    final Expression initializer;

    public VarDeclaration(TypeInfo type, boolean isMutable, String name, Expression initializer) {
        this.type = type;
        this.isMutable = isMutable;
        this.name = name;
        this.initializer = initializer;
    }

    public TypeInfo getType() {
        return type;
    }

    public boolean isMutable() {
        return isMutable;
    }

    public String getName() {
        return name;
    }

    public Expression getInitializer() {
        return initializer;
    }

    @Override
    protected <R> R accept(Visitor<R> visitor) {
        return visitor.visit(this);
    }
}
