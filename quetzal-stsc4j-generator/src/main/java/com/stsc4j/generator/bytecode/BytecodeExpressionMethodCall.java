package com.stsc4j.generator.bytecode;

import com.stsc4j.generator.BytecodeAbstractGenerator;
import com.stsc4j.generator.BytecodeGenerator;
import com.stsc4j.lexer.TokenType;
import com.stsc4j.parser.v1.ast.Expression;
import com.stsc4j.parser.v1.ast.ExpressionMethodCall;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class BytecodeExpressionMethodCall extends BytecodeAbstractGenerator {

    private final BytecodeGenerator generator;

    public BytecodeExpressionMethodCall(BytecodeGenerator generator) {
        this.generator = generator;
    }

    @Override
    public String visit(ExpressionMethodCall expr) {
        MethodVisitor mv = generator.getMv();
        String nombreMetodo = expr.methodName.getLexeme();

        // ====================================================================
        // 1. INTERCEPTAMOS FUNCIONES NATIVAS DE LA LIBRERÍA DE QUETZAL
        // ====================================================================
        if (nombreMetodo.equals("consola.mostrar") ||
                nombreMetodo.startsWith("consola.mostrar_") ||
                nombreMetodo.startsWith("imprimir")) {

            // Cargamos System.out en la pila de la JVM
            mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");

            if (expr.args != null && !expr.args.isEmpty()) {
                // Visitamos el argumento para que deje su valor en la pila
                String tipoArg = expr.args.get(0).accept(generator);

                // Elegimos el método println correcto según el tipo de dato
                if (TokenType.PRIMITIVE_DECIMAL.name().equals(tipoArg)) {
                    mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(F)V", false);
                }
                else if (TokenType.PRIMITIVE_INTEGER.name().equals(tipoArg)) {
                    mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(I)V", false);
                }
                else if (TokenType.PRIMITIVE_BOOLEAN.name().equals(tipoArg)) {
                    mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Z)V", false);
                }
                else { // Asumimos que es cadena / texto
                    mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
                }
            }
            return null; // Las funciones nativas de impresión no retornan valores
        }

        // ====================================================================
        // 2. LLAMADA A FUNCIONES CREADAS POR EL USUARIO
        // ====================================================================

        // A. Empujamos los argumentos a la pila de la JVM
        if (expr.args != null) {
            for (Expression arg : expr.args) {
                arg.accept(generator);
            }
        }

        // B. Buscamos la firma de la función en el directorio del Orquestador
        String descriptor = generator.getFirmasFunciones().get(nombreMetodo);

        if (descriptor == null) {
            // Un pequeño salvavidas: si la función no existe, asumimos que es una función vacía
            // (El analizador semántico ya debería haber evitado que esto ocurra)
            descriptor = "()V";
        }

        // C. ¡EL SALTO! Invocamos la función estática en la misma clase
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, generator.getNombreClaseActual(), nombreMetodo, descriptor, false);

        // D. DEVOLVEMOS EL TIPO DE DATO (Para poder hacer matemáticas con el resultado)
        // Extraemos la letra de retorno (lo que está después del ')')
        String letraRetorno = descriptor.substring(descriptor.indexOf(')') + 1);
        return obtenerTipoToken(letraRetorno);
    }

    // --- UTILIDAD: Traducir de vuelta el descriptor de la JVM a los tokens de Quetzal ---
    private String obtenerTipoToken(String letra) {
        switch (letra) {
            case "I": return TokenType.PRIMITIVE_INTEGER.name();
            case "F": return TokenType.PRIMITIVE_DECIMAL.name();
            case "Z": return TokenType.PRIMITIVE_BOOLEAN.name();
            case "Ljava/lang/String;": return TokenType.PRIMITIVE_STRING.name();
            default: return null; // Para funciones vacías (Void)
        }
    }
}