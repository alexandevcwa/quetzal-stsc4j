package com.stsc4j.parser.v1.parser;

import com.stsc4j.lexer.Token;
import com.stsc4j.lexer.TokenType;

import java.util.List;

public class TokenStream {
    final List<Token> tokens;
    private int current = 0;

    public TokenStream(List<Token> tokens) {
        this.tokens = tokens;
    }

    public Token show(){
        return tokens.get(current);
    }

    /**
     * Obtener el token anterior al actual. Útil para obtener el token que se acaba de consumir.
     * @return Token anterior
     */
    public Token before(){
        return tokens.get(current - 1);
    }

    /**
     * Verificar si el token actual hace match con algún tipo
     * @param t Tipos de tokens que puede ser el actual
     * @return True = el token actual hace match con alguno de los tipos, False = el token actual no hace match con ninguno de los tipos
     */
    public boolean match( TokenType... t){
        for(TokenType tt : t){
            if(show().getType() == tt){
                advance();
                return true;
            }
        }
        return false;
    }

    /**
     * Consume un token y verifica si el esperado es el correcto. Si no lo es, lanza una excepción con el mensaje proporcionado.
     * @param type Tipo de token esperado
     * @param message Mensaje de error
     * @return Token esperado
     */
    public Token consume(TokenType type, String message){
        if(show().getType() == type) return advance();
        throw new RuntimeException(message);
    }

    /**
     * Avanza al siguiente token y devuelve el token actual.
     * @return Token actual
     */
    public Token advance(){
        if(!isAtEnd()) current++;
        return tokens.get(current - 1);
    }

    /**
     * Verifica si se ha llegado al final de la lista de tokens.
     * @return True = Es final, False = No es el final
     */
    public boolean isAtEnd(){
        return tokens.get(current).getType() == TokenType.EOF;
    }

}
