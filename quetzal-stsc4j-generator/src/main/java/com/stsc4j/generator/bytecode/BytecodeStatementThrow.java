package com.stsc4j.generator.bytecode;

import com.stsc4j.generator.BytecodeAbstractGenerator;
import com.stsc4j.generator.BytecodeGenerator;
import com.stsc4j.parser.v1.ast.StatementThrow;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class BytecodeStatementThrow extends BytecodeAbstractGenerator {

    private final BytecodeGenerator generator;

    public BytecodeStatementThrow(BytecodeGenerator generator) {
        this.generator = generator;
    }

    @Override
    public String visit(StatementThrow stmt) {
        MethodVisitor mv = generator.getMv();

        // 1. Crear el objeto RuntimeException
        mv.visitTypeInsn(Opcodes.NEW, "java/lang/RuntimeException");
        mv.visitInsn(Opcodes.DUP);

        // 2. Extraer el texto del error
        String mensaje = stmt.message.token.getLexeme();

        // Limpiamos las comillas iniciales y finales que vienen del Lexer
        if (mensaje.startsWith("\"") && mensaje.endsWith("\"")) {
            mensaje = mensaje.substring(1, mensaje.length() - 1);
        }

        // 3. Empujar el mensaje a la pila y llamar al constructor
        mv.visitLdcInsn(mensaje);
        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/RuntimeException", "<init>", "(Ljava/lang/String;)V", false);

        // 4. ¡LANZAR!
        mv.visitInsn(Opcodes.ATHROW);

        return null;
    }
}