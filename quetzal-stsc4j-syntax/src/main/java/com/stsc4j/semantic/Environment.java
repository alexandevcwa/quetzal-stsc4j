package com.stsc4j.semantic;

import java.util.HashMap;
import java.util.Map;

public class Environment {
    // Puntero al entorno padre (para manejar variables globales vs locales)
    private final Environment enclosing;

    //Detalles de cada variable

    public static class VariableInfo{
        public String Type;
        public boolean isMutable;

        VariableInfo(String type, boolean isMutable){
            this.Type = type;
            this.isMutable = isMutable;
        }
    }

    // Tabla hash rápida O(1) para: NombreVariable -> TipoDato
    private final Map<String, VariableInfo> values = new HashMap<>();


    // --- NUEVO: SOPORTE PARA FUNCIONES ---
    public static class FunctionInfo {
        public String returnType;
        public java.util.List<String> paramTypes;

        FunctionInfo(String returnType, java.util.List<String> paramTypes) {
            this.returnType = returnType;
            this.paramTypes = paramTypes;
        }
    }

    // Tabla hash para: NombreFuncion -> DetallesDeLaFuncion
    private final Map<String, FunctionInfo> functions = new HashMap<>();

    public void defineFunction(String name, String returnType, java.util.List<String> paramTypes) {
        if (isFunctionDeclared(name)) {
            throw new SemanticError("Error Semántico: La función '" + name + "' ya está declarada.");
        }
        functions.put(name, new FunctionInfo(returnType, paramTypes));
    }

    private boolean isFunctionDeclared(String name) {
        if (functions.containsKey(name)) return true;
        if (enclosing != null) return enclosing.isFunctionDeclared(name);
        return false;
    }

    public FunctionInfo resolveFunction(String name) {
        if (functions.containsKey(name)) return functions.get(name);
        if (enclosing != null) return enclosing.resolveFunction(name);
        throw new SemanticError("Error Semántico: La función '" + name + "' no existe o no ha sido declarada.");
    }
    // --- FIN NUEVO ---


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
    public void define(String name, String type, boolean isMutable) {
        // Usamos nuestro nuevo metodo recursivo para revisar toda la cadena
        if (isDeclared(name)) {
            throw new SemanticError("La variable '" + name + "' ya está declarada en este ámbito o en uno superior.");
        }
        values.put(name, new VariableInfo(type, isMutable));
    }

    /**
     * Metodo auxiliar que busca si la variable ya existe en la caja actual
     * o en cualquier caja padre. Devuelve true si la encuentra.
     */
    private boolean isDeclared(String name) {
        // Revisamos la caja actual
        if (values.containsKey(name)) {
            return true;
        }
        // Si no está, pero tenemos un padre, le preguntamos al padre
        if (enclosing != null) {
            return enclosing.isDeclared(name);
        }
        // Si llegamos hasta arriba y no está, entonces no existe
        return false;
    }

    /**
     * Busca el tipo de una variable subiendo por la cadena de entornos.
     */
    public String resolveType(String name) {
        if (values.containsKey(name)) {
            return values.get(name).Type;
        }
        if (enclosing != null) {
            return enclosing.resolveType(name);
        }
        throw new SemanticError("La variable '" + name + "' no ha sido definida.");
    }

    /**
     * REASIGNAR: Intenta cambiar el valor de una variable ya existente.
     * ¡Aquí está la magia de la inmutabilidad!
     */
    public void assign(String name, String newType) {
        if (values.containsKey(name)) {
            VariableInfo info = values.get(name);

            // REGLA 1: Verificar si es constante
            if (!info.isMutable) {
                throw new SemanticError("Error de inmutabilidad: No puedes reasignar un valor a '" + name + "' porque es una constante.");
            }

            // REGLA 2: Verificar que no le cambien el tipo de dato (ej. entero a cadena)
            if (newType != null && !newType.equals("null") && !newType.equals("nulo") && !info.Type.equals(newType)) {
                throw new SemanticError("Conflicto de tipos: La variable '" + name + "' es de tipo '" + info.Type + "', no puedes asignarle un '" + newType + "'.");
            }

            return; //si es correcto, se permite la reasignación
        }

        // Si no está aquí buscamos en el entorno padre
        if (enclosing != null) {
            enclosing.assign(name, newType);
            return;
        }

        throw new SemanticError("La variable '" + name + "' no ha sido definida.");
    }

    public VariableInfo getVariable(String name) {
        if (values.containsKey(name)) {
            return values.get(name);
        }
        if (enclosing != null) {
            return enclosing.getVariable(name);
        }
        throw new SemanticError("Error Semántico: La variable '" + name + "' no ha sido definida.");
    }

}