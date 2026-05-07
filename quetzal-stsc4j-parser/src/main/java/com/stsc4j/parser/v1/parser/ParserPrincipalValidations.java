package com.stsc4j.parser.v1.parser;

import com.stsc4j.lexer.TokenType;

public abstract class ParserPrincipalValidations {
    protected final TokenStream tokenStream;

    public ParserPrincipalValidations(TokenStream tokenStream) {
        this.tokenStream = tokenStream;
    }

    protected boolean isVariableDeclaration() {
        return false;
    }

    protected boolean isFunctionDeclaration() {
        return false;
    }

    protected boolean isAssignmentOrIncDec() {
        return false;
    }

    protected boolean isIncrementalDecremental() {
        if (tokenStream.notMatch(TokenType.IDENTIFIER)) {
            return false;
        }
        if(tokenStream.notMatch(TokenType.PLUS,TokenType.MINUS)){
            tokenStream.back();
            return false;
        }
        if(tokenStream.notMatch(TokenType.PLUS,TokenType.MINUS)){
            tokenStream.back(2);
            return false;
        }
        tokenStream.back(3);
        return true;
    }
}
