package com.stsc4j.generator.bytecode;

import com.stsc4j.generator.BytecodeAbstractGenerator;
import com.stsc4j.generator.BytecodeGenerator;
import com.stsc4j.parser.v1.ast.ExpressionPropertyAccess;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class BytecodeExpressionPropertyAccess extends BytecodeAbstractGenerator {

    private final BytecodeGenerator generator;

    public BytecodeExpressionPropertyAccess(BytecodeGenerator generator) {
        this.generator = generator;
    }

    @Override
    public String visit(ExpressionPropertyAccess expr) {
        MethodVisitor mv = generator.getMv();

        // 1. Cargar el objeto (LinkedHashMap) en la pila
        expr.object.accept(generator);

        // 2. Cargar el nombre de la propiedad (como String)
        String propiedad = expr.propertyName.getLexeme();
        mv.visitLdcInsn(propiedad);

        // 3. Llamar a LinkedHashMap.get(Object) -> devuelve Object
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/util/LinkedHashMap", "get", "(Ljava/lang/Object;)Ljava/lang/Object;", false);

        // Retornamos "JSN_OBJECT" para que el método imprimir() sepa que debe usar la versión genérica (Ljava/lang/Object;)V
        return "JSN_OBJECT";
    }
}