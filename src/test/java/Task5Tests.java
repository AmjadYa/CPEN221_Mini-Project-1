import cpen221.mp1.autocompletion.AutoCompletor;
import cpen221.mp1.cities.DataAnalyzer;
import cpen221.mp1.datawrapper.DataWrapper;
import cpen221.mp1.ngrams.NGrams;
import cpen221.mp1.searchterm.SearchTerm;
import cpen221.mp1.sentimentanalysis.meaningfinder.MeaningFinder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/*
Something important to note is that we used the website "https://lingojam.com/TexttoOneLine"
in order to turn the text files used from "https://www.gutenberg.org/" into SINGLE LINES of text.
This made the implementation of our code more easily parse through what it should consider as
a sentence.

Another note is that the separateBySentence function in MeaningFinder will work with files
that are NOT one line. It just works better when it is one line.
 */

public class Task5Tests {


    /*
    BeforeAll initializes the training data every time you run a test.
    Be careful if you are testing this on your own - make sure to delete
    the file named "task5_data.txt" if you want to run a test more than once.
    If you do not, the training data file will continue to duplicate.
     */
    @BeforeAll
    public static void addTrainingData() {

        try {
            String synonym = null;

            List<String> files = new ArrayList<>();
            files.add("data/training/war.txt");
            files.add("data/training/alice.txt");
            files.add("data/training/bible.txt");
            files.add("data/training/brothers.txt");
            files.add("data/training/crime.txt");
            files.add("data/training/diary.txt");
            files.add("data/training/anatomy.txt");
            files.add("data/training/cook.txt");
            files.add("data/training/encyclo.txt");
            files.add("data/training/cinder.txt");
            files.add("data/training/gods.txt");
            files.add("data/training/humour.txt");
            files.add("data/training/beemovie.txt");
            files.add("data/training/huckle.txt");
            files.add("data/training/anne.txt");
            files.add("data/training/oz.txt");
            files.add("data/training/peter.txt");
            files.add("data/training/jungle.txt");
            files.add("data/training/warandpeace.txt");
            files.add("data/training/prideandp.txt");
            files.add("data/training/dante.txt");

            for (String fileName : files)
            {
                DataWrapper dw = new DataWrapper(fileName);

                MeaningFinder mf = new MeaningFinder(new String[]{""});

                String line = dw.nextLine();

                while (line != null) {
                    mf.addTrainingData(line);
                    line = dw.nextLine();
                }
            }

        } catch (IOException io){
            System.out.println("Error Encountered");
        }

    }

    @Test
    public void createFileAndAddData() {
        try {
            MeaningFinder mf = new MeaningFinder(new String[]{"hello gamers. jojo reference.asas", "season's greetings"});
            mf.addTrainingData("the steven returns. asdfjlk .asdlkfj awer. sdfwer");
        } catch (IOException io) {
            System.out.println("An Error Occurred");
        }
    }

    @Test
    public void cosineSim() {
        double similarity;
        similarity = MeaningFinder.cosineSimilarity(
                new String[]{"hello gamers. jojo reference."},
                new String[]{"hi gamers"});
        assertEquals(1 / (Math.sqrt(4) * Math.sqrt(2)), similarity);
    }

    @Test
    public void hardTest1() {

        String synonym = null;
        try {
            MeaningFinder mf = new MeaningFinder(new String[]{""});
            synonym = mf.predictedSynonym("kid", new String[]{"dummy", "woman", "child", "pineapple"});
        } catch (IOException io) {
            System.out.println("An Error Occurred");
        }

        assertEquals("child", synonym);
    }

    @Test
    public void hardTest2() {

        String synonym = null;
        try {
            MeaningFinder mf = new MeaningFinder(new String[]{""});
            synonym = mf.predictedSynonym("girl", new String[]{"dummy", "woman", "test", "pineapple"});
        } catch (IOException io) {
            System.out.println("An Error Occured");
        }

        assertEquals("woman", synonym);
    }

    @Test
    public void easyTest1() {

        String synonym = null;
        try {
            MeaningFinder mf = new MeaningFinder(new String[]{""});
            synonym = mf.predictedSynonym("jesus", new String[]{"foo", "bar", "sum", "god"});
        } catch (IOException io) {
            System.out.println("An Error Occured");
        }

        assertEquals("god", synonym);
    }

    @Test
    public void regularConversation() {

        String synonym = null;
        try {
            MeaningFinder mf = new MeaningFinder(new String[]{""});
            synonym = mf.predictedSynonym("hello", new String[]{"hey", "goodbye", "leave", "scram"});
        } catch (IOException io) {
            System.out.println("An Error Occured");
        }

        assertEquals("hey", synonym);
    }

    @Test
    public void nameSynonyms() {

        String synonym = null;
        try {
            MeaningFinder mf = new MeaningFinder(new String[]{""});
            synonym = mf.predictedSynonym("Steven", new String[]{"Mark", "chair", "desk", "monkey"});
        } catch (IOException io) {
            System.out.println("An Error Occured");
        }

        assertEquals("Mark", synonym);
    }

    @Test
    public void illegalArgumentTest() {

        String synonym = null;
        try {
            MeaningFinder mf = new MeaningFinder(new String[]{""});
            synonym = mf.predictedSynonym("", new String[]{"Mark", "chair", "desk", "monkey"});
        } catch (Exception io) {
            System.out.println("An Error Occured");
            assertTrue(true);
        }

    }

}
