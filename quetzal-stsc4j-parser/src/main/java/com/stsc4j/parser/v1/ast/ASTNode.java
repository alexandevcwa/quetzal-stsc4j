package com.stsc4j.parser.v1.ast;

public abstract class ASTNode {

    public abstract <T> T accept(Visitor<T> visitor);

}
