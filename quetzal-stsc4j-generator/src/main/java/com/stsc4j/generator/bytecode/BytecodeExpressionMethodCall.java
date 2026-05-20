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
        // 1. FUNCIONES GLOBALES NATIVAS (imprimir, consola.mostrar)
        // ====================================================================
        if (nombreMetodo.equals("consola.mostrar") || nombreMetodo.startsWith("consola.mostrar_") || nombreMetodo.equals("imprimir")) {
            mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
            if (expr.args != null && !expr.args.isEmpty()) {
                String tipoArg = expr.args.get(0).accept(generator);
                if (TokenType.PRIMITIVE_DECIMAL.name().equals(tipoArg)) {
                    mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(F)V", false);
                } else if (TokenType.PRIMITIVE_INTEGER.name().equals(tipoArg)) {
                    mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(I)V", false);
                } else if (TokenType.PRIMITIVE_BOOLEAN.name().equals(tipoArg)) {
                    mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Z)V", false);
                } else if (TokenType.PRIMITIVE_STRING.name().equals(tipoArg)) {
                    mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
                } else {
                    mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/Object;)V", false);
                }
            }
            return null;
        }

        // ====================================================================
        // 2. LLAMADAS A MÉTODOS DE OBJETOS (JSN)
        // ====================================================================
        if (expr.object != null) {
            boolean esLlamadaAObjeto = true;

            // 🚨 PARCHE PARA EL AST: Verificamos si el Parser nos pasó una falsa variable
            if (expr.object instanceof com.stsc4j.parser.v1.ast.ExpressionVariable) {
                com.stsc4j.parser.v1.ast.ExpressionVariable varExpr = (com.stsc4j.parser.v1.ast.ExpressionVariable) expr.object;
                if (!generator.getTiposVariables().containsKey(varExpr.token.getLexeme())) {
                    // La variable no existe. Es una llamada directa a función global.
                    esLlamadaAObjeto = false;
                }
            }

            if (esLlamadaAObjeto) {
                String tipoObjeto = expr.object.accept(generator);

                if ("JSN_OBJECT".equals(tipoObjeto)) {
                    switch (nombreMetodo) {
                        case "contiene_clave":
                            expr.args.get(0).accept(generator);
                            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/util/LinkedHashMap", "containsKey", "(Ljava/lang/Object;)Z", false);
                            return TokenType.PRIMITIVE_BOOLEAN.name();

                        case "claves":
                            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/util/LinkedHashMap", "keySet", "()Ljava/util/Set;", false);
                            mv.visitInsn(Opcodes.DUP);
                            mv.visitMethodInsn(Opcodes.INVOKEINTERFACE, "java/util/Set", "size", "()I", true);
                            mv.visitTypeInsn(Opcodes.ANEWARRAY, "java/lang/String");
                            mv.visitMethodInsn(Opcodes.INVOKEINTERFACE, "java/util/Set", "toArray", "([Ljava/lang/Object;)[Ljava/lang/Object;", true);
                            mv.visitTypeInsn(Opcodes.CHECKCAST, "[Ljava/lang/String;");
                            return "[Ljava/lang/String;";

                        case "valores":
                            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/util/LinkedHashMap", "values", "()Ljava/util/Collection;", false);
                            mv.visitInsn(Opcodes.DUP);
                            mv.visitMethodInsn(Opcodes.INVOKEINTERFACE, "java/util/Collection", "size", "()I", true);
                            mv.visitTypeInsn(Opcodes.ANEWARRAY, "java/lang/Object");
                            mv.visitMethodInsn(Opcodes.INVOKEINTERFACE, "java/util/Collection", "toArray", "([Ljava/lang/Object;)[Ljava/lang/Object;", true);
                            return "[Ljava/lang/Object;";

                        case "establecer":
                            expr.args.get(0).accept(generator);
                            String tVal = expr.args.get(1).accept(generator);
                            empaquetarPrimitivo(mv, tVal);
                            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/util/LinkedHashMap", "put", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", false);
                            mv.visitInsn(Opcodes.POP);
                            return null;

                        case "eliminar":
                            expr.args.get(0).accept(generator);
                            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/util/LinkedHashMap", "remove", "(Ljava/lang/Object;)Ljava/lang/Object;", false);
                            return "JSN_OBJECT";

                        case "fusionar":
                            expr.args.get(0).accept(generator);
                            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/util/LinkedHashMap", "putAll", "(Ljava/util/Map;)V", false);
                            return null;

                        case "texto":
                        case "texto_formateado":
                            mv.visitMethodInsn(Opcodes.INVOKESTATIC, "com/stsc4j/generator/bytecode/JsnRuntime", "texto", "(Ljava/util/LinkedHashMap;)Ljava/lang/String;", false);
                            return TokenType.PRIMITIVE_STRING.name();
                    }
                } else if (TokenType.PRIMITIVE_STRING.name().equals(tipoObjeto) && "jsn".equals(nombreMetodo)) {
                    mv.visitMethodInsn(Opcodes.INVOKESTATIC, "com/stsc4j/generator/bytecode/JsnRuntime", "jsn", "(Ljava/lang/String;)Ljava/util/LinkedHashMap;", false);
                    return "JSN_OBJECT";
                }
                return null;
            }
        }

        // ====================================================================
        // 3. LLAMADA A FUNCIONES DE USUARIO
        // ====================================================================
        if (expr.args != null) {
            for (Expression arg : expr.args) arg.accept(generator);
        }

        String descriptor = generator.getFirmasFunciones().get(nombreMetodo);
        if (descriptor == null) descriptor = "()V";

        mv.visitMethodInsn(Opcodes.INVOKESTATIC, generator.getNombreClaseActual(), nombreMetodo, descriptor, false);
        String letraRetorno = descriptor.substring(descriptor.indexOf(')') + 1);
        return obtenerTipoToken(letraRetorno);
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

    private String obtenerTipoToken(String letra) {
        switch (letra) {
            case "I": return TokenType.PRIMITIVE_INTEGER.name();
            case "F": return TokenType.PRIMITIVE_DECIMAL.name();
            case "Z": return TokenType.PRIMITIVE_BOOLEAN.name();
            case "Ljava/lang/String;": return TokenType.PRIMITIVE_STRING.name();
            default: return null;
        }
    }
}