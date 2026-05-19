package com.stsc4j.generator.bytecode;

import com.stsc4j.generator.BytecodeAbstractGenerator;
import com.stsc4j.generator.BytecodeGenerator;
import com.stsc4j.parser.v1.ast.Statement;
import com.stsc4j.parser.v1.ast.StatementBlock;

public class BytecodeStatementBlock extends BytecodeAbstractGenerator {

    private final BytecodeGenerator generator;

    public BytecodeStatementBlock(BytecodeGenerator generator) {
        this.generator = generator;
    }

    @Override
    public String visit(StatementBlock stmt) {
        // Un bloque solo es una lista de instrucciones, así que las despachamos una por una
        if (stmt.statements != null) {
            for (Statement instruccion : stmt.statements) {
                instruccion.accept(generator);
            }
        }
        return null;
    }
}