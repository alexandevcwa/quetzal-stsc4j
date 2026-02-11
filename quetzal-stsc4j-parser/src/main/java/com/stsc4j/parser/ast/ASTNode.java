package com.stsc4j.parser.ast;

public abstract class ASTNode {
    protected abstract <R> R accept(Visitor<R> visitor);
}
