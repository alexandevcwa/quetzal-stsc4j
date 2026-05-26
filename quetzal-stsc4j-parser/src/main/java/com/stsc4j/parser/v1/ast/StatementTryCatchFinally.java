package com.stsc4j.parser.v1.ast;

/**
 * Sentencia try-catch-finally.
 */
public class StatementTryCatchFinally extends Statement{

    /**
     * Bloque try.
     */
    public final StatementBlock tryBlock;

    /**
     * Expresión variable que representa la excepción capturada en el bloque catch.
     */
    public final ExpressionVariable exception;

    /**
     * Bloque catch.
     */
    public final StatementBlock catchBlock;

    /**
     * Bloque finally (opcional).
     */
    public final StatementBlock finallyBlock;

    public StatementTryCatchFinally(StatementBlock tryBlock, ExpressionVariable exception, StatementBlock catchBlock, StatementBlock finallyBlock) {
        this.tryBlock = tryBlock;
        this.exception = exception;
        this.catchBlock = catchBlock;
        this.finallyBlock = finallyBlock;
    }

    public StatementTryCatchFinally(StatementBlock tryBlock, ExpressionVariable exception, StatementBlock catchBlock) {
        this.tryBlock = tryBlock;
        this.exception = exception;
        this.catchBlock = catchBlock;
        this.finallyBlock = null;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
