package com.stsc4j.parser.v1.ast;

public class StatementTryCatchFinally extends Statement{

    public final StatementBlock tryBlock;
    public final ExpressionVariable exception;
    public final StatementBlock catchBlock;
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
