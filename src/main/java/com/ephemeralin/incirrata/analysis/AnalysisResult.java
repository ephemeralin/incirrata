package com.ephemeralin.incirrata.analysis;

import java.util.HashMap;
import java.util.Map;

public class AnalysisResult {

    Map<Integer, Map<String, Integer>> topFixedLengthWords = new HashMap<>();
    long numberOfChars = 0;
    long numberOfLetters = 0;
    long numberOfDigits = 0;
    long numberOfWords = 0;
    long numberOfSentences = 0;
    char mostPopularLetter = 0;
    char leastPopularLetter = 0;

    public Map<Integer, Map<String, Integer>> getTopFixedLengthWords() {
        return topFixedLengthWords;
    }

    public long getNumberOfChars() {
        return numberOfChars;
    }

    public long getNumberOfLetters() {
        return numberOfLetters;
    }

    public long getNumberOfDigits() {
        return numberOfDigits;
    }

    public long getNumberOfWords() {
        return numberOfWords;
    }

    public long getNumberOfSentences() {
        return numberOfSentences;
    }

    public char getMostPopularLetter() {
        return mostPopularLetter;
    }

    public char getLeastPopularLetter() {
        return leastPopularLetter;
    }
}
