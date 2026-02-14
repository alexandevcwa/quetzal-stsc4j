package com.stsc4j.lexer.state;

import com.stsc4j.lexer.LexerContext;
import com.stsc4j.lexer.TokenType;

public class StringLiteralState implements  LexerState{

    private boolean isFirstDoubleQuote = true;

    @Override
    public void process(char c, int length, int index, LexerContext context) {
        if(c == '"'){
            context.add(c);
            if(isFirstDoubleQuote){
                isFirstDoubleQuote = false;
            }else {
                context.generateToken(TokenType.LIT_STRING);
            }
        }else {
            context.add(c);
        }
    }

    @Override
    public void finalize(LexerContext lexer) {

    }
}
