package com.stsc4j.generator.bytecode;

import com.stsc4j.generator.BytecodeAbstractGenerator;
import com.stsc4j.generator.BytecodeGenerator;
import com.stsc4j.lexer.TokenType;
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

        // 1. Evaluamos el objeto base (empuja el HashMap o la Excepción a la pila)
        String tipoObjeto = expr.object.accept(generator);
        String propiedad = expr.propertyName.getLexeme();

        // ==========================================
        // 2. INTERCEPTOR DE EXCEPCIONES
        // ==========================================
        if ("java/lang/Exception".equals(tipoObjeto)) {

            if ("mensaje".equals(propiedad)) {
                // Traduce 'error.mensaje' a 'e.getMessage()'
                mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/Exception", "getMessage", "()Ljava/lang/String;", false);
                return TokenType.PRIMITIVE_STRING.name();
            }
            else if ("linea".equals(propiedad)) {
                // Traduce 'error.linea' a 'e.getStackTrace()[0].getLineNumber()'
                mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/Exception", "getStackTrace", "()[Ljava/lang/StackTraceElement;", false);
                mv.visitInsn(Opcodes.ICONST_0); // Tomamos el primer elemento (índice 0)
                mv.visitInsn(Opcodes.AALOAD);
                mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StackTraceElement", "getLineNumber", "()I", false);
                return TokenType.PRIMITIVE_INTEGER.name();
            }
            else if ("llamadas".equals(propiedad)) {
                // Dejamos la excepción intacta en la pila, pero le cambiamos la "etiqueta"
                // para que la siguiente operación (.texto()) sepa de dónde viene.
                return "EXCEPTION_LLAMADAS";
            }
        }

        // ==========================================
        // 3. COMPORTAMIENTO ORIGINAL (Objetos JSN)
        // ==========================================
        mv.visitLdcInsn(propiedad);
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/util/LinkedHashMap", "get", "(Ljava/lang/Object;)Ljava/lang/Object;", false);
        return "JSN_OBJECT";
    }
}