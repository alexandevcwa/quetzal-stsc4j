package com.stsc4j.generator.bytecode;

import com.stsc4j.generator.BytecodeAbstractGenerator;
import com.stsc4j.generator.BytecodeGenerator;
import com.stsc4j.lexer.TokenType;
import com.stsc4j.parser.v1.ast.StatementLoopForEach;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class BytecodeStatementLoopForEach extends BytecodeAbstractGenerator {

    private final BytecodeGenerator generator;

    public BytecodeStatementLoopForEach(BytecodeGenerator generator) {
        this.generator = generator;
    }

    @Override
    public String visit(StatementLoopForEach stmt) {
        MethodVisitor mv = generator.getMv();

        // ==========================================================
        // 1. DECLARAR Y REGISTRAR LA VARIABLE DE ITERACIÓN
        // ==========================================================
        String nombreIterador = stmt.declaration.variable.token.getLexeme();
        String lexemaTipo = stmt.declaration.type.getLexeme();

        // 🚨 ¡EL PASO CLAVE! Reservamos el espacio en la memoria para 'n'
        generator.getEnvJVM().registrarVariable(nombreIterador);

        // Traducimos el tipo (ej. "entero") al formato interno para que las demás clases lo entiendan
        String tipoInterno = TokenType.PRIMITIVE_STRING.name(); // Por defecto
        if (lexemaTipo.equals("entero")) {
            tipoInterno = TokenType.PRIMITIVE_INTEGER.name();
        } else if (lexemaTipo.equals("decimal") || lexemaTipo.equals("número")) {
            tipoInterno = TokenType.PRIMITIVE_DECIMAL.name();
        } else if (lexemaTipo.equals("log") || lexemaTipo.equals("booleano")) {
            tipoInterno = TokenType.PRIMITIVE_BOOLEAN.name();
        }

        // Lo guardamos en el diccionario global del orquestador
        generator.getTiposVariables().put(nombreIterador, tipoInterno);

        // Ahora sí, obtenemos el índice sin que explote
        int indiceIterador = generator.getEnvJVM().obtenerIndice(nombreIterador);

        // 2. Cargar la lista en la pila y obtener su índice en memoria
        stmt.listVariable.accept(generator);
        int indiceArreglo = generator.getEnvJVM().obtenerIndice(stmt.listVariable.token.getLexeme());
        String tipoLista = generator.getTiposVariables().get(stmt.listVariable.token.getLexeme());

        // 3. Crear contador oculto (índice i) y ponerlo en 0
        // Registramos un índice temporal único para este bucle
        String nombreContador = "tmp_i_" + System.nanoTime();
        generator.getEnvJVM().registrarVariable(nombreContador);
        int indiceI = generator.getEnvJVM().obtenerIndice(nombreContador);

        mv.visitInsn(Opcodes.ICONST_0);
        mv.visitVarInsn(Opcodes.ISTORE, indiceI);

        Label labelInicio = new Label();
        Label labelFin = new Label();
        generator.registrarCiclo(labelInicio, labelFin); // Soporte para break/continue

        mv.visitLabel(labelInicio);

        // 4. CONDICIÓN: ¿i < lista.length?
        mv.visitVarInsn(Opcodes.ILOAD, indiceI);
        mv.visitVarInsn(Opcodes.ALOAD, indiceArreglo);
        mv.visitInsn(Opcodes.ARRAYLENGTH);
        mv.visitJumpInsn(Opcodes.IF_ICMPGE, labelFin);

        // 5. OBTENER ELEMENTO: lista[i]
        mv.visitVarInsn(Opcodes.ALOAD, indiceArreglo);
        mv.visitVarInsn(Opcodes.ILOAD, indiceI);

        // Selección inteligente de instrucción de carga
        if (tipoLista.startsWith("[I")) mv.visitInsn(Opcodes.IALOAD);
        else if (tipoLista.startsWith("[F")) mv.visitInsn(Opcodes.FALOAD);
        else mv.visitInsn(Opcodes.AALOAD);

        // 6. ASIGNAR a la variable declarada (ej: valor_numero)
        if (tipoLista.startsWith("[F")) mv.visitVarInsn(Opcodes.FSTORE, indiceIterador);
        else if (tipoLista.startsWith("[I")) mv.visitVarInsn(Opcodes.ISTORE, indiceIterador);
        else mv.visitVarInsn(Opcodes.ASTORE, indiceIterador);

        // 7. EJECUTAR BLOQUE
        stmt.block.accept(generator);

        // 8. INCREMENTAR i++
        mv.visitIincInsn(indiceI, 1);
        mv.visitJumpInsn(Opcodes.GOTO, labelInicio);

        mv.visitLabel(labelFin);
        generator.salirCiclo();

        return null;
    }
}