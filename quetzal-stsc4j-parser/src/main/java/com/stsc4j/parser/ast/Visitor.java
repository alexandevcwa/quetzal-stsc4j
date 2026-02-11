package com.stsc4j.parser.ast;

import com.stsc4j.parser.declaration.Block;
import com.stsc4j.parser.declaration.FunctionDeclaration;
import com.stsc4j.parser.declaration.ReturnStatement;
import com.stsc4j.parser.declaration.VarDeclaration;

public interface Visitor<R> {

    R visit(VarDeclaration stmt);

    R visit(FunctionDeclaration stmt);

    R visit(ReturnStatement expr);

    R visit(Block block);
}
