package com.stsc4j.parser.v1.parser;

import com.stsc4j.lexer.Token;
import com.stsc4j.lexer.TokenType;
import com.stsc4j.parser.v1.ast.Expression;
import com.stsc4j.parser.v1.ast.Statement;
import com.stsc4j.parser.v1.ast.StatementExpression;

import java.util.ArrayList;
import java.util.List;

public class ParserPrincipal extends ParserPrincipalValidations {

    private final ParserStatement parserStatement;

    public ParserPrincipal(TokenStream stream) {
        super(stream);
        this.parserStatement = new ParserStatement(stream, this);
    }

    public List<Statement> parse() {
        List<Statement> ast = new ArrayList<>();
        while (!tokenStream.isAtEnd()) {
            ast.add(parseNext());
        }
        return ast;
    }

    public List<Statement> parse(TokenType type) {
        List<Statement> ast = new ArrayList<>();
        boolean stop = false;
        while (!stop) {
            ast.add(parseNext());
            if (tokenStream.matchNotAdvance(type)) {
                stop = true;
            }
        }
        return ast;
    }

    public Statement parseNext() {

        // Parser de Asignaciones e Incrementales/Decrementales
        if (isIncrementalDecremental()) {
            return parserStatement.parseIncremental().parseStatement();
        }

        // Parser de Llamadas a Métodos y Funciones
        if (isFunctionCall()) {
            Expression expr = parserStatement.parseExpressions().parseExpression();
            return new StatementExpression(expr);
        }

        // Parser (Funciones)
        if (isFunctionDeclaration()) {
            return parserStatement.parseFunction().parseStatement();
        }

        // Parser (Matrix Assignation)
        if (isMatrixAssignation()) {
            return parserStatement.parseMatrixAssignation().parseStatement();
        }

        // Parser (Variables)
        if (isVariableDeclaration()) {
            return parserStatement.parseVar().parseStatement();
        }

        // Parser (Listas)
        if (isListDeclaration()) {
            return parserStatement.parseList().parseStatement();
        }

        // Parser (If)
        if (isIfDeclaration()) {
            return parserStatement.parseIf().parseStatement();
        }

        // Parser (Return)
        if (isReturnDeclaration()) {
            return parserStatement.parseReturn().parseStatement();
        }

        // Parser (JSN)
        if (isJSNDeclaration()) {
            return parserStatement.parseJsn().parseStatement();
        }

        // Parser (Loop While)
        if (isLoopWhileDeclaration()) {
            return parserStatement.parseLoopWhile().parseStatement();
        }

        // Parser (Loop Do While)
        if (isLoopDoWhileDeclaration()) {
            return parserStatement.parseLoopDoWhile().parseStatement();
        }

        // Parser (Loop For Each)
        if (isLoopForEach()) {
            return parserStatement.parseLoopForEach().parseStatement();
        }

        // Parser (Loop For)
        if (isLoopForDeclaration()) {
            return parserStatement.parseLoopFor().parseStatement();
        }

        // Parser (Try Catch Finally)
        if (isTryCatchDeclaration()) {
            return parserStatement.parseTryCatchFinally().parseStatement();
        }

        // Parser (Console.Out)
        if (isConsoleClass()) {
            return parserStatement.parseConsoleOut().parseStatement();
        }

        // Parser (Break)
        if (isBreakDeclaration()) {
            return parserStatement.parseBreak().parseStatement();
        }

        // Parser (Continue)
        if (isContinueDeclaration()) {
            return parserStatement.parseContinue().parseStatement();
        }

        // Parser (Throw)
        if (isThrowDeclaration()) {
            return parserStatement.parseThrow().parseStatement();
        }

        Token current = tokenStream.show();
        String sb = "Token no reconocido.......................................\n" +
                String.format("%-18s -> %s\n", "Tipo", current.type.toString()) +
                String.format("%-18s -> %s\n", "Lexema", current.lexeme) +
                String.format("%-18s -> %s\n", "Linea", current.line);
        throw new RuntimeException(sb);
    }
}
