package com.stsc4j.generator;

import java.util.*;

public class EnvironmentJVM {

    // Stack de scopes
    private final Deque<Map<String, Integer>> scopes = new ArrayDeque<>();

    // Próximo slot libre JVM
    private int proximoIndiceLibre;

    // =========================================================
    // CONSTRUCTORES
    // =========================================================

    public EnvironmentJVM(int indiceInicial) {
        this.proximoIndiceLibre = indiceInicial;
        enterScope();
    }

    public EnvironmentJVM() {
        this.proximoIndiceLibre = 1; // main(String[] args)
        enterScope();
    }

    // =========================================================
    // SCOPES
    // =========================================================

    public void enterScope() {
        scopes.push(new HashMap<>());
    }

    public void exitScope() {
        if (!scopes.isEmpty()) {
            scopes.pop();
        }
    }

    // =========================================================
    // VARIABLES
    // =========================================================

    public void registrarVariable(String nombre) {

        Map<String, Integer> actual = scopes.peek();

        if (actual == null) {
            throw new RuntimeException("No existe scope activo.");
        }

        if (!actual.containsKey(nombre)) {

            actual.put(nombre, proximoIndiceLibre);

            proximoIndiceLibre++;
        }
    }

    public int obtenerIndice(String nombre) {

        for (Map<String, Integer> scope : scopes) {

            if (scope.containsKey(nombre)) {
                return scope.get(nombre);
            }
        }

        throw new RuntimeException(
                "Variable no encontrada: " + nombre
        );
    }
}