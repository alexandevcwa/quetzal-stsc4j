package com.stsc4j.generator;

import com.stsc4j.generator.bytecode.*;
import com.stsc4j.parser.v1.ast.*;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Label;

import java.io.FileOutputStream;
import java.util.*;

public class BytecodeGenerator extends BytecodeAbstractGenerator {

    private EnvironmentJVM envJVM = new EnvironmentJVM();
    private ClassWriter cw;
    private MethodVisitor mv;
    private String nombreClaseActual;
    private final Map<String, String> tiposVariables = new HashMap<>();
    private final Map<String, String> firmasFunciones = new HashMap<>();

    // 1. Instancias de las clases delegadas
    private final BytecodeExpressionLiteral expLiteral = new BytecodeExpressionLiteral(this);
    private final BytecodeExpressionVariable expVariable = new BytecodeExpressionVariable(this);
    private final BytecodeExpressionBinary expBinary = new BytecodeExpressionBinary(this);
    private final BytecodeStatementVariable stmtVariable = new BytecodeStatementVariable(this);
    private final BytecodeStatementConsolaOut stmtConsolaOut = new BytecodeStatementConsolaOut(this);
    private final BytecodeExpressionMethodCall expMethodCall = new BytecodeExpressionMethodCall(this);
    private final BytecodeStatementBlock stmtBlock = new BytecodeStatementBlock(this);
    private final BytecodeStatementIf generadorIf = new BytecodeStatementIf(this);
    private final BytecodeStatementLoopDoWhile generadorLoopDoWhile = new BytecodeStatementLoopDoWhile(this);
    private final BytecodeStatementIncDec generadorIncDec = new BytecodeStatementIncDec(this);
    private final BytecodeExpressionIncDec expIncDec = new BytecodeExpressionIncDec(this);
    private final BytecodeStatementLoopWhile generadorLoopWhile = new BytecodeStatementLoopWhile(this);
    private final BytecodeStatementLoopFor generadorLoopFor = new BytecodeStatementLoopFor(this);
    private final BytecodeStatementBreak generadorBreak = new BytecodeStatementBreak(this);
    private final BytecodeStatementContinue generadorContinue = new BytecodeStatementContinue(this);
    private final BytecodeStatementFunction generadorFunction = new BytecodeStatementFunction(this);
    private final BytecodeStatementReturn generadorReturn = new BytecodeStatementReturn(this);
    private final BytecodeExpressionNull generadorNull = new BytecodeExpressionNull(this);
    private final BytecodeExpressionTernary generadorTernary = new BytecodeExpressionTernary(this);
    private final BytecodeExpressionConsoleIn generadorConsoleIn = new BytecodeExpressionConsoleIn(this);
    private final BytecodeStatementList generadorList = new BytecodeStatementList(this);
    private final BytecodeExpressionIndexAccess generadorIndexAccess = new BytecodeExpressionIndexAccess(this);
    private final BytecodeStatementLoopForEach generadorForEach = new BytecodeStatementLoopForEach(this);




    private final Deque<Label> pilaBreak = new ArrayDeque<>();
    private final Deque<Label> pilaContinue = new ArrayDeque<>();

    public void compile(List<Statement> statements, String nombreClaseSalida) {
        this.nombreClaseActual = nombreClaseSalida;
        cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        cw.visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC, nombreClaseSalida, null, "java/lang/Object", null);

