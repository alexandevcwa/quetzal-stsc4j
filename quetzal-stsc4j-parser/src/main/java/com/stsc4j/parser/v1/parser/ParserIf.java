package com.stsc4j.parser.v1.parser;

import com.stsc4j.lexer.TokenType;
import com.stsc4j.parser.v1.ast.Expression;
import com.stsc4j.parser.v1.ast.Statement;
import com.stsc4j.parser.v1.ast.StatementIf;

public class ParserIf extends Parser {

    private final TokenStream tokenStream;
    private final ParserBlock parserBlock;
    private final ParserExpression parserExpression;

    public ParserIf(TokenStream tokenStream, ParserExpression parserExpression, ParserBlock parserBlock) {
        this.tokenStream = tokenStream;
        this.parserExpression = parserExpression;
        this.parserBlock = parserBlock;
    }

    @Override
    public Statement parseStatement() {
        tokenStream.consume(TokenType.IF, "Se esperaba 'si'.");
        return parseIfChain();
    }

    /**
     * Parsea una cadena de if-else-if-else de forma optimizada.
     * Evita recursión profunda y maneja múltiples else-if eficientemente.
     */
    private Statement parseIfChain() {
        tokenStream.consume(TokenType.LEFT_PARENT, "Se esperaba '(' después del si.");
        Expression condition = parserExpression.parseExpression();
        tokenStream.consume(TokenType.RIGHT_PARENT, "Se esperaba ')' después de la condición.");
        Statement thenBranch = parserBlock.parseStatement();
        Statement elseBranch = null;

        if (tokenStream.match(TokenType.ELSE)) {
            if (tokenStream.matchNotAdvance(TokenType.IF)) {
                // Recursión lineal para else-if (no hay problema con profundidad)
                tokenStream.advance(); // Consumir el IF
                elseBranch = parseIfChain();
            } else {
                // else simple sin if
                elseBranch = parserBlock.parseStatement();
            }
        }
        return new StatementIf(condition, thenBranch, elseBranch);
    }
}
