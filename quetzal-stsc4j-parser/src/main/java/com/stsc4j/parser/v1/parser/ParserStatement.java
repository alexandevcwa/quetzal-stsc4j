package com.stsc4j.parser.v1.parser;

import com.stsc4j.lexer.Token;
import com.stsc4j.lexer.TokenType;
import com.stsc4j.parser.v1.ast.*;

import java.util.ArrayList;
import java.util.List;

public class ParserStatement {
    private final TokenStream tokenStream;
    private final ParserExpressions parserExpressions;
    private final ParserPrincipal parserPrincipal;
    private final ParserJsnExpression parserJsnExpression;

    public ParserStatement(TokenStream tokenStream, ParserExpressions parserExpressions, ParserPrincipal parserPrincipal,
                           ParserJsnExpression parserJsnExpression) {
        this.tokenStream = tokenStream;
        this.parserExpressions = parserExpressions;
        this.parserPrincipal = parserPrincipal;
        this.parserJsnExpression = parserJsnExpression;
    }

    public Statement parseIf() {
        tokenStream.consume(TokenType.LEFT_PARENT, "Se esperaba '(' después del si.");
        Expression condition = parserExpressions.parseExpression();
        tokenStream.consume(TokenType.RIGHT_PARENT, "Se esperaba ')' después de la condición.");
        Statement thenBranch = parseBlock();
        Statement elseBranch = null;

        if (tokenStream.match(TokenType.ELSE)) {
            if (tokenStream.match(TokenType.IF)) {
                elseBranch = parseIf();
            } else {
                elseBranch = parseBlock();
            }
        }
        return new StatementIf(condition, thenBranch, elseBranch);
    }

    public Statement parseBlock() {
        tokenStream.consume(TokenType.BRACES_OPEN, "Se esperaba { al inicio de bloque.");
        List<Statement> statements = new ArrayList<>();

        while (!tokenStream.show().getType().equals(TokenType.BRACES_CLOSE) && !tokenStream.isAtEnd()) {
            statements.add(parserPrincipal.parseNext());
        }
        tokenStream.consume(TokenType.BRACES_CLOSE, "Se esperaba } al final de bloque.");
        return new StatementBlock(statements);
    }

    public Statement parseJsn() {
        tokenStream.consume(TokenType.JSN, "Se esperaba JSN");
        boolean isMutable = tokenStream.match(TokenType.MUTABLE_VARIABLE);
        Token identifier = tokenStream.consume(TokenType.IDENTIFIER, "Se esperaba una identificador para el JSN");
        tokenStream.consume(TokenType.EQUAL, "Se esperaba '=' luego del identificador del JSN");
        ExpressionJsnBlock expression = parserJsnExpression.parserJsnExpression();
        return new StatementJsn(identifier, isMutable, expression);
    }

    public Statement parseFunction() {
        Token returnValue = null;
        if (tokenStream.notMatch(TokenType.PRIMITIVE_INTEGER, TokenType.PRIMITIVE_DECIMAL, TokenType.PRIMITIVE_STRING,
                TokenType.PRIMITIVE_BOOLEAN, TokenType.PRIMITIVE_VOID)) {
            throw new RuntimeException("Se esperaba un tipo de retorno para la función");
        }
        returnValue = tokenStream.before();
        Token identified = tokenStream.consume(TokenType.IDENTIFIER, "Se esperaba un identificador para la función");
        tokenStream.consume(TokenType.LEFT_PARENT, "Se esperaba '(' después del identificador de la función");

        ParserFunctionParameter pParameter = new ParserFunctionParameter(tokenStream);
        List<Statement> parameters = pParameter.parseStatements();
        tokenStream.consume(TokenType.RIGHT_PARENT, "Se esperaba ')' después de los parámetros de la función");
        tokenStream.consume(TokenType.BRACES_OPEN, "Se esperaba '{' al inicio del bloque de la función");

        // Parseo de bloque
        List<Statement> block = parserPrincipal.parse(TokenType.BRACES_CLOSE);
        tokenStream.consume(TokenType.BRACES_CLOSE, "Se esperaba '}' al final del bloque de la función");
        return new StatementFunction(returnValue, identified, parameters, new StatementBlock(block));
    }

    public Statement parseReturn() {
        tokenStream.consume(TokenType.RETURN, "Se esperaba 'retornar'");
        Expression returnExpression = parserExpressions.parseExpression();
        return new StatementReturn(returnExpression);
    }
}
