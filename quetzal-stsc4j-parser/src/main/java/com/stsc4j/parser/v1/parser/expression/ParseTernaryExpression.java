package com.stsc4j.parser.v1.parser.expression;

import com.stsc4j.parser.v1.ast.Expression;
import com.stsc4j.parser.v1.ast.ExpressionBinary;

public interface ParseTernaryExpression {

    Expression parseTernaryExpression(ExpressionBinary expression);
}
