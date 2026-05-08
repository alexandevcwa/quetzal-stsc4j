package com.stsc4j.lexer.state;

import com.stsc4j.lexer.LexerContext;
import com.stsc4j.lexer.TokenType;

public class InitialState implements LexerState {

    // Entro un =
    @Override
    public void process(char c, int length, int index, LexerContext context) {

        // Analyze whitespace
        if (Character.isWhitespace(c) || c == '\n') {
            if(c == '\n'){
                context.oneMoreLine();
            }
            return;
        }
        // Analyze special characters like symbols
        // EL igual = puede ser procesado
        if ((!Character.isLetterOrDigit(c) && '_' != c && '"' != c) || c == 'y' || c == 'o') {
            // Cambio de estado mi automata para que ese estado determine que tipo de token es
            LexerState s = new SymbolState();
            // Asignar nuevo estado al contexto
            context.setState(s);
            // Reprocesar el mismo caracter actual
            // Entro el =
            s.process(c, length, index, context);
            return;
        }

        // Analyze letters
        if (Character.isLetter(c)) {
            LexerState s = new LetterState();
            context.setState(s);
            s.process(c, length, index, context);
            return;
        }

        // Analyze String Literals
        if (c == '"') {
            LexerState s = new StringLiteralState();
            context.setState(s);
            s.process(c, length, index, context);
            return;
        }

        // Analyze Numeric Literals
        if (Character.isDigit(c)) {
            LexerState s = new DigitLiteralState();
            context.setState(s);
            s.process(c, length, index, context);
            return;
        }

        context.generateToken(TokenType.UNKNOW);
    }

    @Override
    public void finalize(LexerContext lexer) {

    }
}
