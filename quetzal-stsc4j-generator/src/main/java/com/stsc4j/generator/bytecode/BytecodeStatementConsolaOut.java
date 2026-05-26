package com.stsc4j.generator.bytecode;

import com.stsc4j.generator.BytecodeAbstractGenerator;
import com.stsc4j.generator.BytecodeGenerator;
import com.stsc4j.parser.v1.ast.StatementConsolaOut;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class BytecodeStatementConsolaOut extends BytecodeAbstractGenerator {

    private final BytecodeGenerator generator;

    public BytecodeStatementConsolaOut(BytecodeGenerator generator) {
        this.generator = generator;
    }

    @Override
    public String visit(StatementConsolaOut stmt) {
        MethodVisitor mv = generator.getMv();

        // 1. Cargar System.out en la pila
        mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");

        // 2. Generar la expresión (esto pone el valor real en la pila)
        String tipoResultado = null;
        if (stmt.expression != null) {
            tipoResultado = stmt.expression.accept(generator);
        }

        // 3. Fallback Seguro
        if (tipoResultado == null) {
            tipoResultado = "java/lang/Object";
        }

        // 4. Normalización limpia
        tipoResultado = tipoResultado.trim().toUpperCase();

        // 5. Llamada a println atrapando palabras de Quetzal Y Tokens Internos
        if (tipoResultado.contains("ENTERO") || tipoResultado.contains("INT")) {
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(I)V", false);
        }
        else if (tipoResultado.contains("NUMERO") || tipoResultado.contains("NÚMERO") || tipoResultado.contains("DECIMAL") || tipoResultado.contains("FLOAT")) {
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(F)V", false);
        }
        else if (tipoResultado.contains("LOG") || tipoResultado.contains("BOOL")) {
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Z)V", false);
        }
        else if (tipoResultado.contains("TEXTO") || tipoResultado.contains("STRING")) {
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
        }
        else {
            // JSN, Listas, o cualquier objeto complejo
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/Object;)V", false);
        }

        return null;
    }
}