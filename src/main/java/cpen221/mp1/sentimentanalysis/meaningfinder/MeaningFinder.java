package cpen221.mp1.sentimentanalysis.meaningfinder;

import cpen221.mp1.datawrapper.DataWrapper;
import cpen221.mp1.ngrams.NGrams;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class MeaningFinder {

    String DATA_FILE = "data/task5_data.txt";

    /**
     * Create a MeaningFinder object to compute synonyms and similarities
     *
     * @param trainingData the data which will be used to train the algorithm
     * @throws IOException if the file fails to load
     */
    public MeaningFinder(String[] trainingData) throws IOException {
        addDataToFile(trainingData, DATA_FILE);
    }

    /**
     * This method takes in a text string and adds it to a file which acts as
     * the algorithm's memory. Any input here will train the algorithm.
     *
     * Adding the same text string will bias the algorithm to react to inputs
     * based on the repeated author's writing style. Basically... don't repeat.
     *
     * @param text the training data that you would like to add
     * @throws IOException if the file does not exist
     */
    public void addTrainingData(String text) throws IOException {
        String[] textAsArray = new String[] {text};
        addDataToFile(textAsArray, DATA_FILE);
    }

    /**
     * This method will take two sets of text and compute the cosine similarity
     * between them. The cosine similarity computes a word's relationship to
     * a different word, based on the context that the words are used in.
     *
     * @param text1 the set of sentences that contain the first word
     * @param text2 the set of sentences that contain the second word
     * @return the closeness of the words in their surrounding contexts
     */
    public static double cosineSimilarity(String[] text1, String[] text2) {

        NGrams ngrams1 = new NGrams(text1);
        List<List<String>> lineList1 = ngrams1.getLineList();

        NGrams ngrams2 = new NGrams(text2);
        List<List<String>> lineList2 = ngrams2.getLineList();

        Set<String> uniqueWords = new HashSet<>();

        for (List line1: lineList1) {
            for (int i = 0; i < line1.size(); i++) {
                String currentWord = (String) line1.get(i);
                uniqueWords.add(currentWord);
            }
        }

        for (List line2: lineList2) {
            for (int i = 0; i < line2.size(); i++) {
                String currentWord = (String) line2.get(i);
                uniqueWords.add(currentWord);
            }
        }

        int productSummation = 0;
        int squareSummation1 = 0;
        int squareSummation2 = 0;

        for (String word: uniqueWords) {
            int list1occurrences = numOccurrences(lineList1, word);
            int list2occurrences = numOccurrences(lineList2, word);

            productSummation += list1occurrences * list2occurrences;
            squareSummation1 += Math.pow(list1occurrences, 2);
            squareSummation2 += Math.pow(list2occurrences, 2);
        }

        return productSummation / (Math.sqrt(squareSummation1) * Math.sqrt(squareSummation2));
    }

    /**
     *
     * @param allLines all
     * @param curWord
     * @return
     */
    private static int numOccurrences (List< List <String> > allLines, String curWord) {

        int counter = 0;

        for (List line : allLines) {

            counter += Collections.frequency(line, curWord);
        }

        return counter;
    }

    /**
     * Assuming this instance has been trained, return an element
     * from choices that is closest in meaning to word.
     *
     * @param word the word whose similarity you would like to find
     *             cannot contain spaces, cannot be null, cannot be empty
     * @param choices the set of words to choose from that may match the word
     *                cannot contain spaces, cannot be null, cannot be empty
     * @return the option that matches the word most closely
     * @throws IOException if getMarkerString fails to read a file
     * @throws IllegalArgumentException if the word or the choices
     *                          are null or empty, or contain spaces
     */
    public String predictedSynonym(String word, String[] choices) throws IOException {

        if (word.contains(" ") || word == null || word.equals("")){
            throw new IllegalArgumentException();
        }

        for(String choice : choices){
            if (choice.contains(" ") || choice == null || choice.equals("")){
                throw new IllegalArgumentException();
            }
        }

        String[] wordMarkerStrings = getMarkerString(word);
        double maxSimilarity = 0;
        String synonym = choices[0];

        for (String choice: choices) {

            String[] choiceMarkerStrings = getMarkerString(choice);

            double similarity = cosineSimilarity(wordMarkerStrings, choiceMarkerStrings);

            if (similarity > maxSimilarity) {
                synonym = choice;
                maxSimilarity = similarity;
            }

        }

        return synonym;
    }

    /**
     * This method takes a word and searches for all the sentences
     * in the training data which contain the given word. It then
     * isolates these sentences in order to be processed in other methods.
     * The purpose is to be able to parse through the context in which a
     * word is used.
     *
     * @param word the word which you would to isolate
     * @return the sentences which contextually agree with the given word
     * @throws IOException if the file cannot be opened
     */
    public String[] getMarkerString(String word) throws IOException {

        DataWrapper reader = new DataWrapper(DATA_FILE);
        reader.resetScanner();

        String currentLine = reader.nextLine();

        List<String> allMarkerStrings = new ArrayList<>();

        while (currentLine != null) {
            String markerString = "";

            String[] currentLineAsArray = new String[] {currentLine};
            NGrams currentLineNgrams = new NGrams(currentLineAsArray);
            List<String> listWords =  currentLineNgrams.getLineList().get(0);

            if (listWords.contains(word)) {
                for (String currentWord: listWords) {
                    if (currentWord.equals(word)) {
                        continue;
                    }
                    markerString += currentWord + " ";
                }
            }
            if (!markerString.equals("")) {
                allMarkerStrings.add(markerString);
            }
            currentLine = reader.nextLine();
        }

        String[] markerStringArray = new String[allMarkerStrings.size()];
        allMarkerStrings.toArray(markerStringArray);

        return markerStringArray;
    }

    /**
     * This method is a file writer. It writes each line as a sentence
     * using the separateBySentence method above.
     *
     * @param data the training data to add to the training file
     * @param fileName the file name of the text
     * @throws IOException if the writer cannot find the file with the file name
     */
    private void addDataToFile(String[] data, String fileName) throws IOException {

        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true));

        for (String line: data) {
            String[] sentences = separateBySentence(line);
            for (String sentence: sentences) {
                writer.append(sentence + "\n");
            }
        }

        writer.close();
    }

    /**
     * This method takes some text and organizes it into sentences.
     * The reason we need this is to be able to read the training data
     * file line by line. We want to find the context of a word within a
     * sentence (or at least something similar).
     *
     * @param line the line in a text file that you want to break
     *             up into sentences
     * @return an array of sentences to be added to the training file
     */
    private String[] separateBySentence (String line) {

        String[] sentences;
        line.replaceAll("\n"," ");

        if(line.contains(". ")){
            sentences = line.split("\\. ");
        } else if (line.contains("! ")) {
            sentences = line.split("! ");
        } else if (line.contains("? ")) {
            sentences = line.split("\\? ");
        } else{
            sentences = new String[1];
            sentences[0] = line;
        }
        return sentences;
    }

}