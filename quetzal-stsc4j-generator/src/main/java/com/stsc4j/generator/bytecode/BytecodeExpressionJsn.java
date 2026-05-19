package com.stsc4j.generator.bytecode;

import com.stsc4j.generator.BytecodeAbstractGenerator;
import com.stsc4j.generator.BytecodeGenerator;
import com.stsc4j.lexer.TokenType;
import com.stsc4j.parser.v1.ast.ExpressionJsn;
import com.stsc4j.parser.v1.ast.Expression;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class BytecodeExpressionJsn extends BytecodeAbstractGenerator {

    private final BytecodeGenerator generator;

    public BytecodeExpressionJsn(BytecodeGenerator generator) {
        this.generator = generator;
    }

    @Override
    public String visit(ExpressionJsn expr) {
        MethodVisitor mv = generator.getMv();

        // 1. Empujar la CLAVE (ej: "nombre" o "calificaciones")
        String clave = expr.key.getLexeme().replace("\"", "");
        mv.visitLdcInsn(clave);

        // ==========================================
        // CASO A: Es un valor simple (expr.value)
        // ==========================================
        if (expr.value != null) {
            String tipoValor = expr.value.accept(generator);
            empaquetarPrimitivo(mv, tipoValor);
        }
        // ==========================================
        // CASO B: Es una lista de valores (expr.values)
        // ==========================================
        else if (expr.values != null) {
            // Instanciar un ArrayList de Java
            mv.visitTypeInsn(Opcodes.NEW, "java/util/ArrayList");
            mv.visitInsn(Opcodes.DUP);
            mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/util/ArrayList", "<init>", "()V", false);

            // Recorrer la lista de expresiones del AST y meterlas al ArrayList
            for (Expression item : expr.values) {
                mv.visitInsn(Opcodes.DUP); // Duplicar referencia del ArrayList para el 'add'

                String tipoItem = item.accept(generator); // Evaluar el elemento
                empaquetarPrimitivo(mv, tipoItem);        // Hacer Boxing si es necesario

                // Llamar a ArrayList.add(Object) -> devuelve boolean
                mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/util/ArrayList", "add", "(Ljava/lang/Object;)Z", false);
                mv.visitInsn(Opcodes.POP); // Tirar a la basura el boolean que devuelve el 'add'
            }
            // Al terminar el bucle, la referencia al ArrayList lleno queda en la cima de la pila,
            // lista para ser guardada como 'valor' en el HashMap.
        }

        // 3. Guardar en el HashMap: map.put(clave, valor_o_arraylist)
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/util/LinkedHashMap", "put",
                "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", false);

        // El 'put' devuelve el valor anterior (o null), lo sacamos de la pila
        mv.visitInsn(Opcodes.POP);

        return null;
    }

    /**
     * auxiliar para hacer "Boxing".
     * Los HashMaps y ArrayLists de Java no aceptan primitivos (int, float, boolean).
     * Deben ser convertidos a sus clases Envoltorio (Integer, Float, Boolean).
     */
    private void empaquetarPrimitivo(MethodVisitor mv, String tipoValor) {
        if (TokenType.PRIMITIVE_INTEGER.name().equals(tipoValor)) {
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;", false);
        } else if (TokenType.PRIMITIVE_DECIMAL.name().equals(tipoValor)) {
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Float", "valueOf", "(F)Ljava/lang/Float;", false);
        } else if (TokenType.PRIMITIVE_BOOLEAN.name().equals(tipoValor)) {
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Boolean", "valueOf", "(Z)Ljava/lang/Boolean;", false);
        }
    }
}