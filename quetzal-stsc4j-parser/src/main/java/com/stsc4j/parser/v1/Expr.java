package com.stsc4j.parser.v1;

public abstract class Expr {
    abstract <R> R accept(Visitor<R> visitor);
}
