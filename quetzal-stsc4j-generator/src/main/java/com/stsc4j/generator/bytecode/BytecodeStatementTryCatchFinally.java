package com.stsc4j.generator.bytecode;

import com.stsc4j.generator.BytecodeAbstractGenerator;
import com.stsc4j.generator.BytecodeGenerator;
import com.stsc4j.parser.v1.ast.StatementTryCatchFinally;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class BytecodeStatementTryCatchFinally extends BytecodeAbstractGenerator {

    private final BytecodeGenerator generator;

    public BytecodeStatementTryCatchFinally(BytecodeGenerator generator) {
        this.generator = generator;
    }

    @Override
    public String visit(StatementTryCatchFinally stmt) {
        MethodVisitor mv = generator.getMv();

        // Etiquetas para delimitar las zonas de peligro
        Label tryInicio = new Label();
        Label tryFin = new Label();
        Label catchInicio = new Label();
        Label finTotal = new Label();
        Label finallyCatchAll = new Label(); // Ruta oculta para errores no manejados

        // 1. Declarar la Tabla de Excepciones a la JVM (Se hace antes de visitar las etiquetas)
        mv.visitTryCatchBlock(tryInicio, tryFin, catchInicio, "java/lang/Exception");

        // Si hay finally, registramos una red de seguridad absoluta
        if (stmt.finallyBlock != null) {
            mv.visitTryCatchBlock(tryInicio, tryFin, finallyCatchAll, null);
            mv.visitTryCatchBlock(catchInicio, finTotal, finallyCatchAll, null);
        }

        // ==========================================
        // BLOQUE TRY
        // ==========================================
        mv.visitLabel(tryInicio);
        stmt.tryBlock.accept(generator);
        mv.visitLabel(tryFin);

        // Si todo sale bien, ejecutamos finally y saltamos la zona del catch
        if (stmt.finallyBlock != null) stmt.finallyBlock.accept(generator);
        mv.visitJumpInsn(Opcodes.GOTO, finTotal);


        // ==========================================
        // BLOQUE CATCH
        // ==========================================
        mv.visitLabel(catchInicio);

        // La JVM atrapó el error y lo dejó en la cima de la pila.
        // Lo guardamos en la variable que indicó el usuario (ej: 'catch(e)').
        String nombreExcepcion = stmt.exception.token.getLexeme();
        generator.getEnvJVM().registrarVariable(nombreExcepcion);
        int idxEx = generator.getEnvJVM().obtenerIndice(nombreExcepcion);
        generator.getTiposVariables().put(nombreExcepcion, "java/lang/Exception"); // Registramos como Objeto
        mv.visitVarInsn(Opcodes.ASTORE, idxEx); // Guardar la excepción

        // Ejecutar código del usuario en el catch
        stmt.catchBlock.accept(generator);

        // Al terminar el catch, ejecutamos finally y vamos al final
        if (stmt.finallyBlock != null) stmt.finallyBlock.accept(generator);
        mv.visitJumpInsn(Opcodes.GOTO, finTotal);


        // ==========================================
        // BLOQUE FINALLY (Ruta oculta Catch-All)
        // ==========================================
        if (stmt.finallyBlock != null) {
            mv.visitLabel(finallyCatchAll);

            // Un error crítico no manejado ocurrió. Lo guardamos temporalmente.
            mv.visitVarInsn(Opcodes.ASTORE, idxEx);

            // Ejecutamos obligatoriamente el finally
            stmt.finallyBlock.accept(generator);

            // Recuperamos el error crítico y lo volvemos a lanzar
            mv.visitVarInsn(Opcodes.ALOAD, idxEx);
            mv.visitInsn(Opcodes.ATHROW);
        }

        // ==========================================
        // LÍNEA DE META
        // ==========================================
        mv.visitLabel(finTotal);

        return null;
    }
}