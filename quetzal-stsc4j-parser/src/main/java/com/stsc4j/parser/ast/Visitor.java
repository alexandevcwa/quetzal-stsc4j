package com.stsc4j.parser.ast;


import com.stsc4j.parser.ast.expression.BinaryExpression;
import com.stsc4j.parser.ast.expression.ListExpression;
import com.stsc4j.parser.ast.expression.LiteralExpression;
import com.stsc4j.parser.ast.expression.UnaryExpression;
import com.stsc4j.parser.ast.expression.VariableExpression;
import com.stsc4j.parser.ast.statement.*;

public interface Visitor<R> {

    // Statements
    R visit(VarDeclaration node);
    R visit(IfStatement node);
    R visit(WhileStatement node);
    R visit(ForStatement node);
    R visit(DoWhileStatement node);
    R visit(ReturnStatement node);
    R visit(BreakStatement node);
    R visit(ContinueStatement node);
    R visit(ExpressionStatement node);
    R visit(BlockStatement node);

    // Expressions
    R visit(BinaryExpression node);
    R visit(UnaryExpression node);
    R visit(LiteralExpression node);
    R visit(VariableExpression node);
    R visit(ListExpression node);

}
