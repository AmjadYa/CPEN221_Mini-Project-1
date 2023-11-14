package cpen221.mp1.ratemyprofessor;

import cpen221.mp1.datawrapper.DataWrapper;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataAnalyzer {
    private ArrayList<String> lines = new ArrayList<>();

    public List<String> getLines() {
        List<String> newList = new ArrayList<>(lines);
        return newList;
    }

    /**
     * Create an object to analyze a RateMyProfessor dataset.
     * @param dataSourceFileName the name of the file that contains the data
     * @throws FileNotFoundException if the file does not exist or is not found
     */
    public DataAnalyzer(final String dataSourceFileName)
            throws FileNotFoundException {
        DataWrapper dw = new DataWrapper(dataSourceFileName);
        dw.resetScanner();
        String currentline = dw.nextLine();
        currentline = dw.nextLine();
        while (currentline != null) {
            lines.add(currentline);
            currentline = dw.nextLine();
        }
    }

    /**
     * Obtain a histogram with the number of occurrences of the
     * query term in the RMP comments, categorized as men-low (ML),
     * women-low (WL), men-medium (MM), women-medium (WM),
     * men-high (MH), and women-high (WH).
     * @param query the search term, which contains between one and three words
     * @return the histogram with the number of occurrences of the
     * query term in the RMP comments, categorized as men-low (ML),
     * women-low (WL), men-medium (MM), women-medium (WM),
     * men-high (MH), and women-high (WH)
     */
    public Map<String, Long> getHistogram(final String query) {
        HashMap<String, Long> genderRatingCount = new HashMap<>(Map.of(
                "ML", 0L,
                "MM", 0L,
                "MH", 0L,
                "WL", 0L,
                "WM", 0L,
                "WH", 0L
        ));

            for (String line: lines) {

                long count = queryCount(line, query);

                char gender = line.charAt(4);
                char rating = getKey(line);
                String genderRate = "" + gender + rating;
                System.out.println(genderRate);

                genderRatingCount.put(genderRate,
                        genderRatingCount.get(genderRate) + count);
            }

        return genderRatingCount;
    }

    /**
     * Take the rating and gives it a classification
     * @param line the line of the one singular review, can not be null,
     *             line.size() has to be bigger than 3 and the first
     *             three characters must be parsable into a float
     * @return the classification of the rating as either low(L), medium(M)，or high(H)
     */
    private char getKey(final String line) {
        float rating = Float.parseFloat(line.substring(0, 3));
        if (rating <= 2.0) {
            return 'L';
        }
        if (rating > 2.0 && rating <= 3.5) {
            return 'M';
        }
        return 'H';
    }

    /**
     * Find the number of occurrences of the given query within the review
     * @param line the line of the one singular review, can not be null,
     *             line.size() has to be bigger than 6
     * @param query the word that we want to count the occurrences for,
     *              can not be null or empty.
     * @return how many times the given word shows up.
     */
    private long queryCount(final String line, final String query) {

        //add a space to the start and end of the review
        String spaceLine = line.substring(0, 6) + " "
                + line.substring(6) + " ";

        long numQueryCharacters = spaceLine.length()
                - spaceLine.replaceAll(" " + query + " ","").length();
        //this includes the spaces before and after
        long queryLength = query.length() + 2;

        return numQueryCharacters / queryLength;
    }

    private String[] separateBySentence (String line) {

        String[] sentences;
        if(line.contains(".") && line.length() >= 10){
            sentences = line.split(".");
        }
        else{
            sentences = new String[1];
            sentences[0] = line;
        }
        return sentences;
    }
}


