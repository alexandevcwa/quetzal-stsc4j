package com.stsc4j.parser.ast;

public class TypeInfo {
    /**
     * Data type or reserved word like (texto, número, lista)
     */
    String name;

    /**
     * For lista<entero | número | texto>
     */
    TypeInfo geneticType;

    public TypeInfo(String name, TypeInfo geneticType) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name + (geneticType != null ? "<" + geneticType + ">" : "");
    }
}
