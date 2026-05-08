package com.stsc4j.lexer.state;

import com.stsc4j.lexer.LexerContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("SymbolState - Tests")
class SymbolStateTest {



    @Test
    void testSimbolosArroba(){
        LexerContext c = new LexerContext();
        assertThrows(Exception.class, () -> c.process("@"));
    }

}