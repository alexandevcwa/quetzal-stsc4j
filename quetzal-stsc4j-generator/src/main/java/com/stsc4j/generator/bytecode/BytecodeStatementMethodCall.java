package com.stsc4j.generator.bytecode;

import com.stsc4j.generator.BytecodeAbstractGenerator;
import com.stsc4j.generator.BytecodeGenerator;
import com.stsc4j.parser.v1.ast.StatementMethodCall;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class BytecodeStatementMethodCall extends BytecodeAbstractGenerator {

    private final BytecodeGenerator generator;

    public BytecodeStatementMethodCall(BytecodeGenerator generator) {
        this.generator = generator;
    }

    @Override
    public String visit(StatementMethodCall stmt) {
        MethodVisitor mv = generator.getMv();

        // 1. Generamos el bytecode de la llamada al método
        // Esto automáticamente buscará la función e inyectará el INVOKESTATIC
        String tipoRetorno = stmt.methodCall.accept(generator);

        // 2. LA LIMPIEZA (POP)
        // Si retornó algo a la pila, pero no lo estamos guardando en ninguna variable,
        // debemos sacarlo de la pila inmediatamente para no corromper la memoria de la JVM.
        if (tipoRetorno != null && !tipoRetorno.equals("VOID")) {
            // Si el tipo es un decimal doble o un entero largo (ocupan 2 espacios de memoria), se usa POP2.
            // Para lo demás (enteros, booleanos, strings, arreglos), se usa POP.
            mv.visitInsn(Opcodes.POP);
        }

        return null;
    }
}