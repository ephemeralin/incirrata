package com.ephemeralin.incirrata.analysis;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class TextAnalyzer {

    private static final String WORDS_SPLITTERATOR = "\\W+";
    private static final Set<String> ONE_LETTER_WORDS = new HashSet<>(Arrays.asList("i", "a"));

    private int readerBufferSize;
    private String filePath;
    private AnalysisResult analysisResult;
    private Set<Integer> wordSizesForMostFrequentTop;
    private Map<Integer, Map<String, Integer>> fixedLengthWords;
    private Map<Character, Integer> lettersMap;
    private int numberOfPlacesForWordsTop;
    private String textBeginLine;
    private String textEndLine;

    private TextAnalyzer(Builder builder) {
        this.filePath = builder.filePath;
        this.numberOfPlacesForWordsTop = builder.numberOfPlacesForWordsTop;
        this.wordSizesForMostFrequentTop = builder.wordSizesForMostFrequentTop;
        this.readerBufferSize = builder.readerBufferSize;
        this.fixedLengthWords = new HashMap<>();
        for (Integer size : wordSizesForMostFrequentTop) {
            fixedLengthWords.put(size, new HashMap<>());
        }
        this.analysisResult = new AnalysisResult();
        this.lettersMap = new HashMap<>();
        this.textBeginLine = builder.textBeginLine;
        this.textEndLine = builder.textEndLine;
    }

    public AnalysisResult analyze() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath), readerBufferSize);
        boolean canRead = false;
        try (reader) {
            String line = reader.readLine();
            while (line != null) {
                if (!canRead) {
                    canRead = isCanStartRead(line);
                } else {
                    if (textEndLine != null && !textEndLine.isEmpty()) {
                        if (line.contains(textEndLine)) {
                            break;
                        }
                    }
                    charactersAnalysis(line);
                    wordsAnalysis(line);
                }
                line = reader.readLine();
            }
        }
        determineTopLetters();
        collectTopWordsLists();

        return analysisResult;
    }

    private void determineTopLetters() {
        analysisResult.mostPopularLetter = findMostPopularLetter();
        analysisResult.leastPopularLetter = findLeastPopularLetter();
    }

    private void wordsAnalysis(String line) {
        String[] wordsOfLine = line.split(WORDS_SPLITTERATOR);
        for (String w : wordsOfLine) {
            String word = w.toLowerCase();
            if (isNotWord(word)) {
                continue;
            }
            analysisResult.numberOfWords = analysisResult.numberOfWords + 1;
            int wordLength = word.length();
            if (wordLength > 2) {
                if (wordSizesForMostFrequentTop.contains(wordLength)) {
                    fixedLengthWords.get(wordLength).merge(word, 1, Integer::sum);
                }
            }
        }
    }

    private void collectTopWordsLists() {
        for (Integer wordSize : wordSizesForMostFrequentTop) {
            Map<String, Integer> wordsWithFrequency = fixedLengthWords.get(wordSize);
            Map<String, Integer> topMostPopularWordsForWordSize = findTopMostPopularWords(wordsWithFrequency);
            analysisResult.topFixedLengthWords.put(wordSize, topMostPopularWordsForWordSize);
        }
    }

    private void charactersAnalysis(String line) {
        for (char ch : line.toCharArray()) {
            analysisResult.numberOfChars++;
            if ((ch >= 65 && ch <= 90) || (ch >= 97 && ch <= 122)) {
                char ch_lower = Character.toLowerCase(ch);
                lettersMap.merge(ch_lower, 1, Integer::sum);
                analysisResult.numberOfLetters++;
            } else if ((ch >= 48 && ch <= 57)) {
                analysisResult.numberOfDigits++;
            }
        }
        analysisResult.numberOfChars++; //line separator
    }

    private boolean isNotWord(String word) {
        return word.isEmpty() || (word.length() == 1 && !ONE_LETTER_WORDS.contains(word));
    }

    private char findMostPopularLetter() {
        Map.Entry<Character, Integer> result =
                lettersMap.entrySet().stream().max(Map.Entry.comparingByValue()).orElse(null);
        if (result != null) {
            return result.getKey();
        } else {
            return 0;
        }
    }

    private char findLeastPopularLetter() {
        Map.Entry<Character, Integer> result =
                lettersMap.entrySet().stream().min(Map.Entry.comparingByValue()).orElse(null);
        if (result != null) {
            return result.getKey();
        } else {
            return 0;
        }
    }

    private Map<String, Integer> findTopMostPopularWords(Map<String, Integer> words) {
        return words.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(numberOfPlacesForWordsTop)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private List<String> findTopLeastPopularWords(Map<String, Integer> words) {
        return words.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .limit(numberOfPlacesForWordsTop)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    private boolean isCanStartRead(String line) {
        boolean canRead = false;
        if (textBeginLine != null && !textBeginLine.isEmpty()) {
            if (line.contains(textBeginLine)) {
                canRead = true;
            }
        } else {
            canRead = true;
        }
        return canRead;
    }

    public static class Builder {

        private String filePath;
        private int numberOfPlacesForWordsTop;
        private Set<Integer> wordSizesForMostFrequentTop;
        private int readerBufferSize;
        private String textBeginLine;
        private String textEndLine;

        public Builder(String filePath) {
            this.filePath = filePath;
            this.numberOfPlacesForWordsTop = 5;
            this.wordSizesForMostFrequentTop = new HashSet<>() {{
                add(8);
            }};
            this.readerBufferSize = 8 * 1024;
        }

        public Builder withNumberOfPlacesForWordsTop(int numberOfPlacesForWordsTop) {
            this.numberOfPlacesForWordsTop = numberOfPlacesForWordsTop;
            return this;
        }

        public Builder withWordSizesForMostFrequentTop(Set<Integer> wordSizesForMostFrequentTop) {
            this.wordSizesForMostFrequentTop = wordSizesForMostFrequentTop;
            return this;
        }

        public Builder withReaderBufferSize(int readerBufferSize) {
            this.readerBufferSize = readerBufferSize;
            return this;
        }

        public Builder withTextBeginLine(String textBeginLine) {
            this.textBeginLine = textBeginLine;
            return this;
        }

        public Builder withTextEndLine(String textEndLine) {
            this.textEndLine = textEndLine;
            return this;
        }

        public TextAnalyzer build() {
            return new TextAnalyzer(this);
        }
    }
}
