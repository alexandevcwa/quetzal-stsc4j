package com.stsc4j.semantic;

import java.util.HashMap;
import java.util.Map;

public class Environment {
    // Puntero al entorno padre (para manejar variables globales vs locales)
    private final Environment enclosing;

    // Tabla hash rápida O(1) para: NombreVariable -> TipoDato
    private final Map<String, String> values = new HashMap<>();

    // Constructor para el entorno global
    public Environment() {
        this.enclosing = null;
    }

    // Constructor para entornos locales (dentro de un bloque, if, etc.)
    public Environment(Environment enclosing) {
        this.enclosing = enclosing;
    }

    /**
     * Declara una nueva variable. Falla si ya existe en este mismo nivel.
     */
    public void define(String name, String type) {
        if (values.containsKey(name)) {
            throw new SemanticError("La variable '" + name + "' ya está declarada en este ámbito.");
        }
        values.put(name, type);
    }

    /**
     * Busca el tipo de una variable subiendo por la cadena de entornos.
     */
    public String resolveType(String name) {
        if (values.containsKey(name)) {
            return values.get(name);
        }
        if (enclosing != null) {
            return enclosing.resolveType(name);
        }
        throw new SemanticError("La variable '" + name + "' no ha sido definida.");
    }
}