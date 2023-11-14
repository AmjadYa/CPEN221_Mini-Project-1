package cpen221.mp1.ngrams;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NGrams {

    // contains the list of words for each review, all in order
    private List<List<String>> lineList = new ArrayList<>();

    /**
     * Create an NGrams object.
     *
     * @param text all the text to analyze and create n-grams from;
     *             is not null and is not empty
     */
    public NGrams(final String[] text) {

        for (String string: text) {
            List<String> currentLine = processLine(string);

            String[] wordsArray = new String[currentLine.size()];
            currentLine.toArray(wordsArray);

            lineList.add(currentLine);
        }
    }

    /**
     * Turns a {@code String} into a {@code List} of {@code String}s
     * that are individuals words.
     *
     * @param string the text to process
     * @return the string's list of words
     */
    private List<String> processLine(final String string) {

        List<String> line = new ArrayList<>();
        BreakIterator wb = BreakIterator.getWordInstance();
        wb.setText(string);

        int start = wb.first();

        for (int end = wb.next();
             end != BreakIterator.DONE;
             start = end, end = wb.next()) {

            String word = string.substring(start, end).toLowerCase();

            word = word.replaceAll("^\\s*\\p{Punct}+\\s*", "")
                    .replaceAll("\\s*\\p{Punct}+\\s*$", "");
            if (goodWord(word)) {
                line.add(word);
            }
        }
        return line;
    }

    /**
     * Returns false if a String is empty or only contains spaces, true
     * otherwise.
     *
     * @param word String to check for validity.
     * @return a boolean indicating if the String is a non-empty word
     */
    private boolean goodWord(final String word) {
        return !(word.trim().equals(""));
    }

    /**
     * Obtain the total number of unique 1-grams,
     * 2-grams, ..., n-grams.
     *
     * Specifically, if there are m_i i-grams,
     * obtain sum_{i=1}^{n} m_i.
     *
     * @param n the limit of ngram word length, must be positive
     * @return the total number of 1-grams,
     * 2-grams, ..., n-grams
     */
    public long getTotalNGramCount(final int n) {

        List<Map<String, Long>> bigList = getAllNGrams();

        int totalNGram = 0;

        for (int i = 0; i < n; i++) {
            totalNGram += bigList.get(i).size();
        }

        return totalNGram;
    }

    /**
     * Get the n-grams, as a List, with the i-th entry being
     * all the (i+1)-grams and their counts.
     *
     * @return a list of n-grams and their associated counts,
     * with the i-th entry being all the (i+1)-grams and their counts
     */
    public List<Map<String, Long>> getAllNGrams() {

        int ngramLength = 1;
        boolean foundNGram = true;

        List<Map<String, Long>> nGramMapList = new ArrayList<>();

        while (foundNGram) {
            foundNGram = false;

            HashMap<String, Long> currentMap = new HashMap<>();

            for (List line: lineList) {
                for (int i = 0; i < line.size(); i++) {
                    if (i + ngramLength - 1 >= line.size()) {
                        break;
                    }

                    foundNGram = true;

                    String currentNGram = createNGram(ngramLength, i, line);

                    long currentMapCount =
                            currentMap.getOrDefault(currentNGram, 0L);
                    currentMap.put(currentNGram, currentMapCount + 1);
                }
            }

            if (foundNGram) {
                nGramMapList.add(currentMap);
            }
            ngramLength++;
        }

        return nGramMapList;
    }

    /**
     * Creates a String of an NGram with ngramLength words,
     * starting from index currentWord in a list of words.
     *
     * @param ngramLength number of words in the NGram String
     * @param currentWord the index in the list of words
     * @param line the list of words to create the NGram from
     * @return NGram String with the length and at the location
     * specified.
     */
    private String createNGram(final int ngramLength,
                               final int currentWord, final List<String> line) {
        String currentNGram = "";

        for (int j = 0; j < ngramLength; j++) {
            currentNGram += line.get(currentWord + j);
            if (j != ngramLength - 1) {
                currentNGram += " ";
            }
        }
        return currentNGram;
    }

    /**
     * Returns a copy of {@code lineList}.
     * Used by other classes to parse a file into words.
     *
     * @return a list containing the list of words for each review, all in order
     */
    public List<List<String>> getLineList() {
        List<List<String>> returnList = new ArrayList<>();
        for (List line: lineList) {
            List<String> currentLine = new ArrayList<>();
            for (int i = 0; i < line.size(); i++) {
                currentLine.add((String) line.get(i));
            }
            returnList.add(currentLine);
        }
        return returnList;
    }

}
