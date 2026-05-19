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

        // 1. OBJETOS Y ARREGLOS (ALOAD)
        if (TokenType.PRIMITIVE_STRING.name().equals(tipo) || (tipo != null && tipo.startsWith("["))) {
            mv.visitVarInsn(Opcodes.ALOAD, indiceMemoria);
        }
        // 2. DECIMALES (FLOAD)
        else if (TokenType.PRIMITIVE_DECIMAL.name().equals(tipo)) {
            mv.visitVarInsn(Opcodes.FLOAD, indiceMemoria);
        }
        // 3. ENTEROS Y BOOLEANOS (ILOAD)
        else {
            mv.visitVarInsn(Opcodes.ILOAD, indiceMemoria);
        }

        return tipo;
    }
}