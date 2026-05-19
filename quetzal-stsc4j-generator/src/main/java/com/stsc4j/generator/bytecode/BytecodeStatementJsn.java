package com.stsc4j.generator.bytecode;

import com.stsc4j.generator.BytecodeAbstractGenerator;
import com.stsc4j.generator.BytecodeGenerator;
import com.stsc4j.parser.v1.ast.StatementJsn;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class BytecodeStatementJsn extends BytecodeAbstractGenerator {

    private final BytecodeGenerator generator;

    public BytecodeStatementJsn(BytecodeGenerator generator) {
        this.generator = generator;
    }

    @Override
    public String visit(StatementJsn stmt) {
        MethodVisitor mv = generator.getMv();
        String nombreVariable = stmt.identifier.getLexeme();

        // 1. Reservar espacio en la memoria
        generator.getEnvJVM().registrarVariable(nombreVariable);
        int indiceMemoria = generator.getEnvJVM().obtenerIndice(nombreVariable);

        // 2. Construir el HashMap lleno de datos (deja la referencia en la pila)
        stmt.block.accept(generator);

        // 3. Guardamos el tipo para que las demás operaciones sepan qué es
        generator.getTiposVariables().put(nombreVariable, "JSN_OBJECT");

        // 4. Guardamos la referencia en el casillero de memoria (ASTORE porque es un Objeto)
        mv.visitVarInsn(Opcodes.ASTORE, indiceMemoria);

        return null;
    }
}