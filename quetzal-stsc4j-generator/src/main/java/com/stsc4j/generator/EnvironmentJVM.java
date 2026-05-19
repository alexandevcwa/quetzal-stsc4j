package com.stsc4j.generator;

import java.util.HashMap;
import java.util.Map;

public class EnvironmentJVM {
    // Diccionario para recordar: Nombre de variable en Quetzal -> Índice en la JVM
    private final Map<String, Integer> variables = new HashMap<>();

    private int proximoIndiceLibre;

    // ¡NUEVO! Constructor flexible para funciones estáticas (empezarán en 0)
    public EnvironmentJVM(int indiceInicial) {
        this.proximoIndiceLibre = indiceInicial;
    }

    // Constructor por defecto (mantiene el 1 para el main con sus 'args')
    public EnvironmentJVM() {
        this.proximoIndiceLibre = 1;
    }

    // Registra una nueva variable y le asigna un "casillero" numérico
    public void registrarVariable(String nombre) { // Nota: en tu clase original se llamaba registrarVariable o registrar, asegúrate de que el nombre coincida con lo que usas
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