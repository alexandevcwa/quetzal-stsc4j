package com.stsc4j.parser.v1.ast;

import com.stsc4j.lexer.Token;

public class StatementFuncionParameter extends Statement {
    public Token type;
    public Token identified;

    public StatementFuncionParameter(Token type, Token identified){
        this.type = type;
        this.identified = identified;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
