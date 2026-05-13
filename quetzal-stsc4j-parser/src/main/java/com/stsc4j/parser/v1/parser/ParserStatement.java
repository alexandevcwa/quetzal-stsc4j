package com.stsc4j.parser.v1.parser;

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
    private ParserLoopDoWhile parserLoopDoWhile;
    private ParserLoopFor parserLoopFor;
    private ParserLoopForEach parserLoopForEach;
    private ParserIncremental parserIncremental;
    private ParserMatrixAssignation parserMatrixAssignation;
    private ParserTryCatchFinally parserTryCatchFinally;
    private ParserConsoleOut parserConsoleOut;
    private ParserContinue parserContinue;
    private ParserBreak parserBreak;


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
        return parserLoopWhile;
    }

    public Parser parseLoopDoWhile() {
        if (parserLoopDoWhile == null) {
            parserLoopDoWhile = new ParserLoopDoWhile(tokenStream, parserExpressions, (ParserBlock) parseBlock());
        }
        return parserLoopDoWhile;
    }

    public Parser parseIncremental() {
        if (parserIncremental == null) {
            parserIncremental = new ParserIncremental(parserExpressions);
        }
        return parserIncremental;
    }

    public Parser parseLoopForEach() {
        if (parserLoopForEach == null) {
            parserLoopForEach = new ParserLoopForEach(tokenStream, parserExpressions, (ParserBlock) parseBlock());
        }
        return parserLoopForEach;
    }

    public Parser parseLoopFor() {
        if (parserLoopFor == null) {
            parserLoopFor = new ParserLoopFor(tokenStream, parserDeclaration, parserExpressions, (ParserBlock) parseBlock());
        }
        return parserLoopFor;
    }

    public Parser parseMatrixAssignation() {
        if (parserMatrixAssignation == null) {
            parserMatrixAssignation = new ParserMatrixAssignation(tokenStream, parserExpressions);
        }
        return parserMatrixAssignation;
    }

    public Parser parseTryCatchFinally() {
        if (parserTryCatchFinally == null) {
            parserTryCatchFinally = new ParserTryCatchFinally((ParserBlock) parseBlock(), parserExpressions, tokenStream);
        }
        return parserTryCatchFinally;
    }

    public Parser parseConsoleOut() {
        if (parserConsoleOut == null) {
            parserConsoleOut = new ParserConsoleOut(tokenStream, parserExpressions);
        }
        return parserConsoleOut;
    }

    public Parser parseContinue() {
        if (parserContinue == null) {
            parserContinue = new ParserContinue(tokenStream);
        }
        return parserContinue;
    }

    public Parser parseBreak() {
        if (parserBreak == null) {
            parserBreak = new ParserBreak(tokenStream);
        }
        return parserBreak;
    }
}
