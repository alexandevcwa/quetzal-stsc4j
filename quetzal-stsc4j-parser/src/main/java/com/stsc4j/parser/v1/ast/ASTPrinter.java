package com.stsc4j.parser.v1.ast;

import java.util.List;

public class ASTPrinter implements Visitor<String> {

    private static final String INDENT = "  ";
    private int indentLevel = 0;

    /**
     * Retorna la salida impresa de un nodo AST
     */
    public String print(ASTNode node) {
        this.indentLevel = 0;
        return node.accept(this);
    }

    private String getIndent() {
        return INDENT.repeat(indentLevel);
    }

    @Override
    public String visit(ExpressionVariable expressionVariable) {
        return getIndent() + "Variable: " + expressionVariable.token.getLexeme();
    }

    @Override
    public String visit(ExpressionLiteral expressionLiteral) {
        String type = expressionLiteral.value == null ? "null" : expressionLiteral.value.getClass().getSimpleName();
        return getIndent() + "Literal: " + expressionLiteral.value + " (" + type + ")";
    }

    @Override
    public String visit(ExpressionBinary expressionBinary) {
        StringBuilder sb = new StringBuilder();
        sb.append(getIndent()).append("Binary Operation: ").append(expressionBinary.operator.getLexeme()).append("\n");
        indentLevel++;
        sb.append(getIndent()).append("├─ Left:\n");
        indentLevel++;
        sb.append(expressionBinary.left.accept(this)).append("\n");
        indentLevel--;
        sb.append(getIndent()).append("└─ Right:\n");
        indentLevel++;
        sb.append(expressionBinary.right.accept(this));
        indentLevel -= 2;
        return sb.toString();
    }

    @Override
    public String visit(StatementIf statementIf) {
        StringBuilder sb = new StringBuilder();
        sb.append(getIndent()).append("If Statement\n");
        indentLevel++;
        sb.append(getIndent()).append("├─ Condition:\n");
        indentLevel++;
        sb.append(statementIf.condition.accept(this)).append("\n");
        indentLevel--;
        sb.append(getIndent()).append("├─ Then:\n");
        indentLevel++;
        sb.append(statementIf.thenStatement.accept(this)).append("\n");
        indentLevel--;
        if (statementIf.elseStatement != null) {
            sb.append(getIndent()).append("└─ Else:\n");
            indentLevel++;
            sb.append(statementIf.elseStatement.accept(this));
            indentLevel--;
        }
        indentLevel--;
        return sb.toString();
    }

    @Override
    public String visit(StatementBlock statementBlock) {
        StringBuilder sb = new StringBuilder();
        sb.append(getIndent()).append("Block { ").append(statementBlock.statements.size()).append(" statement(s) }\n");
        indentLevel++;
        List<Statement> statements = statementBlock.statements;
        for (int i = 0; i < statements.size(); i++) {
            if (i < statements.size() - 1) {
                sb.append(getIndent()).append("├─ ");
            } else {
                sb.append(getIndent()).append("└─ ");
            }
            Statement stmt = statements.get(i);
            String stmtOutput = stmt.accept(this);
            // Remover la indentación del primer nivel de la salida anidada
            String[] lines = stmtOutput.split("\n");
            sb.append(lines[0].replaceFirst("^" + INDENT.repeat(indentLevel), ""));
            for (int j = 1; j < lines.length; j++) {
                sb.append("\n").append(lines[j]);
            }
            if (i < statements.size() - 1) {
                sb.append("\n");
            }
        }
        indentLevel--;
        return sb.toString();
    }

    @Override
    public String visit(StatementExpression statementExpression) {
        StringBuilder sb = new StringBuilder();
        sb.append(getIndent()).append("Expression Statement\n");
        indentLevel++;
        sb.append(statementExpression.expression.accept(this));
        indentLevel--;
        return sb.toString();
    }

    @Override
    public String visit(StatementVariable statementVariable) {
        StringBuilder sb = new StringBuilder();
        String mutability = statementVariable.mutable ? "mutable" : "immutable";
        sb.append(getIndent()).append("Variable Declaration (").append(mutability).append(")\n");
        indentLevel++;
        sb.append(getIndent()).append("├─ Type: ").append(statementVariable.typo.getLexeme()).append("\n");
        sb.append(getIndent()).append("├─ Name: ").append(statementVariable.name.getLexeme()).append("\n");
        sb.append(getIndent()).append("└─ Initializer:\n");
        indentLevel++;
        sb.append(statementVariable.initialValue.accept(this));
        indentLevel -= 2;
        return sb.toString();
    }
}
