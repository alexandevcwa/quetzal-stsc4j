package com.stsc4j.generator.bytecode;

import com.stsc4j.generator.BytecodeAbstractGenerator;
import com.stsc4j.generator.BytecodeGenerator;
import com.stsc4j.lexer.TokenType;
import com.stsc4j.parser.v1.ast.ExpressionConsoleIn;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class BytecodeExpressionConsoleIn extends BytecodeAbstractGenerator {

    private final BytecodeGenerator generator;

    public BytecodeExpressionConsoleIn(BytecodeGenerator generator) {
        this.generator = generator;
    }

    @Override
    public String visit(ExpressionConsoleIn expr) {
        MethodVisitor mv = generator.getMv();

        // 1. MOSTRAR EL MENSAJE (Si el usuario escribió consola.pedir("Ingrese nombre:"))
        if (expr.message != null) {
            mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
            expr.message.accept(generator);
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "print", "(Ljava/lang/String;)V", false);
        }

        // 2. CREAR EL LECTOR ESTRICTO (DataInputStream)
        // A diferencia de Scanner o BufferedReader, este no absorbe bytes adicionales del System.in
        mv.visitTypeInsn(Opcodes.NEW, "java/io/DataInputStream");
        mv.visitInsn(Opcodes.DUP);
        mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "in", "Ljava/io/InputStream;");
        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/io/DataInputStream", "<init>", "(Ljava/io/InputStream;)V", false);

        // 3. LEER EXACTAMENTE UNA LÍNEA
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/DataInputStream", "readLine", "()Ljava/lang/String;", false);

        return TokenType.PRIMITIVE_STRING.name();
    }
}