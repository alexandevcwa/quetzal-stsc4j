package com.stsc4j.parser.ast;

import com.stsc4j.parser.declaration.*;

import java.util.List;

public class ASTPrinter implements Visitor<String> {

    public String print(List<Statement> statements) {
        StringBuilder sb = new StringBuilder();
        for (Statement stmt : statements) {
            sb.append(stmt.accept(this)).append("\n");
        }
        return sb.toString();
    }

    @Override
    public String visit(VarDeclaration stmt) {
        String mut = stmt.isMutable() ? "[MUTABLE]" : "";
        String val = (stmt.getInitializer() != null) ? stmt.getInitializer().accept(this) : "null";
        return "(VAR " + mut + " " + stmt.getType() + " " + stmt.getName() + " = " + val + ")";
    }

    @Override
    public String visit(FunctionDeclaration stmt) {
        StringBuilder params = new StringBuilder();
        for (VarDeclaration p : stmt.getParameters()) {
            params.append(p.getType() + " " + p.getName() + ", ");
        }
        return "(FUNC " + stmt.getReturnType() + " " + stmt.getName() + "(" + params + ") {\n" +
                stmt.getBody().accept(this) + "})";
    }

    @Override
    public String visit(ReturnStatement expr) {
        return "";
    }

    @Override
    public String visit(Block block) {
        StringBuilder sb = new StringBuilder();
        for (Statement s : block.getStatements()) sb.append("  ").append(s.accept(this)).append("\n");
        return sb.toString();
    }

    @Override
    public String visit(LiteralExpression expr) {
        if (expr.getValue() instanceof String) return "\"" + expr.getValue() + "\"";
        return expr.getValue().toString();
    }

    @Override
    public String visit(ListExpression expr) {
        StringBuilder sb = new StringBuilder("[");
        for (Expression e : expr.getElements()) sb.append(e.accept(this)).append(", ");
        return sb + "]";
    }

    @Override
    public String visit(BinaryExpression expr) {
        return "(" + expr.getLeft().accept(this) + " " + expr.getOperator() + " " + expr.getRight().accept(this) + ")";
    }

    @Override
    public String visit(VariableExpression expr) {
        return expr.getName();
    }
}
