package com.stsc4j.parser.v1.parser;

import com.stsc4j.parser.v1.ast.*;


public class ParserStatement {
    private final TokenStream tokenStream;

    // Required Parsers
    private final ParserPrincipal parserPrincipal;
    private final ParserExpressions parserExpressions;

    // Parser for Statement
    private ParserBlock parserBlock;
    private ParserJsn parserJsn;
    private ParserFunction parserFunction;
    private ParserReturn parserReturn;
    private ParserIf parserIf;
    private ParserList parserList;
    private ParserDeclaration parserDeclaration;
    private ParserLoopWhile parserLoopWhile;
    private ParserIncremental parserIncremental;


    public ParserStatement(TokenStream tokenStream, ParserPrincipal parserPrincipal) {
        this.tokenStream = tokenStream;
        this.parserExpressions = new ParserExpressions(tokenStream);
        this.parserPrincipal = parserPrincipal;
    }

    public Parser parseExpressions() {
        return parserExpressions;
    }

    public Parser parseVar() {
        if (parserDeclaration == null) {
            parserDeclaration = new ParserDeclaration(tokenStream, parserExpressions);
        }
        return parserDeclaration;
    }

    public Parser parseIf() {
        if (parserIf == null) {
            parserIf = new ParserIf(tokenStream, parserExpressions, (ParserBlock) parseBlock());
        }
        return parserIf;
    }

    public Parser parseList() {
        if (parserList == null) {
            parserList = new ParserList(tokenStream, parserExpressions);
        }
        return parserList;
    }

    public Parser parseBlock() {
        if (parserBlock == null) {
            parserBlock = new ParserBlock(tokenStream, parserPrincipal);
        }
        return parserBlock;
    }

    public Parser parseJsn() {
        if (parserJsn == null) {
            parserJsn = new ParserJsn(tokenStream, parserExpressions);
        }
        return parserJsn;
    }

    public Parser parseFunction() {
        if (parserFunction == null) {
            parserFunction = new ParserFunction(tokenStream, parserPrincipal);
        }
        return parserFunction;
    }

    public Parser parseReturn() {
        if (parserReturn == null) {
            parserReturn = new ParserReturn(parserExpressions, tokenStream);
        }
        return parserReturn;
    }

    public Parser parseLoopWhile() {
        if (parserLoopWhile == null) {
            parserLoopWhile = new ParserLoopWhile(tokenStream, parserExpressions, (ParserBlock) parseBlock());
        }
        return null;
    }

    public Parser parseIncremental() {
        if (parserIncremental == null) {
            parserIncremental = new ParserIncremental(parserExpressions);
        }
        return parserIncremental;
    }
}
