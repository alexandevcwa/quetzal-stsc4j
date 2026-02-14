package com.stsc4j.parser.ast;

import com.stsc4j.parser.ast.expression.*;
import com.stsc4j.parser.ast.statement.*;

/**
 * Visitor que imprime el AST en formato legible.
 * Útil para depuración y visualización del árbol de sintaxis.
 */
public class ASTPrinter implements Visitor<String> {

    private int indentLevel = 0;
    private static final String INDENT = "  ";

    private String indent() {
        return INDENT.repeat(indentLevel);
    }

    public String print(ASTNode node) {
        return node.accept(this);
    }

    // ============== STATEMENTS ==============

    @Override
    public String visit(VarDeclaration node) {
        StringBuilder sb = new StringBuilder();
        sb.append(indent()).append("VarDeclaration:\n");
        indentLevel++;
        sb.append(indent()).append("type: ").append(node.getType().toString()).append("\n");
        sb.append(indent()).append("name: ").append(node.getName()).append("\n");
        sb.append(indent()).append("mutable: ").append(node.isMutable()).append("\n");
        sb.append(indent()).append("value:\n");
        indentLevel++;
        sb.append(node.getExpression().accept(this));
        indentLevel--;
        indentLevel--;
        return sb.toString();
    }

    @Override
    public String visit(IfStatement node) {
        StringBuilder sb = new StringBuilder();
        sb.append(indent()).append("IfStatement:\n");
        indentLevel++;
        sb.append(indent()).append("condition:\n");
        indentLevel++;
        sb.append(node.getCondition().accept(this));
        indentLevel--;
        sb.append(indent()).append("then:\n");
        indentLevel++;
        for (Statement stmt : node.getThenBranch()) {
            sb.append(stmt.accept(this));
        }
        indentLevel--;
        if (node.getElseBranch() != null) {
            sb.append(indent()).append("else:\n");
            indentLevel++;
            for (Statement stmt : node.getElseBranch()) {
                sb.append(stmt.accept(this));
            }
            indentLevel--;
        }
        indentLevel--;
        return sb.toString();
    }

    @Override
    public String visit(WhileStatement node) {
        StringBuilder sb = new StringBuilder();
        sb.append(indent()).append("WhileStatement:\n");
        indentLevel++;
        sb.append(indent()).append("condition:\n");
        indentLevel++;
        sb.append(node.getCondition().accept(this));
        indentLevel--;
        sb.append(indent()).append("body:\n");
        indentLevel++;
        for (Statement stmt : node.getBody()) {
            sb.append(stmt.accept(this));
        }
        indentLevel--;
        indentLevel--;
        return sb.toString();
    }

    @Override
    public String visit(ForStatement node) {
        StringBuilder sb = new StringBuilder();
        sb.append(indent()).append("ForStatement:\n");
        indentLevel++;
        if (node.getInitializer() != null) {
            sb.append(indent()).append("initializer:\n");
            indentLevel++;
            sb.append(node.getInitializer().accept(this));
            indentLevel--;
        }
        if (node.getCondition() != null) {
            sb.append(indent()).append("condition:\n");
            indentLevel++;
            sb.append(node.getCondition().accept(this));
            indentLevel--;
        }
        if (node.getIncrement() != null) {
            sb.append(indent()).append("increment:\n");
            indentLevel++;
            sb.append(node.getIncrement().accept(this));
            indentLevel--;
        }
        sb.append(indent()).append("body:\n");
        indentLevel++;
        for (Statement stmt : node.getBody()) {
            sb.append(stmt.accept(this));
        }
        indentLevel--;
        indentLevel--;
        return sb.toString();
    }

    @Override
    public String visit(DoWhileStatement node) {
        StringBuilder sb = new StringBuilder();
        sb.append(indent()).append("DoWhileStatement:\n");
        indentLevel++;
        sb.append(indent()).append("body:\n");
        indentLevel++;
        for (Statement stmt : node.getBody()) {
            sb.append(stmt.accept(this));
        }
        indentLevel--;
        sb.append(indent()).append("condition:\n");
        indentLevel++;
        sb.append(node.getCondition().accept(this));
        indentLevel--;
        indentLevel--;
        return sb.toString();
    }

    @Override
    public String visit(ReturnStatement node) {
        StringBuilder sb = new StringBuilder();
        sb.append(indent()).append("ReturnStatement:\n");
        if (node.getValue() != null) {
            indentLevel++;
            sb.append(node.getValue().accept(this));
            indentLevel--;
        }
        return sb.toString();
    }

    @Override
    public String visit(BreakStatement node) {
        return indent() + "BreakStatement\n";
    }

    @Override
    public String visit(ContinueStatement node) {
        return indent() + "ContinueStatement\n";
    }

    @Override
    public String visit(ExpressionStatement node) {
        StringBuilder sb = new StringBuilder();
        sb.append(indent()).append("ExpressionStatement:\n");
        indentLevel++;
        sb.append(node.getExpression().accept(this));
        indentLevel--;
        return sb.toString();
    }

    @Override
    public String visit(BlockStatement node) {
        StringBuilder sb = new StringBuilder();
        sb.append(indent()).append("BlockStatement:\n");
        indentLevel++;
        for (Statement stmt : node.getStatements()) {
            sb.append(stmt.accept(this));
        }
        indentLevel--;
        return sb.toString();
    }

    // ============== EXPRESSIONS ==============

    @Override
    public String visit(BinaryExpression node) {
        StringBuilder sb = new StringBuilder();
        sb.append(indent()).append("BinaryExpression: ").append(node.getOperator()).append("\n");
        indentLevel++;
        sb.append(indent()).append("left:\n");
        indentLevel++;
        sb.append(node.getLeft().accept(this));
        indentLevel--;
        sb.append(indent()).append("right:\n");
        indentLevel++;
        sb.append(node.getRight().accept(this));
        indentLevel--;
        indentLevel--;
        return sb.toString();
    }

    @Override
    public String visit(UnaryExpression node) {
        StringBuilder sb = new StringBuilder();
        sb.append(indent()).append("UnaryExpression: ").append(node.getOperator()).append("\n");
        indentLevel++;
        sb.append(node.getOperand().accept(this));
        indentLevel--;
        return sb.toString();
    }

    @Override
    public String visit(LiteralExpression node) {
        Object value = node.getValue();
        String type = value == null ? "null" : value.getClass().getSimpleName();
        return indent() + "Literal(" + type + "): " + value + "\n";
    }

    @Override
    public String visit(VariableExpression node) {
        return indent() + "Variable: " + node.getName() + "\n";
    }

    @Override
    public String visit(ListExpression node) {
        StringBuilder sb = new StringBuilder();
        sb.append(indent()).append("ListExpression:\n");
        indentLevel++;
        int index = 0;
        for (Expression element : node.getElements()) {
            sb.append(indent()).append("[").append(index++).append("]:\n");
            indentLevel++;
            sb.append(element.accept(this));
            indentLevel--;
        }
        indentLevel--;
        return sb.toString();
    }
}

