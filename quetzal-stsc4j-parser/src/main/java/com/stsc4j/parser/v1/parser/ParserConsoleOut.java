package com.stsc4j.parser.v1.parser;

import com.stsc4j.lexer.Token;
import com.stsc4j.lexer.TokenType;
import com.stsc4j.parser.v1.ast.Expression;
import com.stsc4j.parser.v1.ast.Statement;
import com.stsc4j.parser.v1.ast.StatementConsolaOut;
import com.stsc4j.parser.v1.exception.ParserException;

import static com.stsc4j.lexer.TokenType.*;

public class ParserConsoleOut extends Parser {

    private final TokenStream tokenStream;
    private final ParserExpression parserExpression;

    public ParserConsoleOut(TokenStream tokenStream, ParserExpression parserExpression) {
        this.tokenStream = tokenStream;
        this.parserExpression = parserExpression;
    }

    @Override
    public Statement parseStatement() {
        tokenStream.consume(TokenType.C_CONSOLE, "Se esperaba una llamada a la clase 'consola'");
        tokenStream.consume(DOT, "Se esperaba un '.' después de 'consola' para acceder a sus funciones");

        if (tokenStream.notMatch(F_PRINT, F_PRINT_ERROR, F_PRINT_WARNING, F_PRINT_INFO, F_PRINT_SUCCESS)) {
            throw new ParserException(String.format("La función '%s', no esta definida en la clase 'consola' para salida de datos", tokenStream.before().getLexeme()));
        }
        Token function = tokenStream.before();
        tokenStream.consume(LEFT_PARENT, "Se esperaba un apertura de parentesis '(' despues de la llamada de la función");
        Expression expression = parserExpression.parseExpression();
        tokenStream.consume(RIGHT_PARENT, "Se esperaba un cierre de parentesis ')' para finalizar la llamada a la función");
        return new StatementConsolaOut(function, expression);
    }
}
