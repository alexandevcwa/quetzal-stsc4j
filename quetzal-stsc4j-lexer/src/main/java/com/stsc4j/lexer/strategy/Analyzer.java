package com.stsc4j.lexer.strategy;

import com.stsc4j.lexer.TokenType;

import java.util.List;

public class Analyzer {
    private List<Classifier> classifiers;

    public Analyzer(List<Classifier> classifiers) {
        this.classifiers = classifiers;
    }

    public TokenType analyze(String s) {
        for (Classifier classifier : classifiers) {
            if (classifier.match(s)) {
                return classifier.classify(s);
            }
        }
        return TokenType.UNKNOW;
    }
}
