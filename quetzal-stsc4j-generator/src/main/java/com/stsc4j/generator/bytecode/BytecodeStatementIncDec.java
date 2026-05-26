package com.stsc4j.generator.bytecode;

import com.stsc4j.generator.BytecodeAbstractGenerator;
import com.stsc4j.generator.BytecodeGenerator;
import com.stsc4j.parser.v1.ast.StatementIncDec;

public class BytecodeStatementIncDec extends BytecodeAbstractGenerator {

    private final BytecodeGenerator generator;

    public BytecodeStatementIncDec(BytecodeGenerator generator) {
        this.generator = generator;
    }

    @Override
    public String visit(StatementIncDec stmt) {
        // Solo delegamos el trabajo a la expresión interna
        if (stmt.expression != null) {
            stmt.expression.accept(generator);
        }
        return null;
    }
}