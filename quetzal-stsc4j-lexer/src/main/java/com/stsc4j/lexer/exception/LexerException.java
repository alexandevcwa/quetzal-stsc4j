package com.stsc4j.lexer.exception;

/**
 * Clase que representa una excepción específica para errores encontrados durante
 * la etapa de análisis léxico (tokenización) en un intérprete o compilador.
 */
public class LexerException extends RuntimeException {
    public LexerException(String tipo, String message) {
        super("\n[LXE] :: " + String.format("[%s] ::", tipo.toUpperCase()) + message);
    }
}
