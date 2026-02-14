package com.stsc4j.parser.ast;

public class TypeInfo {

    private final String name;
    private final TypeInfo genericType;

    public TypeInfo(String name, String genericTypeName) {
        this.name = name;
        this.genericType = genericTypeName != null ? new TypeInfo(genericTypeName, (String) null) : null;
    }

    public TypeInfo(String name, TypeInfo genericType) {
        this.name = name;
        this.genericType = genericType;
    }

    public String getName() {
        return name;
    }

    public TypeInfo getGenericType() {
        return genericType;
    }

    @Override
    public String toString() {
        return name + (genericType != null ? "<" + genericType + ">" : "");
    }
}
