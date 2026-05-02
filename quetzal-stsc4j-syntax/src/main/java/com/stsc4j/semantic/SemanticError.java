package com.stsc4j.semantic;

public class SemanticError extends RuntimeException {
    public SemanticError(String message) {
        super("Error Semántico: " + message);
    }
}