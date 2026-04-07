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

    /**
     * Obtiene el token actual sin avanzar en el flujo de tokens.
     *
     * @return El token actual en el flujo.
     */
    public Token show() {
        return tokens.get(current);
    }

    /**
     * Obtiene el siguiente token en el flujo de tokens sin consumirlo.
     * Si el token actual es el fin del archivo (EOF), retorna el token actual.
     *
     * @return El siguiente token en el flujo si no es EOF, o el token actual si es EOF.
     */
    public Token futureShow() {
        if (matchButNotAdvance(TokenType.EOF)) {
            return show();
        }
        return tokens.get(current + 1);
    }

    /**
     * Obtener el token anterior al actual. Útil para obtener el token que se acaba de consumir.
     *
     * @return Token anterior
     */
    public Token before() {
        return tokens.get(current - 1);
    }

    /**
     * Verificar si el token actual hace match con algún tipo y avanza una posición.
     *
     * @param t Tipos de tokens que puede ser el actual
     * @return True = el token actual hace match con alguno de los tipos, False = el token actual no hace match con
     * ninguno de los tipos
     */
    public boolean match(TokenType... t) {
        for (TokenType tt : t) {
            if (show().getType() == tt) {
                advance();
                return true;
            }
        }
        return false;
    }

    /**
     * Verifica si el token actual coincide con el tipo esperado y avanza el flujo de tokens.
     * Si el token no coincide, lanza una excepción con el mensaje proporcionado.
     *
     * @param t       Tipo de token esperado.
     * @param message Mensaje de error que será lanzado si no hay coincidencia.
     */
    public void match(TokenType t, String message) {
        boolean match = match(t);
        if (!match) throw new RuntimeException(message);
    }

    /**
     * Verifica si el token actual coincide con el tipo esperado y avanza el flujo de tokens.
     * Si el token no coincide, lanza una excepción del tipo proporcionado.
     *
     * @param <T>       Clase del tipo de excepción que extiende de {@link RuntimeException}.
     * @param t         Tipo de token esperado.
     * @param exception Excepción que se lanzará si el tipo del token actual no coincide con el tipo esperado.
     */
    public <T extends RuntimeException> void match(TokenType t, T exception) {
        boolean match = match(t);
        if (!match) throw exception;
    }

    /**
     * Verificar si el token actual no hace match con ningún tipo y avanza una posición.
     *
     * @param t Tipos de tokens que puede no ser el actual
     * @return True = el token actual no hace match con alguno de los tipos, False = el token actual hace match con
     * alguno de los tipos
     */
    public boolean notMatch(TokenType... t) {
        for (TokenType tt : t) {
            if (show().getType() == tt) {
                advance();
                return false;
            }
        }
        return true;
    }

    /**
     * Verificar si el token actual hace match con algún tipo sin consumirlo.
     *
     * @param t Tipos de tokens que puede ser el actual
     * @return True = to token actual hace match con alguno de los tipos, False = el token actual no hace match con
     * ninguno de los tipos
     */
    public boolean matchButNotAdvance(TokenType... t) {
        for (TokenType tt : t) {
            if (show().getType() == tt) {
                return true;
            }
        }
        return false;
    }

    /**
     * Verifica si el token actual coincide con alguno de los tipos de tokens especificados y retrocede una posición en
     * el flujo de tokens si hay coincidencia.
     *
     * @param t Lista de tipos de {@code TokenType} con los que el token actual puede coincidir.
     * @return {@code true} si el token actual coincide con alguno de los tipos especificados y retrocede en el flujo,
     * {@code false} en caso contrario.
     */
    public boolean matchAndBack(TokenType... t) {
        for (TokenType tt : t) {
            if (show().getType() == tt) {
                back();
                return true;
            }
        }
        return false;
    }

    /**
     * Verifica si el token actual hace match con algún tipo sin consumirlo.
     *
     * @param t Lista de tipos de tokens
     * @return True = el token actual hace match con alguno de los tipos, False = el token actual no hace match con
     * ninguno de los tipos
     */
    public boolean currentEquals(TokenType... t) {
        for (TokenType tt : t) {
            if (show().getType() == tt) {
                return true;
            }
        }
        return false;
    }

    /**
     * Consume un token y verifica si el esperado es el correcto. Si no lo es, lanza una excepción con el mensaje
     * proporcionado.
     *
     * @param type    Tipo de token esperado
     * @param message Mensaje de error
     * @return Token esperado
     */
    public Token consume(TokenType type, String message) {
        if (show().getType() == type) return advance();
        throw new RuntimeException(message);
    }

    /**
     * Consume un token del flujo si el tipo coincide con el tipo esperado. Si no coincide, lanza una excepción.
     *
     * @param <T>       Clase del tipo de excepción que extiende de RuntimeException y será lanzada en caso de error.
     * @param type      Tipo de token esperado.
     * @param exception Excepción a lanzar si el tipo del token actual no coincide con el tipo esperado.
     * @return El token consumido si el tipo coincide con el tipo esperado.
     */
    public <T extends RuntimeException> Token consume(TokenType type, T exception) {
        if (show().getType() == type) {
            return advance();
        } else {
            throw exception;
        }
    }

    /**
     * Consume un token y verifica si el esperado es el correcto. Si no lo es, lanza una excepción con el mensaje
     * proporcionado. No devuelve el token esperado.
     *
     * @param type    Tipo de token esperado
     * @param message Mensaje de error
     */
    public void consumeAsVoid(TokenType type, String message) {
        consume(type, message);
    }

    /**
     * Consume un token del flujo si el tipo coincide con el tipo esperado. Si no coincide, lanza una excepción.
     * Esta operación no retorna el token consumido, solo verifica y consume.
     *
     * @param type      Tipo de token esperado.
     * @param exception Excepción a lanzar si el tipo del token actual no coincide con el tipo esperado.
     */
    public void consumeAsVoid(TokenType type, RuntimeException exception) {
        consume(type, exception);
    }

    /**
     * Avanza al siguiente token y devuelve el token actual.
     *
     * @return Token actual
     */
    public Token advance() {
        if (!isAtEnd()) current++;
        return tokens.get(current - 1);
    }

    public Token back() {
        if (current > 0) current--;
        return tokens.get(current);
    }

    /**
     * Verifica si se ha llegado al final de la lista de tokens.
     *
     * @return True = Es final, False = No es el final
     */
    public boolean isAtEnd() {
        return tokens.get(current).getType() == TokenType.EOF;
    }

}
