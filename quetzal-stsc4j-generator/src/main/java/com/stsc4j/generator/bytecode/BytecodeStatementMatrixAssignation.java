package com.stsc4j.generator.bytecode;

import com.stsc4j.generator.BytecodeAbstractGenerator;
import com.stsc4j.generator.BytecodeGenerator;
import com.stsc4j.parser.v1.ast.StatementMatrixAssignation;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class BytecodeStatementMatrixAssignation extends BytecodeAbstractGenerator {

    private final BytecodeGenerator generator;

    public BytecodeStatementMatrixAssignation(BytecodeGenerator generator) {
        this.generator = generator;
    }

    @Override
    public String visit(StatementMatrixAssignation stmt) {
        MethodVisitor mv = generator.getMv();

        // 1. Cargar la REFERENCIA del arreglo (Ej: empuja 'numeros')
        String tipoArray = stmt.matrix.objectList.accept(generator);

        // 2. Cargar el ÍNDICE (Ej: empuja el 0)
        // Tomamos el primer índice (si tuvieras matrices 2D, aquí haríamos un bucle)
        stmt.matrix.index.get(0).accept(generator);

        // 3. Cargar el NUEVO VALOR que queremos asignar (Ej: empuja el 5)
        stmt.expression.accept(generator);

        // 4. GUARDAR el valor en la memoria (Array Store)
        if (tipoArray != null) {
            if (tipoArray.startsWith("[I")) {
                mv.visitInsn(Opcodes.IASTORE); // Para enteros
            } else if (tipoArray.startsWith("[F")) {
                mv.visitInsn(Opcodes.FASTORE); // Para decimales
            } else if (tipoArray.startsWith("[Z")) {
                mv.visitInsn(Opcodes.BASTORE); // Para booleanos
            } else {
                mv.visitInsn(Opcodes.AASTORE); // Para textos u objetos (JSN)
            }
        }

        return null; // Como es un Statement, ya limpió la pila y no retorna nada
    }
}