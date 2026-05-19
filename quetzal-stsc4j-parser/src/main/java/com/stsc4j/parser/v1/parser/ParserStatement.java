package com.stsc4j.parser.v1.parser;

public class ParserStatement {
    private final TokenStream tokenStream;

    // Required Parsers
    private final ParserPrincipal parserPrincipal;
    private final ParserExpression parserExpression;

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
    private ParserCall parserCall;
    private ParserTryCatchFinally parserTryCatchFinally;
    private ParserConsoleOut parserConsoleOut;
    private ParserContinue parserContinue;
    private ParserBreak parserBreak;
    private ParserThrow parserThrow;


    public ParserStatement(TokenStream tokenStream, ParserPrincipal parserPrincipal) {
        this.tokenStream = tokenStream;
        this.parserExpression = new ParserExpression(tokenStream);
        this.parserPrincipal = parserPrincipal;
    }

    public Parser parseVar() {
        if (parserDeclaration == null) {
            parserDeclaration = new ParserDeclaration(tokenStream, parserExpression);
        }
        return parserDeclaration;
    }

    public Parser parseIf() {
        if (parserIf == null) {
            parserIf = new ParserIf(tokenStream, parserExpression, (ParserBlock) parseBlock());
        }
        return parserIf;
    }

    public Parser parseList() {
        if (parserList == null) {
            parserList = new ParserList(tokenStream, parserExpression);
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
            parserJsn = new ParserJsn(tokenStream, parserExpression);
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
            parserReturn = new ParserReturn(parserExpression, tokenStream);
        }
        return parserReturn;
    }

    public Parser parseLoopWhile() {
        if (parserLoopWhile == null) {
            parserLoopWhile = new ParserLoopWhile(tokenStream, parserExpression, (ParserBlock) parseBlock());
        }
        return parserLoopWhile;
    }

    public Parser parseLoopDoWhile() {
        if (parserLoopDoWhile == null) {
            parserLoopDoWhile = new ParserLoopDoWhile(tokenStream, parserExpression, (ParserBlock) parseBlock());
        }
        return parserLoopDoWhile;
    }

    public Parser parseIncremental() {
        if (parserIncremental == null) {
            parserIncremental = new ParserIncremental(parserExpression);
        }
        return parserIncremental;
    }

    public Parser parseLoopForEach() {
        if (parserLoopForEach == null) {
            parserLoopForEach = new ParserLoopForEach(tokenStream, parserExpression, (ParserBlock) parseBlock());
        }
        return parserLoopForEach;
    }

    public Parser parseLoopFor() {
        if (parserLoopFor == null) {
            parserLoopFor = new ParserLoopFor(tokenStream, parserDeclaration, parserExpression, (ParserBlock) parseBlock());
        }
        return parserLoopFor;
    }

    public Parser parseMatrixAssignation() {
        if (parserMatrixAssignation == null) {
            parserMatrixAssignation = new ParserMatrixAssignation(tokenStream, parserExpression);
        }
        return parserMatrixAssignation;
    }

    public Parser parseCall() {
        if (parserCall == null) {
            parserCall = new ParserCall(tokenStream, parserExpression);
        }
        return parserCall;
    }

    public Parser parseTryCatchFinally() {
        if (parserTryCatchFinally == null) {
            parserTryCatchFinally = new ParserTryCatchFinally((ParserBlock) parseBlock(), parserExpression, tokenStream);
        }
        return parserTryCatchFinally;
    }

    public Parser parseConsoleOut() {
        if (parserConsoleOut == null) {
            parserConsoleOut = new ParserConsoleOut(tokenStream, parserExpression);
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

    public Parser parseThrow(){
        if(parserThrow == null){
            parserThrow = new ParserThrow(tokenStream, parserExpression);
        }
        return parserThrow;
    }
}
