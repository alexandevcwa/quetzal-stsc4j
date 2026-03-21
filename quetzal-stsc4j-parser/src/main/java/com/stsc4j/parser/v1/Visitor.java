package com.stsc4j.parser.v1;

public interface Visitor<R> {
    R visitLiteralExpr(Literal expr);
    R visitBinaryExpr(Binary expr);
}
