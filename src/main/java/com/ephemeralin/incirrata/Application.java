package com.ephemeralin.incirrata;

import com.ephemeralin.incirrata.analysis.AnalysisResult;
import com.ephemeralin.incirrata.analysis.TextAnalyzer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;

public class Application {

    public static void main(String[] args) throws IOException {
        System.out.println(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_TIME));
        TextAnalyzer analyzer = new TextAnalyzer.Builder(args[0])
                .withNumberOfPlacesForWordsTop(5)
                .withWordSizesForMostFrequentTop(new HashSet<>() {{
                    add(8);
                    add(9);
                    add(10);
                }})
                .withTextBeginLine("*** START OF THIS PROJECT GUTENBERG EBOOK")
                .withTextEndLine("*** END OF THIS PROJECT GUTENBERG EBOOK")
                .build();
        AnalysisResult analysisResult = analyzer.analyze();
        ResultOutputProcessor resultOutputProcessor = new ResultOutputProcessorImpl();
        resultOutputProcessor.out(analysisResult);

        System.out.println(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_TIME));
    }


}
