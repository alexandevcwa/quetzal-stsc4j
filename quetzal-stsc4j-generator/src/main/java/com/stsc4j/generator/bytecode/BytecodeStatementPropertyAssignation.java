package com.stsc4j.generator.bytecode;

import com.stsc4j.generator.BytecodeAbstractGenerator;
import com.stsc4j.generator.BytecodeGenerator;
import com.stsc4j.lexer.TokenType;
import com.stsc4j.parser.v1.ast.StatementPropertyAssignation;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class BytecodeStatementPropertyAssignation extends BytecodeAbstractGenerator {

    private final BytecodeGenerator generator;

    public BytecodeStatementPropertyAssignation(BytecodeGenerator generator) {
        this.generator = generator;
    }

    @Override
    public String visit(StatementPropertyAssignation stmt) {
        MethodVisitor mv = generator.getMv();

        // 1. Desarmamos el nodo property para cargar el OBJETO (LinkedHashMap) en la pila
        stmt.property.object.accept(generator);

        // 2. Cargar el nombre de la CLAVE en la pila
        String clave = stmt.property.propertyName.getLexeme();
        mv.visitLdcInsn(clave);

        // 3. Evaluar el NUEVO VALOR y empujarlo a la pila
        String tipoValor = stmt.expression.accept(generator);

        // 4. BOXING: Envolver si es primitivo (igual que cuando creamos el JSN)
        empaquetarPrimitivo(mv, tipoValor);

        // 5. Guardar en el mapa: map.put(clave, valor)
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/util/LinkedHashMap", "put", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", false);

        // 6. Limpieza: el put deja el valor viejo en la pila, lo tiramos
        mv.visitInsn(Opcodes.POP);

        return null;
    }

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