package com.stsc4j.generator.bytecode;

import com.stsc4j.generator.BytecodeAbstractGenerator;
import com.stsc4j.generator.BytecodeGenerator;
import com.stsc4j.lexer.TokenType;
import com.stsc4j.parser.v1.ast.StatementFunctionParameter;

public class BytecodeStatementFunctionParameter extends BytecodeAbstractGenerator {

    private final BytecodeGenerator generator;

    public BytecodeStatementFunctionParameter(BytecodeGenerator generator) {
        this.generator = generator;
    }

    @Override
    public String visit(StatementFunctionParameter stmt) {
        String nombreVar = stmt.identified.getLexeme();
        String tipoLexema = stmt.type.getLexeme();

        // 1. Registramos en la memoria (El Orquestador le asignará el índice correcto)
        generator.getEnvJVM().registrarVariable(nombreVar);

        // 2. Guardamos el tipo oficial para que BytecodeExpressionVariable sepa cómo leerlo
        String tipoInterno = mapearTipo(tipoLexema);
        generator.getTiposVariables().put(nombreVar, tipoInterno);

        //NO EMITIMOS BYTECODE: La JVM ya asignó el valor a las variables locales al invocar.
        return null;
    }

    private String mapearTipo(String tipo) {
        if (tipo.equals("texto")) return TokenType.PRIMITIVE_STRING.name();
        if (tipo.equals("entero")) return TokenType.PRIMITIVE_INTEGER.name();
        if (tipo.equals("numero") || tipo.equals("decimal")) return TokenType.PRIMITIVE_DECIMAL.name();
        if (tipo.equals("log") || tipo.equals("booleano")) return TokenType.PRIMITIVE_BOOLEAN.name();
        if (tipo.equals("jsn")) return "JSN_OBJECT";
        return TokenType.PRIMITIVE_INTEGER.name(); // Default seguro
    }
}