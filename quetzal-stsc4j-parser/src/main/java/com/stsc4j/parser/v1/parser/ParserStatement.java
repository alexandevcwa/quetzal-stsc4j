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
        boolean isMutable = tokenStream.match(TokenType.MUTABLE_VARIABLE);
        Token identifier = tokenStream.consume(TokenType.IDENTIFIER, "Se esperaba una identificador para el JSN");
        tokenStream.consume(TokenType.EQUAL, "Se esperaba '=' luego del identificador del JSN");
        ExpressionJsnBlock expression = parserJsnExpression.parserJsnExpression();
        return new StatementJsn(identifier, isMutable, expression);
    }

    public Statement parseSyncFunction() {
        return null;
    }
}
