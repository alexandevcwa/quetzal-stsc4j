package com.stsc4j.parser.ast;

import com.stsc4j.lexer.Token;
import com.stsc4j.lexer.TokenType;
import com.stsc4j.lexer.strategy.Classifier;
import com.stsc4j.lexer.strategy.ClassifierKeywords;
import com.stsc4j.parser.declaration.BinaryExpression;
import com.stsc4j.parser.declaration.LiteralExpression;
import com.stsc4j.parser.declaration.VarDeclaration;
import com.stsc4j.parser.declaration.VariableExpression;

import java.util.ArrayList;
import java.util.List;

import static com.stsc4j.lexer.strategy.LexerDictionary.*;

public class Parser {

    private final List<Token> tokens;
    private int current = 0;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    private Token peek() {
        return tokens.get(current);
    }

    private boolean isAtEnd() {
        return peek().getType() == TokenType.EOF;
    }

    private Token consume(TokenType type, String msg) {
        if (peek().getType() == type) return tokens.get(current++);
        throw new RuntimeException(msg + " se encontró " + peek());
    }

    private boolean match(TokenType type) {
        if (peek().getType() == type) {
            current++;
            return true;
        }
        return false;
    }

    private final Classifier C_KEYWORDS = new ClassifierKeywords();

    private TypeInfo parseType() {
        String typeName = consumeTypeKeyword();

        TypeInfo generic = null;
        if (typeName.equals(LIST) && match(TokenType.LESS_THAN)) {
            generic = parseType();
            consume(TokenType.GREATER_THAN, "Falta '>' cierre de genérico");
        }
        return new TypeInfo(typeName, generic);
    }

    private String consumeTypeKeyword(){
        if(match(TokenType.PRIMITIVE_LONG)) return ENTERO;
        if(match(TokenType.PRIMITIVE_INT)) return ENTERO;
        if(match(TokenType.PRIMITIVE_SHORT)) return ENTERO;
        if(match(TokenType.PRIMITIVE_DOUBLE)) return NUMERO;
        if(match(TokenType.PRIMITIVE_FLOAT)) return NUMERO;
        if(match(TokenType.PRIMITIVE_STRING)) return TEXTO;
        if(match(TokenType.PRIMITIVE_BOOLEAN)) return LOG;
        throw new RuntimeException("Se esperaba un tipo válido");
    }

    public List<Statement> parse() {
        List<Statement> statements = new ArrayList<>();
        while (!isAtEnd()) {
            statements.add(parseDeclaration());
        }
        return statements;
    }

    private Statement parseDeclaration() {

        TypeInfo type = parseType();

        boolean isMutable = match(TokenType.MUTABLE_VARIABLE);

        Token id = consume(TokenType.IDENTIFIER, "Se esperaba un identificador");

        if (match(TokenType.LEFT_PARENT)) {
            return null;
        }

        return parseVarDeclaration(type, isMutable, id.getLexeme());
    }

    private VarDeclaration parseVarDeclaration(TypeInfo type, boolean isMutable, String name) {
        consume(TokenType.EQUAL, "Falta =");
        Expression init = parseExpression();
        return new VarDeclaration(type, isMutable, name, init);
    }

    private Expression parseExpression() {
        return parseTerm();
    }

    private Expression parseTerm() {
        Expression left = parsePrimary();
        while (match(TokenType.PLUS)) {
            Expression right = parsePrimary();
            left = new BinaryExpression(left, right, "+");
        }
        return left;
    }

    private Expression parsePrimary() {
        if (match(TokenType.LIT_LONG))
            return new LiteralExpression(Long.parseLong(tokens.get(current - 1).getLexeme()));
        if (match(TokenType.LIT_INT))
            return new LiteralExpression(Integer.parseInt(tokens.get(current - 1).getLexeme()));
        if (match(TokenType.LIT_SHORT))
            return new LiteralExpression(Short.parseShort(tokens.get(current - 1).getLexeme()));
        if (match(TokenType.LIT_DOUBLE))
            return new LiteralExpression(Double.parseDouble(tokens.get(current - 1).getLexeme()));
        if (match(TokenType.LIT_FLOAT))
            return new LiteralExpression(Float.parseFloat(tokens.get(current - 1).getLexeme()));
        if (match(TokenType.LIT_STRING)) return new LiteralExpression(tokens.get(current - 1).getLexeme());
        if (match(TokenType.LIT_TRUE)) return new LiteralExpression(true);
        if (match(TokenType.LIT_FALSE)) return new LiteralExpression(false);
        if (match(TokenType.IDENTIFIER)) return new VariableExpression(tokens.get(current - 1).getLexeme());

        if (match(TokenType.LEFT_BRACKET)) {
            List<Expression> elements = new ArrayList<>();
            if (!check(TokenType.LEFT_BRACKET)) {
                do {
                    elements.add(parseExpression());
                } while (match(TokenType.COMMA));
            }
        }

        throw new RuntimeException("Expresión no esperada: " + peek());
    }

    private boolean check(TokenType type) {
        return peek().getType() == type;
    }


}
