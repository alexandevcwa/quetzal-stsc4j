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
            expr.message.accept(generator); // Empujamos el texto del mensaje

            // Usamos 'print' (no println) para que el usuario escriba al lado del mensaje
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "print", "(Ljava/lang/String;)V", false);
        }

        // 2. CREAR EL SCANNER: new Scanner(System.in)
        mv.visitTypeInsn(Opcodes.NEW, "java/util/Scanner");
        mv.visitInsn(Opcodes.DUP);
        mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "in", "Ljava/io/InputStream;");
        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/util/Scanner", "<init>", "(Ljava/io/InputStream;)V", false);

        // 3. LEER EL TECLADO: scanner.nextLine()
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/util/Scanner", "nextLine", "()Ljava/lang/String;", false);

        // El nextLine() deja automáticamente el texto ingresado en la cima de la pila.
        return TokenType.PRIMITIVE_STRING.name();
    }
}