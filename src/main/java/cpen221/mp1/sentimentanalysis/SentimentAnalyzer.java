package cpen221.mp1.sentimentanalysis;

import cpen221.mp1.ngrams.NGrams;
import cpen221.mp1.ratemyprofessor.DataAnalyzer;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SentimentAnalyzer {

    // list of word maps for each review, mapping word to number of occurrences
    private List<Map<String, Long>> allBagOfWords;

    /* mapping ratings to the word map for that rating,
       the latter mapping each word to number of occurrences */
    private Map<Double, Map<String, Long>> wordUsesAtRating;

    // mapping rating to the number of reviews with that rating
    private Map<Double, Long> ratingCount;
    private int totalReviews = 0;
    private int totalWords = 0;

    private final double MIN_RATING = 1;
    private final double MAX_RATING = 5;
    private final double RATING_INCREMENT = 0.5;

    /**
     * Create a SentimentAnalyzer object to analyze a RateMyProfessor dataset.
     *
     * @param filename File to read review data from and build training model,
     *                not null and file exists, of RateMyProfessor format.
     * @throws FileNotFoundException
     */
    public SentimentAnalyzer(final String filename) throws FileNotFoundException {

        String[] fileLines = getReviewStringArray(filename);

        /* turn array of String reviews into a list of reviews,
        each containing a list of words (Strings) */
        NGrams wordNGrams = new NGrams(fileLines);
        List<List<String>> reviewWordList = wordNGrams.getLineList();

        allBagOfWords = new ArrayList<>();
        wordUsesAtRating = new HashMap<>();
        ratingCount = new HashMap<>();

        for (int i = 0; i < reviewWordList.size(); i++) {
            List<String> line = reviewWordList.get(i);
            Map<String, Long> currentLineMap = new HashMap<>();

            double rating = Float.parseFloat(fileLines[i].substring(0, 3));

            incrementDoubleLongMap(ratingCount, rating);
            totalReviews++;

            for (int j = 0; j < line.size(); j++) {
                String currentWord = line.get(j);
                incrementStringLongMap(currentLineMap, currentWord);
                updateWordUseMap(rating, currentWord);
                totalWords++;
            }

            allBagOfWords.add(currentLineMap);
        }
    }

    /**
     * Process a RateMyProfessor file to return an array of reviews.
     *
     * @param filename File to read review data from and build
     *                 training model, not null and file exists,
     *                 of RMP format.
     * @return Returns array of reviews as Strings in the order
     *         of the file.
     * @throws FileNotFoundException
     */
    private String[] getReviewStringArray(final String filename)
            throws FileNotFoundException {
        String[] fileLines;
        try {
            DataAnalyzer fileReader = new DataAnalyzer(filename);
            List<String> fileLineList = fileReader.getLines();

            fileLines = new String[fileLineList.size()];
            fileLines = fileLineList.toArray(fileLines);

        } catch (Exception e) {
            throw new FileNotFoundException();
        }
        return fileLines;
    }

    /**
     * Add word to corresponding map and rating in wordUsesAtRating.
     *
     * @param rating The rating of the review where the word was found.
     * @param currentWord The word to update in the map.
     */
    private void updateWordUseMap(final double rating, final String currentWord) {

        Map<String, Long> wordUseMap = wordUsesAtRating.get(rating);
        if (wordUseMap == null) {
            Map<String, Long> newMap = new HashMap<>();
            wordUsesAtRating.put(rating, newMap);
            newMap.put(currentWord, 1L);
        } else {
            incrementStringLongMap(wordUseMap, currentWord);
        }

    }

    /**
     * Increment the {@code Long} value mapped to {@code String} key in
     * {@code map}.
     *
     * @param map {@code <String, Long>} map to modify.
     * @param key {@code String} key corresponding to value to increment.
     */
    private void incrementStringLongMap(final Map<String, Long> map,
                                        final String key) {
        long currentValue = map.getOrDefault(key, 0L);
        map.put(key, currentValue + 1);
    }

    /**
     * Increment the {@code Long} value mapped to {@code Double} key in
     * {@code map}.
     *
     * @param map {@code <Double, Long>} map to modify.
     * @param key {@code Double} key corresponding to value to increment.
     */
    private void incrementDoubleLongMap(final Map<Double, Long> map,
                                        final double key) {
        long currentValue = map.getOrDefault(key, 0L);
        map.put(key, currentValue + 1);
    }

    /**
     * Return the most probable RateMyProfessor rating for a given review
     * String.
     *
     * @param reviewText The review String to be analyzed.
     * @return The predicted rating of the given review.
     */
    public float getPredictedRating(final String reviewText) {

        float maxProbability = 0;
        float predictedRating = 1;

        for (double rating = MIN_RATING; rating <= MAX_RATING;
             rating += RATING_INCREMENT) {

            float ratingProbability =  getProbability(rating, reviewText);
            if (ratingProbability > maxProbability) {
                predictedRating = (float) rating;
                maxProbability = ratingProbability;
            }

        }

        return predictedRating;
    }

    /**
     * Compute the Bayesian probability that a review is of a certain
     * rating. Note that this returns a distribution, which is not
     * necessarily between 0 and 1.
     *
     * @param rating The rating to compute the review's probability.
     * @param review The review text to be analyzed.
     * @return The Bayesian probability distribution of the review
     * being at the given rating.
     */
    private float getProbability(final double rating, final String review) {

        if (ratingCount.get(rating) == null) {
            return 0;
        }

        double totalNumWordAtRating = 0;
        Map<String, Long> ratingWordMap = wordUsesAtRating.get(rating);
        for (String ratingWord: ratingWordMap.keySet()) {
            totalNumWordAtRating += ratingWordMap.get(ratingWord);
        }

        double pRating = ratingCount.get(rating) / (float) totalReviews;
        double pBagOfWords = 1;
        double pBagOfWordsAtRating = 1;

        //get the list of words in the review
        String[] reviewArray = {review};
        NGrams words = new NGrams(reviewArray);
        List<List<String>> wordList = words.getLineList();

        for (String word: wordList.get(0)) {
            long currentWordOccurrences = 0L;

            for (Map map: allBagOfWords) {
                currentWordOccurrences += (long) map.getOrDefault(word, 0L);
            }

            long wordOccurrencesAtRating =
                    wordUsesAtRating.get(rating).getOrDefault(word, 0L);
            double pCurrentWordAtRating =
                    (wordOccurrencesAtRating + 1) / (totalNumWordAtRating + 1);
            double pCurrentWord = currentWordOccurrences / (double) totalWords;

            pBagOfWords *= pCurrentWord;
            pBagOfWordsAtRating *= pCurrentWordAtRating;
        }

        float pRatingForBagOfWords =
                (float) (pBagOfWordsAtRating * pRating / pBagOfWords);
        return pRatingForBagOfWords;
    }

}
