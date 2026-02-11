package com.stsc4j.parser.declaration;

import com.stsc4j.lexer.TokenType;
import com.stsc4j.parser.ast.Statement;
import com.stsc4j.parser.ast.Visitor;

import java.util.List;

public class FunctionDeclaration extends Statement {

    final TokenType returnType;
    final String name;
    final List<VarDeclaration> parameters;
    final Block body;

    public FunctionDeclaration(TokenType returnType, String name, List<VarDeclaration> parameters, Block body) {
        this.returnType = returnType;
        this.name = name;
        this.parameters = parameters;
        this.body = body;
    }


    @Override
    protected <R> R accept(Visitor<R> visitor) {
        return visitor.visit(this);
    }
}
