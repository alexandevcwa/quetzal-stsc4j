package com.stsc4j.generator.bytecode;

import com.stsc4j.generator.BytecodeAbstractGenerator;
import com.stsc4j.generator.BytecodeGenerator;
import com.stsc4j.parser.v1.ast.ExpressionJsnBlock;
import com.stsc4j.parser.v1.ast.ExpressionJsn;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class BytecodeExpressionJsnBlock extends BytecodeAbstractGenerator {

    private final BytecodeGenerator generator;

    public BytecodeExpressionJsnBlock(BytecodeGenerator generator) {
        this.generator = generator;
    }

    @Override
    public String visit(ExpressionJsnBlock expr) {
        MethodVisitor mv = generator.getMv();

        // 1. Crear la instancia de LinkedHashMap
        mv.visitTypeInsn(Opcodes.NEW, "java/util/LinkedHashMap");
        mv.visitInsn(Opcodes.DUP);
        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/util/LinkedHashMap", "<init>", "()V", false);

        // 2. Llenar el HashMap con todas las propiedades del bloque
        if (expr.expressions != null) {
            for (ExpressionJsn prop : expr.expressions) {
                // Duplicamos la referencia del mapa en la pila ANTES de evaluar la propiedad,
                // porque el 'put' consume la referencia. Así aseguramos que siempre haya un mapa para la siguiente iteración.
                mv.visitInsn(Opcodes.DUP);
                prop.accept(generator);
            }
        }

        // Al terminar, en la cima de la pila quedará la referencia al HashMap ya lleno.
        return "JSN_OBJECT";
    }
}