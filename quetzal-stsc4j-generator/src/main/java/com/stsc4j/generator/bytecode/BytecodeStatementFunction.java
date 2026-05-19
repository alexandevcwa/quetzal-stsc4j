package com.stsc4j.generator.bytecode;

import com.stsc4j.generator.BytecodeAbstractGenerator;
import com.stsc4j.generator.BytecodeGenerator;
import com.stsc4j.generator.EnvironmentJVM;
import com.stsc4j.parser.v1.ast.Statement;
import com.stsc4j.parser.v1.ast.StatementFunction;
import com.stsc4j.parser.v1.ast.StatementFunctionParameter;
import com.stsc4j.semantic.Environment; // Asegúrate de que esta ruta sea correcta
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class BytecodeStatementFunction extends BytecodeAbstractGenerator {

    private final BytecodeGenerator generator;

    public BytecodeStatementFunction(BytecodeGenerator generator) {
        this.generator = generator;
    }

    @Override
    public String visit(StatementFunction stmt) {
        String nombreMetodo = stmt.identified.getLexeme();

        // 1. CONSTRUIR EL DESCRIPTOR: Ej. (I)I
        StringBuilder descriptor = new StringBuilder("(");
        for (Statement paramStmt : stmt.parameters) {
            StatementFunctionParameter param = (StatementFunctionParameter) paramStmt;
            descriptor.append(TypeDescriptor.obtener(param.type.getLexeme()));
        }
        descriptor.append(")").append(TypeDescriptor.obtener(stmt.returnValue.getLexeme()));

        // Guardamos la firma en el orquestador para usarla después
        generator.getFirmasFunciones().put(nombreMetodo, descriptor.toString());

        // 2. GUARDAR EL CONTEXTO ANTERIOR (El método main)
        MethodVisitor mvAnterior = generator.getMv();
        EnvironmentJVM envAnterior = generator.getEnvJVM();

        // 3. CREAR EL NUEVO MÉTODO (Público y Estático)
        MethodVisitor nuevoMv = generator.getCw().visitMethod(
                Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC,
                nombreMetodo,
                descriptor.toString(),
                null, null);
        nuevoMv.visitCode();

        // 4. CAMBIAR AL NUEVO CONTEXTO
        generator.setMv(nuevoMv);
        EnvironmentJVM nuevoEnv = new EnvironmentJVM(0); // ¡Memoria limpia para la función!
        generator.setEnvJVM(nuevoEnv);

        // 5. REGISTRAR LOS PARÁMETROS EN LA MEMORIA NUEVA (Slot 0, 1, 2...)
        for (Statement paramStmt : stmt.parameters) {
            StatementFunctionParameter param = (StatementFunctionParameter) paramStmt;
            String nombreParam = param.identified.getLexeme();
            nuevoEnv.registrarVariable(nombreParam); // Esto le asigna automáticamente un índice
            generator.getTiposVariables().put(nombreParam, param.type.getLexeme());
        }

        // 6. COMPILAR EL CUERPO DE LA FUNCIÓN
        if (stmt.block != null) {
            stmt.block.accept(generator);
        }

        // 7. ASEGURAR EL RETORNO (Si la función es vacía y el usuario no puso 'return')
        if (TypeDescriptor.obtener(stmt.returnValue.getLexeme()).equals("V")) {
            nuevoMv.visitInsn(Opcodes.RETURN);
        }

        nuevoMv.visitMaxs(0, 0); // ASM lo calcula solo
        nuevoMv.visitEnd();

        // 8. RESTAURAR EL CONTEXTO (Volver al main)
        generator.setMv(mvAnterior);
        generator.setEnvJVM(envAnterior);

        return null;
    }
}