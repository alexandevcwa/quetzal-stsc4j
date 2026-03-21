package com.stsc4j.parser.v1;

import com.stsc4j.lexer.Token;
import com.stsc4j.lexer.TokenType;

import java.util.List;

public class Parser {

    private final List<Token> tokens;
    private int current = 0;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    // Método principal para iniciar el parseo de una expresión
    public Expr expression(){
        return term();
    }

    // Maneja multiplicaciones y divisiones
    public Expr term(){
        Expr expr = factor();

        while (!match(TokenType.EOF)){
            Token operator = consume(TokenType.MULTIPLY, TokenType.DIVIDE, TokenType.PLUS, TokenType.MINUS);
            Expr right = factor();
            expr = new Binary(expr, operator, right);
        }
        return expr;
    }

    // Maneja los componentes más básicos: literales y paréntesis
    private Expr factor(){
        if(match(TokenType.LIT_TRUE)) return new Literal(true);
        if(match(TokenType.LIT_FALSE)) return new Literal(false);
        if(match(TokenType.NULL)) return new Literal(null);

        if(match(TokenType.LIT_INTEGER, TokenType.LIT_DECIMAL, TokenType.LIT_STRING)){
            return new Literal(previous().getLexeme());
        }

        if(match(TokenType.LEFT_PARENT)){
            Expr expr = expression();
            consume(TokenType.RIGHT_PARENT,"Se esperaba ')' después de la expresión.");
            return expr;
        }

        throw new RuntimeException("Se esperaba una expresión.");

    }

    private boolean match(TokenType... t){
        for(TokenType tt : t){
            if(check(tt)){
                advance();
                return true;
            }
        }
        return false;
    }

    private boolean check(TokenType t){
        if(isAtEnd()) return false;
        return peek().getType() == t;
    }

    private Token advance(){
        if(!isAtEnd()) current++;
        return previous();
    }

    private Token peek(){
        return tokens.get(current);
    }

    private Token previous(){
        return tokens.get(current - 1);
    }

    private Token consume(TokenType t, String message){
         if(check(t)) return advance();
         throw new RuntimeException(message);
    }

    private Token consume(TokenType... t){
        for(TokenType tt : t){
            if(check(tt)) return advance();
        }
        throw new RuntimeException();
    }

    private boolean isAtEnd(){
        return peek().getType() == TokenType.EOF;
    }

}
