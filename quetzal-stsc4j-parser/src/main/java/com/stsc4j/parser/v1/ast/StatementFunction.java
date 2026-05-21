package com.stsc4j.parser.v1.ast;

import com.stsc4j.lexer.Token;
import java.util.List;

/**
 * Declaración de funciones.
 */
public class StatementFunction extends Statement {
    /**
     * Tipo de dato de retorno
     */
    public final Token returnValue;

    /**
     * Nombre de función
     */
    public final Token identified;

    /**
     * Parámetros de función
     */
    public final List<Statement> parameters;

    /**
     * Block de sentencias de la función
     */
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
