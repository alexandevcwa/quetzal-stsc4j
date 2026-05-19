package com.stsc4j.generator.bytecode;

import com.stsc4j.generator.BytecodeAbstractGenerator;
import com.stsc4j.generator.BytecodeGenerator;
import com.stsc4j.lexer.TokenType;
import com.stsc4j.parser.v1.ast.ExpressionIncDec;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class BytecodeExpressionIncDec extends BytecodeAbstractGenerator {

    private final BytecodeGenerator generator;

    public BytecodeExpressionIncDec(BytecodeGenerator generator) {
        this.generator = generator;
    }

    @Override
    public String visit(ExpressionIncDec expr) {
        MethodVisitor mv = generator.getMv();

        // 1. Extraemos el nombre de la variable navegando dentro de ExpressionVariable
        String nombreVar = expr.identifier.token.getLexeme();

        // 2. Revisamos el primer signo del arreglo para saber si es suma o resta
        boolean esIncremento = expr.operator[0].getLexeme().equals("+");

        // 3. Buscamos la variable en la memoria de la JVM
        int indiceMemoria = generator.getEnvJVM().obtenerIndice(nombreVar);
        String tipo = generator.getTiposVariables().get(nombreVar);

        if (TokenType.PRIMITIVE_DECIMAL.name().equals(tipo)) {
            // FLOTANTES: Sacamos el valor, empujamos 1.0, operamos y guardamos (no existe IINC para floats)
            mv.visitVarInsn(Opcodes.FLOAD, indiceMemoria);
            mv.visitInsn(Opcodes.FCONST_1);
            mv.visitInsn(esIncremento ? Opcodes.FADD : Opcodes.FSUB);
            mv.visitVarInsn(Opcodes.FSTORE, indiceMemoria);
            return TokenType.PRIMITIVE_DECIMAL.name();
        } else {
            // ENTEROS: ¡Súper optimización con IINC en un solo paso!
            int valorAumento = esIncremento ? 1 : -1;
            mv.visitIincInsn(indiceMemoria, valorAumento);
            return TokenType.PRIMITIVE_INTEGER.name();
        }
    }
}