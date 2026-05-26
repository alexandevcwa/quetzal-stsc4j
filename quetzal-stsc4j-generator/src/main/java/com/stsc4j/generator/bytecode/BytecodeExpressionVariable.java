package com.stsc4j.generator.bytecode;

import com.stsc4j.generator.BytecodeAbstractGenerator;
import com.stsc4j.generator.BytecodeGenerator;
import com.stsc4j.lexer.TokenType;
import com.stsc4j.parser.v1.ast.ExpressionVariable;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class BytecodeExpressionVariable extends BytecodeAbstractGenerator {

    private final BytecodeGenerator generator;

    public BytecodeExpressionVariable(BytecodeGenerator generator) {
        this.generator = generator;
    }

    @Override
    public String visit(ExpressionVariable expr) {
        MethodVisitor mv = generator.getMv();
        String nombreVar = expr.token.getLexeme();

        int indiceMemoria = generator.getEnvJVM().obtenerIndice(nombreVar);
        String tipo = generator.getTiposVariables().get(nombreVar);

        // Si la clase de Funciones olvidó registrar el tipo del parámetro,
        // asumimos que es un entero para no romper la pila.
        if (tipo == null) {
            tipo = TokenType.PRIMITIVE_INTEGER.name();
        }

        // 1. ENTEROS Y BOOLEANOS (Soporta Tokens oficiales, palabras crudas Y "IDENTIFIER")
        if (tipo.equals(TokenType.PRIMITIVE_INTEGER.name()) || tipo.equals(TokenType.PRIMITIVE_BOOLEAN.name()) ||
                tipo.equals("entero") || tipo.equals("booleano") || tipo.equals("log") ||
                tipo.equals("IDENTIFIER")) { //: Forzamos ILOAD para IDENTIFIER
            mv.visitVarInsn(Opcodes.ILOAD, indiceMemoria);
        }
        // 2. DECIMALES
        else if (tipo.equals(TokenType.PRIMITIVE_DECIMAL.name()) || tipo.equals("numero") || tipo.equals("decimal")) {
            mv.visitVarInsn(Opcodes.FLOAD, indiceMemoria);
        }
        // 3. OBJETOS, ARREGLOS Y TEXTOS
        else {
            mv.visitVarInsn(Opcodes.ALOAD, indiceMemoria);
        }

        // Normalizamos la salida para que el Orquestador siempre reciba el nombre del Token oficial
        // NORMALIZACIÓN: Si era IDENTIFIER, le mentimos al resto del compilador diciendo que es PRIMITIVE_INTEGER
        if (tipo.equals("entero") || tipo.equals("IDENTIFIER")) return TokenType.PRIMITIVE_INTEGER.name();
        if (tipo.equals("numero") || tipo.equals("decimal")) return TokenType.PRIMITIVE_DECIMAL.name();
        if (tipo.equals("log") || tipo.equals("booleano")) return TokenType.PRIMITIVE_BOOLEAN.name();
        if (tipo.equals("texto")) return TokenType.PRIMITIVE_STRING.name();
        if (tipo.equals("jsn")) return "JSN_OBJECT";

        return tipo;
    }
}