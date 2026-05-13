package com.stsc4j.parser.v1.parser;

import com.stsc4j.lexer.TokenType;

public abstract class ParserPrincipalValidations {
    protected final TokenStream tokenStream;

    public ParserPrincipalValidations(TokenStream tokenStream) {
        this.tokenStream = tokenStream;
    }

    protected boolean isVariableDeclaration() {
        return tokenStream.matchNotAdvance(TokenType.PRIMITIVE_INTEGER, TokenType.PRIMITIVE_DECIMAL, TokenType.PRIMITIVE_STRING,
                TokenType.PRIMITIVE_BOOLEAN, TokenType.IDENTIFIER);
    }

    protected boolean isFunctionCall() {
        if (tokenStream.match(TokenType.IDENTIFIER)) {
            if ((tokenStream.matchAndBack(TokenType.DOT) || tokenStream.matchAndBack(TokenType.LEFT_PARENT))) {
                return true;
            } else {
                tokenStream.back();
            }
        }
        return false;
    }

    protected boolean isFunctionDeclaration() {
        if (tokenStream.match(
                TokenType.PRIMITIVE_INTEGER,
                TokenType.PRIMITIVE_DECIMAL,
                TokenType.PRIMITIVE_STRING,
                TokenType.PRIMITIVE_BOOLEAN,
                TokenType.PRIMITIVE_VOID)
        ) {
            if (tokenStream.match(TokenType.IDENTIFIER)) {
                if (tokenStream.match(TokenType.LEFT_PARENT)) {
                    tokenStream.back(3);
                    return true;
                } else {
                    tokenStream.back(2);
                    return false;
                }
            } else {
                tokenStream.back();
                return false;
            }
        } else {
            return false;
        }
    }

    protected boolean isIncrementalDecremental() {
        if (tokenStream.notMatch(TokenType.IDENTIFIER)) {
            return false;
        }
        if (tokenStream.notMatch(TokenType.PLUS, TokenType.MINUS)) {
            tokenStream.back();
            return false;
        }
        if (tokenStream.notMatch(TokenType.PLUS, TokenType.MINUS)) {
            tokenStream.back(2);
            return false;
        }
        tokenStream.back(3);
        return true;
    }

    protected boolean isListDeclaration() {
        return tokenStream.matchNotAdvance(TokenType.LIST);
    }

    protected boolean isIfDeclaration() {
        return tokenStream.matchNotAdvance(TokenType.IF);
    }

    protected boolean isReturnDeclaration() {
        return tokenStream.matchNotAdvance(TokenType.RETURN);
    }

    protected boolean isJSNDeclaration() {
        return tokenStream.matchNotAdvance(TokenType.JSN);
    }

    protected boolean isLoopWhileDeclaration() {
        return tokenStream.matchNotAdvance(TokenType.LOOP_WHILE);
    }

    protected boolean isLoopDoWhileDeclaration() {
        return tokenStream.matchNotAdvance(TokenType.LOOP_DO);
    }

    protected boolean isLoopForEach() {
        if (tokenStream.notMatch(TokenType.LOOP_FOR)) {
            return false;
        }
        tokenStream.advance(4);
        if (tokenStream.notMatch(TokenType.LOOP_EACH_1, TokenType.LOOP_EACH_2)) {
            tokenStream.back(5);
            return false;
        }
        tokenStream.back(6);
        return true;
    }

    protected boolean isLoopForDeclaration() {
        return tokenStream.matchNotAdvance(TokenType.LOOP_FOR);
    }


    protected boolean isMatrixAssignation(){
        if(tokenStream.notMatch(TokenType.IDENTIFIER)){
            return false;
        }
        if(tokenStream.notMatch(TokenType.LEFT_PARENT)){
            tokenStream.back();
            return false;
        }
        return true;
    }

    protected boolean isTryCatchDeclaration() {
        return tokenStream.matchNotAdvance(TokenType.TRY);
    }

    protected boolean isConsoleClass(){
        return tokenStream.matchNotAdvance(TokenType.C_CONSOLE);
    }

    protected boolean isBreakDeclaration(){
        return tokenStream.matchNotAdvance(TokenType.BREAK);
    }

    protected boolean isContinueDeclaration(){
        return tokenStream.matchNotAdvance(TokenType.CONTINUE);
    }
}
