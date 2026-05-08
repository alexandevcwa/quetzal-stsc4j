package com.stsc4j.generator;

import java.util.HashMap;
import java.util.Map;

public class EnvironmentJVM {
    // Diccionario para recordar: Nombre de variable en Quetzal -> Índice en la JVM
    private final Map<String, Integer> variables = new HashMap<>();

    // Empezamos en 1 porque el índice 0 en public static void main(String[] args) lo ocupa 'args'
    private int proximoIndiceLibre = 1;

    // Registra una nueva variable y le asigna un "casillero" numérico
    public void registrarVariable(String nombre) {
        if (!variables.containsKey(nombre)) {
            variables.put(nombre, proximoIndiceLibre);
            proximoIndiceLibre++;
        }
    }

    // Devuelve el número de casillero donde se guardó la variable
    public int obtenerIndice(String nombre) {
        if (!variables.containsKey(nombre)) {
            throw new RuntimeException("Error en Generador: La variable '" + nombre + "' no existe en memoria.");
        }
        return variables.get(nombre);
    }
}