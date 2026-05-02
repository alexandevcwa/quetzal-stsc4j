package com.stsc4j.parser.v1.parser;

public abstract class ParserPrincipalValidations {
    protected final TokenStream tokenStream;

    public ParserPrincipalValidations(TokenStream tokenStream) {
        this.tokenStream = tokenStream;
    }

    protected boolean isVariableDeclaration(){
        return false;
    }

    protected  boolean isFunctionDeclaration(){
        return false;
    }

    protected boolean isAssignmentOrIncDec(){
        return false;
    }
}
