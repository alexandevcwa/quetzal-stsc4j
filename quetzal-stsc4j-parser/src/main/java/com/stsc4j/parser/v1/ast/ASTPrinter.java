package com.stsc4j.parser.v1.ast;

import com.stsc4j.lexer.Token;

import java.util.Arrays;
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
        return getIndent() + "Variable: " + expressionVariable.token.getLexeme() + " (" + expressionVariable.token.getType().toString() + ")";
    }

    @Override
    public String visit(ExpressionLiteral expressionLiteral) {
        return getIndent() + "Literal: " + expressionLiteral.value + " (" + expressionLiteral.token.getType().toString() + ")";
    }

    @Override
    public String visit(ExpressionBinary expressionBinary) {
        StringBuilder sb = new StringBuilder();
        String operator = null;
        if (null != expressionBinary.operators) {
            operator = Arrays.stream(expressionBinary.operators).map(Token::getLexeme).reduce((a, b) -> a + b).orElse("");
        } else {
            operator = expressionBinary.operator.getLexeme();
        }
        sb.append(getIndent()).append("Binary Operation: ").append(operator).append("\n");
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
    public String visit(ExpressionTernary expressionTernary) {
        StringBuilder sb = new StringBuilder();
        sb.append(getIndent()).append("Ternary Operation\n");
        indentLevel++;
        sb.append(getIndent()).append("├─ Condition:\n");
        indentLevel++;
        sb.append(expressionTernary.binary.accept(this)).append("\n");
        indentLevel--;
        sb.append(getIndent()).append("├─ True:\n");
        indentLevel++;
        sb.append(expressionTernary.left.accept(this)).append("\n");
        indentLevel--;
        sb.append(getIndent()).append("└─ False:\n");
        indentLevel++;
        sb.append(expressionTernary.right.accept(this));
        indentLevel -= 2;
        return sb.toString();
    }

    @Override
    public String visit(ExpressionMethodCall expressionMethodCall) {
        StringBuilder sb = new StringBuilder();
        sb.append(getIndent()).append("Method Call: ").append(expressionMethodCall.methodName.getLexeme()).append("\n");
        indentLevel++;
        sb.append(getIndent()).append("├─ Object:\n");
        indentLevel++;
        sb.append(expressionMethodCall.object.accept(this)).append("\n");
        indentLevel--;
        sb.append(getIndent()).append("└─ Arguments: ").append(expressionMethodCall.args.size()).append("\n");
        indentLevel++;
        List<Expression> arguments = expressionMethodCall.args;
        for (int i = 0; i < arguments.size(); i++) {
            if (i < arguments.size() - 1) {
                sb.append(getIndent()).append("├─ ");
            } else {
                sb.append(getIndent()).append("└─ ");
            }
            Expression arg = arguments.get(i);
            String argOutput = arg.accept(this);
            String[] lines = argOutput.split("\n");
            sb.append(lines[0].replaceFirst("^" + INDENT.repeat(indentLevel), ""));
            for (int j = 1; j < lines.length; j++) {
                sb.append("\n").append(lines[j]);
            }
            if (i < arguments.size() - 1) {
                sb.append("\n");
            }
        }
        indentLevel -= 2;
        return sb.toString();
    }

    @Override
    public String visit(ExpressionIndexAccess expressionIndexAccess) {
        StringBuilder sb = new StringBuilder();
        sb.append(getIndent()).append("Index Access\n");
        indentLevel++;
        sb.append(getIndent()).append("├─ Object:\n");
        indentLevel++;
        sb.append(expressionIndexAccess.objectList.accept(this)).append("\n");
        indentLevel--;

        // Manejo de índices múltiples
        if (expressionIndexAccess.indexList != null && !expressionIndexAccess.indexList.isEmpty()) {
            sb.append(getIndent()).append("└─ Indices: ").append(expressionIndexAccess.indexList.size()).append(" index(es)\n");
            indentLevel++;
            List<Expression> indices = expressionIndexAccess.indexList;
            for (int i = 0; i < indices.size(); i++) {
                if (i < indices.size() - 1) {
                    sb.append(getIndent()).append("├─ ");
                } else {
                    sb.append(getIndent()).append("└─ ");
                }
                Expression idx = indices.get(i);
                String idxOutput = idx.accept(this);
                String[] lines = idxOutput.split("\n");
                sb.append(lines[0].replaceFirst("^" + INDENT.repeat(indentLevel), ""));
                for (int j = 1; j < lines.length; j++) {
                    sb.append("\n").append(lines[j]);
                }
                if (i < indices.size() - 1) {
                    sb.append("\n");
                }
            }
            indentLevel--;
        } else if (expressionIndexAccess.index != null) {
            // Backwards compatibility con la versión deprecated
            sb.append(getIndent()).append("└─ Index:\n");
            indentLevel++;
            sb.append(expressionIndexAccess.index.accept(this));
            indentLevel--;
        }
        indentLevel--;
        return sb.toString();
    }

    @Override
    public String visit(ExpressionList expressionList) {
        // Si la lista está vacía o es nula
        if (expressionList.expressions == null || expressionList.expressions.isEmpty()) {
            return getIndent() + "List []";
        }

        StringBuilder sb = new StringBuilder();
        sb.append(getIndent()).append("List [").append(expressionList.expressions.size()).append(" element(s)]\n");
        indentLevel++;

        List<Expression> expressions = expressionList.expressions;
        for (int i = 0; i < expressions.size(); i++) {
            if (i < expressions.size() - 1) {
                sb.append(getIndent()).append("├─ ");
            } else {
                sb.append(getIndent()).append("└─ ");
            }
            Expression expr = expressions.get(i);
            String exprOutput = expr.accept(this);
            String[] lines = exprOutput.split("\n");
            sb.append(lines[0].replaceFirst("^" + INDENT.repeat(indentLevel), ""));
            for (int j = 1; j < lines.length; j++) {
                sb.append("\n").append(lines[j]);
            }
            if (i < expressions.size() - 1) {
                sb.append("\n");
            }
        }
        indentLevel--;
        return sb.toString();
    }

    @Override
    public String visit(ExpressionJsnBlock expressionJsnBlock) {
        // Si el bloque está vacío o es nulo
        if (expressionJsnBlock.expressions == null || expressionJsnBlock.expressions.isEmpty()) {
            return getIndent() + "JSN Block {}";
        }

        StringBuilder sb = new StringBuilder();
        sb.append(getIndent()).append("JSN Block { ").append(expressionJsnBlock.expressions.size()).append(" element(s) }\n");
        indentLevel++;

        List<ExpressionJsn> expressions = expressionJsnBlock.expressions;
        for (int i = 0; i < expressions.size(); i++) {
            if (i < expressions.size() - 1) {
                sb.append(getIndent()).append("├─ ");
            } else {
                sb.append(getIndent()).append("└─ ");
            }
            ExpressionJsn expr = expressions.get(i);
            String exprOutput = expr.accept(this);
            String[] lines = exprOutput.split("\n");
            sb.append(lines[0].replaceFirst("^" + INDENT.repeat(indentLevel), ""));
            for (int j = 1; j < lines.length; j++) {
                sb.append("\n").append(lines[j]);
            }
            if (i < expressions.size() - 1) {
                sb.append("\n");
            }
        }
        indentLevel--;
        return sb.toString();
    }

    @Override
    public String visit(ExpressionJsn expressionJsn) {
        StringBuilder sb = new StringBuilder();
        sb.append(getIndent()).append("JSN Property: ").append(expressionJsn.key.getLexeme()).append("\n");
        indentLevel++;

        // Si tiene un valor simple
        if (expressionJsn.value != null) {
            sb.append(getIndent()).append("└─ Value:\n");
            indentLevel++;
            sb.append(expressionJsn.value.accept(this));
            indentLevel--;
        }
        // Si tiene una lista de valores
        else if (expressionJsn.values != null && !expressionJsn.values.isEmpty()) {
            sb.append(getIndent()).append("└─ Values: ").append(expressionJsn.values.size()).append(" element(s)\n");
            indentLevel++;
            List<Expression> values = expressionJsn.values;
            for (int i = 0; i < values.size(); i++) {
                if (i < values.size() - 1) {
                    sb.append(getIndent()).append("├─ ");
                } else {
                    sb.append(getIndent()).append("└─ ");
                }
                Expression value = values.get(i);
                String valueOutput = value.accept(this);
                String[] lines = valueOutput.split("\n");
                sb.append(lines[0].replaceFirst("^" + INDENT.repeat(indentLevel), ""));
                for (int j = 1; j < lines.length; j++) {
                    sb.append("\n").append(lines[j]);
                }
                if (i < values.size() - 1) {
                    sb.append("\n");
                }
            }
            indentLevel--;
        }

        indentLevel--;
        return sb.toString();
    }

    @Override
    public String visit(ExpressionPropertyAccess expressionPropertyAccess) {
        StringBuilder sb = new StringBuilder();
        sb.append(getIndent()).append("Property Access: ").append(expressionPropertyAccess.propertyName.getLexeme()).append("\n");
        indentLevel++;
        sb.append(getIndent()).append("└─ Object:\n");
        indentLevel++;
        sb.append(expressionPropertyAccess.object.accept(this));
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

    @Override
    public String visit(StatementList statementList) {
        StringBuilder sb = new StringBuilder();
        String mutability = statementList.mutable ? "mutable" : "immutable";
        sb.append(getIndent()).append("List Declaration (").append(mutability).append(")\n");
        indentLevel++;

        // Mostrar el tipo en notación simplificada
        String typeNotation = getTypeListNotation(statementList.type);
        sb.append(getIndent()).append("├─ Type: ").append(typeNotation).append("\n");

        // Mostrar nombre
        sb.append(getIndent()).append("├─ Name: ").append(statementList.listName.getLexeme()).append("\n");

        // Mostrar número de elementos
        sb.append(getIndent()).append("└─ Elements: ").append(statementList.expressionList.expressions.size()).append(" element(s)\n");

        indentLevel++;
        List<Expression> expressions = statementList.expressionList.expressions;
        for (int i = 0; i < expressions.size(); i++) {
            if (i < expressions.size() - 1) {
                sb.append(getIndent()).append("├─ ");
            } else {
                sb.append(getIndent()).append("└─ ");
            }
            Expression expr = expressions.get(i);
            String exprOutput = expr.accept(this);
            String[] lines = exprOutput.split("\n");
            sb.append(lines[0].replaceFirst("^" + INDENT.repeat(indentLevel), ""));
            for (int j = 1; j < lines.length; j++) {
                sb.append("\n").append(lines[j]);
            }
            if (i < expressions.size() - 1) {
                sb.append("\n");
            }
        }
        indentLevel -= 2;
        return sb.toString();
    }

    @Override
    public String visit(TypeList typeList) {
        StringBuilder sb = new StringBuilder();
        sb.append(getIndent()).append("List Type Structure:\n");
        indentLevel++;
        printTypeListStructure(sb, typeList, 0);
        indentLevel--;
        return sb.toString();
    }

    /**
     * Imprime recursivamente la estructura de un TypeList anidado
     */
    private void printTypeListStructure(StringBuilder sb, Type type, int depth) {
        if (type instanceof TypeList) {
            TypeList typeList = (TypeList) type;
            sb.append(getIndent()).append("├─ List Dimension ");
            sb.append(depth + 1).append(":\n");
            indentLevel++;
            printTypeListStructure(sb, typeList.elementType, depth + 1);
            indentLevel--;
        } else if (type instanceof TypePrimitive) {
            TypePrimitive typePrim = (TypePrimitive) type;
            sb.append(getIndent()).append("└─ Base Type: ")
                    .append(typePrim.primitiveType.getLexeme()).append("\n");
        }
    }

    /**
     * Genera la notación simplificada de un TypeList
     */
    private String getTypeListNotation(TypeList typeList) {
        StringBuilder sb = new StringBuilder();
        Type elementType = typeList;
        int depth = 0;

        // Contar profundidad y abrir brackets
        while (elementType instanceof TypeList) {
            sb.append("List<");
            elementType = ((TypeList) elementType).elementType;
            depth++;
        }

        // Agregar tipo base
        if (elementType instanceof TypePrimitive) {
            sb.append(((TypePrimitive) elementType).primitiveType.getLexeme());
        }

        // Cerrar todos los brackets
        for (int i = 0; i <= depth; i++) {
            sb.append(">");
        }

        return sb.toString();
    }

    @Override
    public String visit(TypePrimitive type) {
        return getIndent() + "Type: " + type.primitiveType.getLexeme();
    }

    @Override
    public String visit(StatementJsn statementJsn) {
        StringBuilder sb = new StringBuilder();
        String mutability = statementJsn.mutable ? "mutable" : "immutable";
        sb.append(getIndent()).append("JSN Declaration (").append(mutability).append(")\n");
        indentLevel++;
        sb.append(getIndent()).append("├─ Name: ").append(statementJsn.identifier.getLexeme()).append("\n");
        sb.append(getIndent()).append("└─ Value:\n");
        indentLevel++;
        sb.append(statementJsn.accept(this));
        indentLevel -= 2;
        return sb.toString();
    }

    @Override
    public String visit(StatementReturn statementReturn) {
        StringBuilder sb = new StringBuilder();
        sb.append(getIndent()).append("Return Statement\n");
        indentLevel++;
        sb.append(statementReturn.returnExpression.accept(this));
        indentLevel--;
        return sb.toString();
    }

    @Override
    public String visit(StatementLoopWhile statementLoopWhile) {
        StringBuilder sb = new StringBuilder();
        sb.append(getIndent()).append("While Loop\n");
        indentLevel++;
        sb.append(getIndent()).append("├─ Condition:\n");
        indentLevel++;
        sb.append(statementLoopWhile.condition.accept(this)).append("\n");
        indentLevel--;
        sb.append(getIndent()).append("└─ Block:\n");
        indentLevel++;
        sb.append(statementLoopWhile.block.accept(this));
        indentLevel -= 2;
        return sb.toString();
    }

    @Override
    public String visit(StatementLoopDoWhile statementLoopDoWhile) {
        StringBuilder sb = new StringBuilder();
        sb.append(getIndent()).append("Do-While Loop\n");
        indentLevel++;
        sb.append(getIndent()).append("├─ Block:\n");
        indentLevel++;
        sb.append(statementLoopDoWhile.block.accept(this)).append("\n");
        indentLevel--;
        sb.append(getIndent()).append("└─ Condition:\n");
        indentLevel++;
        sb.append(statementLoopDoWhile.condition.accept(this));
        indentLevel -= 2;
        return sb.toString();
    }

    @Override
    public String visit(StatementFunction statementFunction) {
        StringBuilder sb = new StringBuilder();
        sb.append(getIndent()).append("Function Declaration\n");
        indentLevel++;
        sb.append(getIndent()).append("├─ Return Type: ").append(statementFunction.returnValue.getLexeme()).append("\n");
        sb.append(getIndent()).append("├─ Name: ").append(statementFunction.identified.getLexeme()).append("\n");
        sb.append(getIndent()).append("├─ Parameters: ").append(statementFunction.parameters.size()).append(" parameter(s)\n");

        // Mostrar parámetros
        indentLevel++;
        for (int i = 0; i < statementFunction.parameters.size(); i++) {
            if (i < statementFunction.parameters.size() - 1) {
                sb.append(getIndent()).append("├─ ");
            } else {
                sb.append(getIndent()).append("└─ ");
            }
            Statement param = statementFunction.parameters.get(i);
            String paramOutput = param.accept(this);
            String[] lines = paramOutput.split("\n");
            sb.append(lines[0].replaceFirst("^" + INDENT.repeat(indentLevel), ""));
            for (int j = 1; j < lines.length; j++) {
                sb.append("\n").append(lines[j]);
            }
            if (i < statementFunction.parameters.size() - 1) {
                sb.append("\n");
            }
        }
        indentLevel--;

        sb.append("\n");
        sb.append(getIndent()).append("└─ Block:\n");
        indentLevel++;
        sb.append(statementFunction.block.accept(this));
        indentLevel -= 2;

        return sb.toString();
    }

    @Override
    public String visit(StatementFunctionParameter statementFunctionParameter) {
        StringBuilder sb = new StringBuilder();
        sb.append(getIndent()).append("Parameter: ");
        sb.append(statementFunctionParameter.type.getLexeme());
        sb.append(" ");
        sb.append(statementFunctionParameter.identified.getLexeme());
        return sb.toString();
    }
}
