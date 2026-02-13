package com.stsc4j.parser.ast;

import com.stsc4j.parser.declaration.*;

public interface Visitor<R> {

    R visit(VarDeclaration stmt);

    R visit(FunctionDeclaration stmt);

    R visit(ReturnStatement expr);

    R visit(Block block);

    R visit(LiteralExpression expr);

    R visit(ListExpression expr);

    R visit(BinaryExpression expr);

    R visit(VariableExpression expr);

}
