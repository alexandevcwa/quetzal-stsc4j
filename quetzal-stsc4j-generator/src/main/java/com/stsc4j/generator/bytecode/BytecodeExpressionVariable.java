package com.stsc4j.generator.bytecode;

import com.stsc4j.generator.BytecodeAbstractGenerator;
import com.stsc4j.generator.BytecodeGenerator;
import com.stsc4j.lexer.TokenType;
import com.stsc4j.parser.v1.ast.ExpressionVariable;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class BytecodeExpressionVariable extends BytecodeAbstractGenerator {

    private final BytecodeGenerator generator;

    public BytecodeExpressionVariable(BytecodeGenerator generator) {
        this.generator = generator;
    }

    @Override
    public String visit(ExpressionVariable expr) {
        MethodVisitor mv = generator.getMv();
        String nombreVar = expr.token.getLexeme();

        int indiceMemoria = generator.getEnvJVM().obtenerIndice(nombreVar);
        String tipo = generator.getTiposVariables().get(nombreVar);

        // 1. ENTEROS Y BOOLEANOS (ILOAD)
        if (TokenType.PRIMITIVE_INTEGER.name().equals(tipo) || TokenType.PRIMITIVE_BOOLEAN.name().equals(tipo)) {
            mv.visitVarInsn(Opcodes.ILOAD, indiceMemoria);
        }
        // 2. DECIMALES (FLOAD)
        else if (TokenType.PRIMITIVE_DECIMAL.name().equals(tipo)) {
            mv.visitVarInsn(Opcodes.FLOAD, indiceMemoria);
        }
        // 3. OBJETOS, ARREGLOS Y TEXTOS (ALOAD)
        else {
            // "JSN_OBJECT", textos y listas ("["...) entran aquí perfectamente.
            mv.visitVarInsn(Opcodes.ALOAD, indiceMemoria);
        }

        return tipo;
    }
}