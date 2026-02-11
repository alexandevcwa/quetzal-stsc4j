package com.stsc4j.parser.declaration;

import com.stsc4j.parser.ast.Expression;
import com.stsc4j.parser.ast.Statement;
import com.stsc4j.parser.ast.Visitor;

public class VarDeclaration extends Statement {

    final String type;
    final boolean isMutable;
    final String name;
    final Expression initializer;

    public VarDeclaration(String type, boolean isMutable, String name, Expression initializer) {
        this.type = type;
        this.isMutable = isMutable;
        this.name = name;
        this.initializer = initializer;
    }

    @Override
    <R> R accept(Visitor<R> visitor) {
        return visitor.visit(this);
    }
}
