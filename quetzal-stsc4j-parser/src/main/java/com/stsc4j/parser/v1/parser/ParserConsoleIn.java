package com.stsc4j.parser.v1.parser;

import com.stsc4j.lexer.TokenType;
import com.stsc4j.parser.v1.ast.Expression;
import com.stsc4j.parser.v1.ast.ExpressionConsoleIn;
import com.stsc4j.parser.v1.ast.ExpressionLiteral;
import com.stsc4j.parser.v1.exception.ParserException;

import static com.stsc4j.lexer.TokenType.*;

/**
 * Maneja la pedida de datos por consola en quetzal 'consola.pedir("Nombre") y consola.pedir_secreto("Contrasena")'
 */
public class ParserConsoleIn extends Parser {

    private final TokenStream tokenStream;
    private final ParserExpression parserExpression;

    public ParserConsoleIn(TokenStream tokenStream, ParserExpression parserExpression) {
        this.tokenStream = tokenStream;
        this.parserExpression = parserExpression;
    }

    @Override
    public Expression parseExpression() {
        tokenStream.consume(TokenType.C_CONSOLE, "Se esperaba una llamada a la clase 'consola'");
        tokenStream.consume(DOT, "Se esperaba un '.' después de 'consola' para acceder a sus funciones");

        if (tokenStream.notMatch(F_SCANNER, F_SCANNER_SECRET)) {
            throw new ParserException(
                    String.format(
                            "La función %s, no esta definida en la clase 'consola' para retornar un valor de tipo expresión",
                            tokenStream.before())
            );
        }

        tokenStream.consume(LEFT_PARENT, "Se esperaba un apertura de parentesis '(' despues de la llamada de la función");
        Expression expression = parserExpression.parseExpression();
        tokenStream.consume(RIGHT_PARENT, "Se esperaba un cierre de parentesis ')' despues de la llamada de la función");

        if (expression instanceof ExpressionLiteral){
            ExpressionLiteral literal = (ExpressionLiteral) expression;
            if (!(literal.token.type.equals(TokenType.LIT_STRING))){
                throw new ParserException("Se esperaba dentro de los parentesis una literal de tipo cadena para la entrada de datos");
            }
        }else {
            throw new ParserException("Se esperaba dentro de los parentesis una literal de tipo cadena para la entrada de datos");
        }
        return new ExpressionConsoleIn((ExpressionLiteral) expression);
    }
}
