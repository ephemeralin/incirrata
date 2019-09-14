package com.ephemeralin.incirrata;

import com.ephemeralin.incirrata.analysis.AnalysisResult;

import java.util.Map;

public class ResultOutputProcessorImpl implements ResultOutputProcessor {

    @Override
    public void out(AnalysisResult result) {
        System.out.println("~~~~~~~~~~~~~~~~~ Statistics ~~~~~~~~~~~~~~~~~ ");
        System.out.println("  * Characters: ");
        System.out.println("    - number of characters total: " + result.getNumberOfChars());
        System.out.println("    - number of letters: " + result.getNumberOfLetters());
        System.out.println("    - number of digits: " + result.getNumberOfDigits());
        System.out.println("    - the most popular letter: " + result.getMostPopularLetter());
        System.out.println("    - the least popular letter: " + result.getLeastPopularLetter());

        System.out.println("  * Words: ");
        System.out.println("    - number of words: " + result.getNumberOfWords());
        for (Map.Entry<Integer, Map<String, Integer>> entry : result.getTopFixedLengthWords().entrySet()) {
            String topWords = formatTopWordsWithFrequency(entry.getValue());
            System.out.println(String.format("    - top %d-letters words: %s", entry.getKey(), topWords));
        }
        //todo
        System.out.println("  * Sentences: ");
        System.out.println("    - number of sentences: " + result.getNumberOfSentences());
        System.out.println("~~~~~~~~~~~~~~~~~ End ~~~~~~~~~~~~~~~~~ ");

    }

    private String formatTopWordsWithFrequency(Map<String, Integer> topWords) {
        StringBuilder formattedString = new StringBuilder();
        for (Map.Entry<String, Integer> entry : topWords.entrySet()) {
            formattedString.append(String.format("%s (%d), ", entry.getKey(), entry.getValue()));
        }
        return formattedString.toString();
    }
}
