package com.stsc4j.parser.ast;

public class TypeInfo {
    /**
     * Tipo de dato primitivo o estructuras como listas
     */
    String name;

    /**
     * Tipo de dato genético, para tipos genéricos como List<T>, el geneticType sería T
     */
    TypeInfo genericType;

    public TypeInfo(String name, TypeInfo geneticType) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name + (genericType != null ? "<" + genericType + ">" : "");
    }
}