        // =========================================================================
        // El constructor por defecto <init>
        // Esto equivale a: public MiClase() { super(); }
        // =========================================================================
        MethodVisitor initMv = cw.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V", null, null);
        initMv.visitCode();
        initMv.visitVarInsn(Opcodes.ALOAD, 0); // Carga 'this' en la pila
        initMv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false); // Llama a Object()
        initMv.visitInsn(Opcodes.RETURN);      // Retorna
        initMv.visitMaxs(1, 1);                // Tamaños máximos fijos para el constructor
        initMv.visitEnd();
        // =========================================================================

        // Ahora sí, empezamos con el main
        mv = cw.visitMethod(Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC, "main", "([Ljava/lang/String;)V", null, null);
        mv.visitCode();

        for (Statement stmt : statements) {
            stmt.accept(this);
        }

        mv.visitInsn(Opcodes.RETURN);
        mv.visitMaxs(0, 0); // ClassWriter.COMPUTE_FRAMES calcula esto por nosotros
        mv.visitEnd();
        cw.visitEnd();

        try (FileOutputStream fos = new FileOutputStream(nombreClaseSalida + ".class")) {
            fos.write(cw.toByteArray());
            System.out.println("¡Compilación exitosa! Archivo generado: " + nombreClaseSalida + ".class");
        } catch (Exception e) {
            System.err.println("Error al generar el .class: " + e.getMessage());
        }
    }

    // 2. Métodos para que los ciclos registren sus etiquetas al entrar y las borren al salir
    public void registrarCiclo(Label labelContinue, Label labelBreak) {
        pilaContinue.push(labelContinue);
        pilaBreak.push(labelBreak);
    }

    public void salirCiclo() {
        pilaContinue.pop();
        pilaBreak.pop();
    }

    public Label obtenerLabelBreak() { return pilaBreak.peek(); }
    public Label obtenerLabelContinue() { return pilaContinue.peek(); }

    // 2. Getters para que las clases delegadas puedan acceder a las herramientas ASM
    public MethodVisitor getMv() { return mv; }
    public EnvironmentJVM getEnvJVM() { return envJVM; }
    public Map<String, String> getTiposVariables() { return tiposVariables; }

    public ClassWriter getCw() { return cw; }
    public String getNombreClaseActual() { return nombreClaseActual; }
    public void setMv(MethodVisitor mv) { this.mv = mv; }
    public void setEnvJVM(EnvironmentJVM env) { this.envJVM = env; }

    public Map<String, String> getFirmasFunciones() { return firmasFunciones; }

    // 3. Despacho (Dispatcher) a las clases específicas
    @Override public String visit(ExpressionLiteral expr) { return expLiteral.visit(expr); }
    @Override public String visit(ExpressionVariable expr) { return expVariable.visit(expr); }
    @Override public String visit(ExpressionBinary expr) { return expBinary.visit(expr); }
    @Override public String visit(StatementVariable stmt) { return stmtVariable.visit(stmt); }
    @Override public String visit(StatementConsolaOut stmt) { return stmtConsolaOut.visit(stmt); }

    @Override
    public String visit(StatementExpression stmtExpr) {
        if (stmtExpr.expression != null) stmtExpr.expression.accept(this);
        return null;
    }

    @Override
    public String visit(ExpressionMethodCall expr) {
        return expMethodCall.visit(expr);
    }

    @Override
    public String visit(StatementBlock stmt) {
        return stmtBlock.visit(stmt);
    }

    @Override
    public String visit(StatementIf stmt) {
        return generadorIf.visit(stmt);
    }

    @Override
    public String visit(StatementLoopDoWhile stmt) {
        return generadorLoopDoWhile.visit(stmt);
    }

    @Override
    public String visit(StatementIncDec stmt) {
        return generadorIncDec.visit(stmt);
    }

    @Override
    public String visit(ExpressionIncDec expr) {
        return expIncDec.visit(expr);
    }

    @Override
    public String visit(StatementLoopWhile stmt) {
        return generadorLoopWhile.visit(stmt);
    }

    @Override
    public String visit(StatementLoopFor stmt) {
        return generadorLoopFor.visit(stmt);
    }

    @Override
    public String visit(StatementBreak stmt) {
        return generadorBreak.visit(stmt);
    }

    @Override
    public String visit(StatementContinue stmt) {
        return generadorContinue.visit(stmt);
    }

    @Override
    public String visit(StatementFunction stmt) {
        return generadorFunction.visit(stmt);
    }

    @Override
    public String visit(StatementReturn stmt) {
        return generadorReturn.visit(stmt);
    }

    @Override
    public String visit(ExpressionNull expr) {
        return generadorNull.visit(expr);
    }

    @Override
    public String visit(ExpressionTernary expr){
        return generadorTernary.visit(expr);
    }

    @Override
    public String visit(ExpressionConsoleIn expr) {
        return generadorConsoleIn.visit(expr);
    }

    @Override
    public String visit(StatementList stmt) {
        return generadorList.visit(stmt);
    }

    @Override
    public String visit(ExpressionIndexAccess expr) {
        return generadorIndexAccess.visit(expr);
    }

    @Override
    public String visit(StatementLoopForEach stmt) {
        return generadorForEach.visit(stmt);
    }
}