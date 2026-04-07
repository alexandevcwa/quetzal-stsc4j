package com.stsc4j.parser.v1.utils;

public final class Coalesce {

    /**
     * Retorna el valor de 'value' si no es nulo, de lo contrario retorna 'defaultValue'.
     */
    public static <T> T compare(T value, T defaultValue) {
        return value != null ? value : defaultValue;
    }
}
