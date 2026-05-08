package com.stsc4j.parser.v1.parser;

import com.stsc4j.lexer.TokenType;
import com.stsc4j.parser.v1.ast.*;
import com.stsc4j.parser.v1.exception.ParserException;

public class ParserLoopFor extends Parser {

    private final TokenStream tokenStream;
    private final ParserDeclaration parserDeclaration;
    private final ParserExpressions parserExpressions;
    private final ParserBlock parserBlock;

    public ParserLoopFor(TokenStream tokenStream, ParserDeclaration parserDeclaration, ParserExpressions parserExpressions, ParserBlock parserBlock) {
        this.tokenStream = tokenStream;
        this.parserDeclaration = parserDeclaration;
        this.parserExpressions = parserExpressions;
        this.parserBlock = parserBlock;
    }

    @Override
    public Statement parseStatement() {
        tokenStream.consume(TokenType.LOOP_FOR, "Se esperaba 'para' en la declaración del ciclo");
        tokenStream.consume(TokenType.LEFT_PARENT, "Se esperaba '(' en la declaración de ciclo 'para'");

        // Declaración de variable o referencia a variable ya declarada
        boolean isIdentifier = tokenStream.matchNotAdvance(TokenType.IDENTIFIER);
        ExpressionVariable identifier = null;
        if (isIdentifier) {
            Expression expression = parserExpressions.parseExpression();
            if (!(expression instanceof ExpressionVariable)) {
                throw new ParserException("Se esperava una declaración de variable o la referencia a una variable ya declarada en el ciclo 'para'");
            }
            identifier = (ExpressionVariable) expression;
        }
        StatementVariable declaration = null;
        if (identifier == null) {
            declaration = (StatementVariable) parserDeclaration.parseStatement();
        }
        tokenStream.consume(TokenType.SEMICOLON, "Se esperaba ';' para separar la expresión de la declaración on referencia de la variable de ciclo 'para'");

        // Condicional del ciclo 'para'
        ExpressionBinary condition;
        Expression temp1 = parserExpressions.parseExpression();
        if (temp1 instanceof ExpressionBinary) {
            condition = (ExpressionBinary) temp1;
        } else {
            throw new ParserException("Se esperaba una expresión condicional en la declaración del ciclo 'para'");
        }
        tokenStream.consume(TokenType.SEMICOLON, "Se esperaba ';' para separar la expresión condicional de la expresión de incremento del ciclo 'para'");

        // Operador de incremento
        ExpressionIncDec incDec;
        Expression temp2 = parserExpressions.parseExpression();
        if (temp2 instanceof ExpressionIncDec) {
            incDec = (ExpressionIncDec) temp2;
        } else {
            throw new ParserException("Se esperaba una expresión de incremento o decremento en la declaración del ciclo 'para'");
        }
        tokenStream.consume(TokenType.RIGHT_PARENT, "Se esperaba ')' al final de la declaración del ciclo 'para'");

        // Bloque de código a ejecutar en cada iteración del ciclo 'para'
        Statement block = parserBlock.parseStatement();

        return new StatementLoopFor(
                (identifier != null ? identifier : declaration),
                condition, incDec, (StatementBlock) block
        );
    }
}
