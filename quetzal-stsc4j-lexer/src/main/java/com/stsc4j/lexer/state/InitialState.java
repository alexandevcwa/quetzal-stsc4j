package com.stsc4j.lexer.state;

import com.stsc4j.lexer.LexerContext;
import com.stsc4j.lexer.TokenType;

public class InitialState implements LexerState {

    @Override
    public void process(char c, int length, int index, LexerContext lexer) {

        // Analyze whitespace
        if (Character.isWhitespace(c)) {
            return;
        }

        // Analyze special characters like symbols
        if (!Character.isLetterOrDigit(c) && '_' != c && '"' != c && '.' != c) {
            LexerState s = new SymbolState();
            lexer.setState(s);
            s.process(c, length, index, lexer);
            return;
        }

        // Analyze letters
        if (Character.isLetter(c)) {
            LexerState s = new LetterState();
            lexer.setState(s);
            s.process(c, length, index, lexer);
            return;
        }

        // Analyze String Literals
        if(c == '"'){
            LexerState s = new StringLiteralState();
            lexer.setState(s);
            s.process(c, length, index, lexer);
            return;
        }

        // Analyze Numeric Literals
        if(Character.isDigit(c)){
            LexerState s = new DigitLiteralState();
            lexer.setState(s);
            s.process(c, length, index, lexer);
            return;
        }

        lexer.generateToken(TokenType.UNKNOW);
        String error = "Unexpected character '" + c + "'";
        throw new IllegalStateException(error);
    }

    @Override
    public void finalize(LexerContext lexer) {

    }
}
