package com.stsc4j.parser.v1.parser;

import com.stsc4j.lexer.Token;
import com.stsc4j.lexer.TokenType;
import com.stsc4j.parser.v1.ast.Expression;
import com.stsc4j.parser.v1.ast.Statement;
import com.stsc4j.parser.v1.ast.StatementVariable;

public class ParserDeclaration {
    private final TokenStream tokenStream;
    private final ParserExpressions parserExpressions;

    public ParserDeclaration(TokenStream tokenStream, ParserExpressions parserExpressions) {
        this.tokenStream = tokenStream;
        this.parserExpressions = parserExpressions;
    }

    public Statement parseVarDeclaration(){
        Token type = tokenStream.before();
        boolean isMutable = tokenStream.match(TokenType.MUTABLE_VARIABLE);
        Token name = tokenStream.consume(TokenType.IDENTIFIER,"Se esperaba el nombre de la variable.");

        Expression initialValue = null;
        if(tokenStream.match(TokenType.EQUAL)){
            initialValue = parserExpressions.parseExpression();
        }
        return new StatementVariable(type,isMutable,name,initialValue);
    }
}
