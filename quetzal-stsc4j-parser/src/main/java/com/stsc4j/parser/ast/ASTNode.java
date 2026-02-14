package com.stsc4j.parser.ast;

public abstract class ASTNode {
    public abstract <R> R accept(Visitor<R> visitor);
}
