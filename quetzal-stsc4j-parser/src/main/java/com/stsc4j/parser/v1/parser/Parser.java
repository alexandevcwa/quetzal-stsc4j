package com.stsc4j.parser.v1.parser;

import com.stsc4j.parser.v1.ast.Expression;
import com.stsc4j.parser.v1.ast.Statement;

import java.util.List;

public abstract class Parser {

    public Statement parseStatement(){
        throw new UnsupportedOperationException("Method parse() not implemented in " + this.getClass().getSimpleName());
    }

    public List<Statement> parseStatements(){
        throw new UnsupportedOperationException("Method parse() not implemented in " + this.getClass().getSimpleName());
    }

    public Expression parseExpression(){
        throw new UnsupportedOperationException("Method parse() not implemented in " + this.getClass().getSimpleName());
    }

}
