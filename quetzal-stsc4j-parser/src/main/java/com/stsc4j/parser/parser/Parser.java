package com.stsc4j.parser.parser;

import com.stsc4j.lexer.Token;
import com.stsc4j.parser.ast.statement.Statement;

import java.util.ArrayList;
import java.util.List;

public class Parser {
    private final ParserContext context;
    private final StatementParser statementParser;

    public Parser(List<Token> tokens) {
        this.context = new ParserContext(tokens);
        this.statementParser = new StatementParser(context);
    }

    public List<Statement> parse() {
        List<Statement> statements = new ArrayList<>();
        while (!context.isAtEnd()) {
            Statement stmt = statementParser.parseDeclaration();
            if (stmt != null) {
                statements.add(stmt);
            }
        }
        return statements;
    }

    public List<String> getErrors() {
        return context.errors;
    }

}
