package com.stsc4j.parser.v1;

import com.stsc4j.lexer.Token;

// Nodo para operaciones binarias (ej. suma, multiplicación)
public class Binary extends Expr {

    final Expr left;
    final Token operator;
    final Expr right;

    public Binary(Expr left, Token operator, Expr right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    @Override
    <R> R accept(Visitor<R> visitor) {
        return visitor.visitBinaryExpr(this);
    }
}
