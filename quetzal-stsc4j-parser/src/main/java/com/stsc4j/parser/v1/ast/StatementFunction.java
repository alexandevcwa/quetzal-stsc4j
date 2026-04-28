package com.stsc4j.parser.v1.ast;

import com.stsc4j.lexer.Token;
import com.stsc4j.parser.v1.parser.ParserFunctionParameter;

import java.util.List;

public class StatementFunction extends Statement {

    public final Token returnValue;
    public final Token identified;
    public final List<Statement> parameters;
    public final StatementBlock block;

    public StatementFunction(Token returnValue, Token identified, List<Statement> parameters, StatementBlock block){
        this.parameters = parameters;
        this.block = block;
        this.returnValue = returnValue;
        this.identified = identified;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
