package com.stsc4j.lexer.state;

import com.stsc4j.lexer.LexerContext;

public interface LexerState {

    void process(char c, LexerContext lexer);

    void finalize(LexerContext lexer);

}
