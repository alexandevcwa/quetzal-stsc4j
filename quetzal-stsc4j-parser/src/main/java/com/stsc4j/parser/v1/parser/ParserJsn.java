package com.stsc4j.parser.v1.parser;

import com.stsc4j.lexer.Token;
import com.stsc4j.lexer.TokenType;
import com.stsc4j.parser.v1.ast.ExpressionJsnBlock;
import com.stsc4j.parser.v1.ast.Statement;
import com.stsc4j.parser.v1.ast.StatementJsn;

public class ParserJsn extends Parser {

    private final TokenStream tokenStream;
    private final ParserJsnExpression parserJsnExpression;

    public ParserJsn(TokenStream tokenStream, ParserExpression parserExpression) {
        this.tokenStream = tokenStream;
        this.parserJsnExpression = new ParserJsnExpression(tokenStream, parserExpression);
    }

    @Override
    public Statement parseStatement() {
        tokenStream.consume(TokenType.JSN, "Se esperaba JSN");
        boolean isMutable = tokenStream.match(TokenType.MUTABLE_VARIABLE);
        Token identifier = tokenStream.consume(TokenType.IDENTIFIER, "Se esperaba una identificador para el JSN");
        tokenStream.consume(TokenType.EQUAL, "Se esperaba '=' luego del identificador del JSN");
        ExpressionJsnBlock expression = parserJsnExpression.parserJsnExpression();
        return new StatementJsn(identifier, isMutable, expression);
    }
}
