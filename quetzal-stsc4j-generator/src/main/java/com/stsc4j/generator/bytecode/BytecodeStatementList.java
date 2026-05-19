package com.stsc4j.generator.bytecode;

import com.stsc4j.generator.BytecodeAbstractGenerator;
import com.stsc4j.generator.BytecodeGenerator;
import com.stsc4j.lexer.TokenType;
import com.stsc4j.parser.v1.ast.StatementList;
import com.stsc4j.parser.v1.ast.TypePrimitive;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class BytecodeStatementList extends BytecodeAbstractGenerator {

    private final BytecodeGenerator generator;

    public BytecodeStatementList(BytecodeGenerator generator) {
        this.generator = generator;
    }

    @Override
    public String visit(StatementList stmt) {
        MethodVisitor mv = generator.getMv();
        String nombreLista = stmt.listName.getLexeme();

        // 1. Reservamos el casillero de memoria para la variable
        generator.getEnvJVM().registrarVariable(nombreLista);
        int indiceMemoria = generator.getEnvJVM().obtenerIndice(nombreLista);

        // 2. Empujamos el tamaño del arreglo a la pila
        int size = stmt.expressionList.expressions.size();
        mv.visitIntInsn(Opcodes.BIPUSH, size);

        // 3. Extraemos el tipo de dato que guardará la lista
        TypePrimitive tipoPrim = (TypePrimitive) stmt.type.elementType;
        String tipoQuetzal = tipoPrim.primitiveType.getLexeme();

        // 4. Creamos el arreglo en la JVM y registramos su firma secreta (Ej. "[I" para int[])
        String arrayDescriptor;
        if (tipoQuetzal.equals("entero")) {
            mv.visitIntInsn(Opcodes.NEWARRAY, Opcodes.T_INT);
            arrayDescriptor = "[I";
        } else if (tipoQuetzal.equals("decimal") || tipoQuetzal.equals("numero")) {
            mv.visitIntInsn(Opcodes.NEWARRAY, Opcodes.T_FLOAT);
            arrayDescriptor = "[F";
        } else if (tipoQuetzal.equals("booleano")) {
            mv.visitIntInsn(Opcodes.NEWARRAY, Opcodes.T_BOOLEAN);
            arrayDescriptor = "[Z";
        } else {
            mv.visitTypeInsn(Opcodes.ANEWARRAY, "java/lang/String");
            arrayDescriptor = "[Ljava/lang/String;";
        }

        // Guardamos el tipo con corchetes para que el resto del compilador sepa que es una lista
        generator.getTiposVariables().put(nombreLista, arrayDescriptor);

        // 5. Llenamos el arreglo iterando sobre las expresiones
        for (int i = 0; i < size; i++) {
            mv.visitInsn(Opcodes.DUP); // Duplicamos la referencia del arreglo
            mv.visitIntInsn(Opcodes.BIPUSH, i); // Posición (índice)

            // Evaluamos la expresión (esto empuja el valor a la pila)
            stmt.expressionList.expressions.get(i).accept(generator);

            // Guardamos el valor en el arreglo (Array Store)
            if (tipoQuetzal.equals("entero")) mv.visitInsn(Opcodes.IASTORE);
            else if (tipoQuetzal.equals("decimal") || tipoQuetzal.equals("numero")) mv.visitInsn(Opcodes.FASTORE);
            else if (tipoQuetzal.equals("booleano")) mv.visitInsn(Opcodes.BASTORE);
            else mv.visitInsn(Opcodes.AASTORE);
        }

        // 6. Guardamos el arreglo completo en la variable local
        // ¡OJO! Como los arreglos son objetos en Java, usamos ASTORE (no ISTORE ni FSTORE)
        mv.visitVarInsn(Opcodes.ASTORE, indiceMemoria);

        return null;
    }
}