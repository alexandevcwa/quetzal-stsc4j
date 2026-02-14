package com.stsc4j.parser.declaration;

import com.stsc4j.parser.ast.Expression;
import com.stsc4j.parser.ast.Statement;
import com.stsc4j.parser.ast.TypeInfo;
import com.stsc4j.parser.ast.Visitor;

/**
 * Clase que representa la declaración de una variable.
 */
public class VarDeclaration extends Statement {

    /**
     * Tipo de la variable.
     */
    final TypeInfo type;

    /**
     * Indica si la variable es mutable o no.
     */
    final boolean isMutable;

    /**
     * Nombre de la variable.
     */
    final String name;

    /**
     * Inicializador de la variable.
     */
    final Expression initializer;

    /**
     * Constructor para la declaración de una variable.
     *
     * @param type        El tipo de la variable, representado como un objeto de TypeInfo.
     * @param isMutable   Indica si la variable es mutable (true) o inmutable (false).
     * @param name        El nombre de la variable.
     * @param initializer La expresión que inicializa la variable.
     */
    public VarDeclaration(TypeInfo type, boolean isMutable, String name, Expression initializer) {
        this.type = type;
        this.isMutable = isMutable;
        this.name = name;
        this.initializer = initializer;
    }

    public TypeInfo getType() {
        return type;
    }

    public boolean isMutable() {
        return isMutable;
    }

    public String getName() {
        return name;
    }

    public Expression getInitializer() {
        return initializer;
    }

    @Override
    protected <R> R accept(Visitor<R> visitor) {
        return visitor.visit(this);
    }
}
