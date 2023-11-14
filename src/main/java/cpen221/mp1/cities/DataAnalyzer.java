package cpen221.mp1.cities;

import cpen221.mp1.autocompletion.AutoCompletor;
import cpen221.mp1.autocompletion.gui.AutoCompletorGUI;
import cpen221.mp1.datawrapper.DataWrapper;
import cpen221.mp1.searchterm.SearchTerm;

import javax.swing.*;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/*
   This DataAnalyzer for the cities dataset is intended to be
   a simple example of a dataset that can be used with the
   AutoCompletor. The example also illustrates how to process
   the dataset using a DataWrapper, and also how to extract
   the different components of the dataset.
 */

public class DataAnalyzer {

    private static final String citiesData = "data/cities.txt";
    private SearchTerm[] searchTerms;

    public DataAnalyzer(String filename) {
        try {
            DataWrapper dw = new DataWrapper(filename);
            List<SearchTerm> stList = new ArrayList<>();
            for (String line = dw.nextLine(); line != null; line = dw.nextLine()) {
                line.trim();
                String[] lineComponents = line.split("\t", 2);
                SearchTerm st = new SearchTerm(lineComponents[1], Integer.parseInt(lineComponents[0].trim()));
                stList.add(st);
            }

            int numTerms = stList.size();
            searchTerms = new SearchTerm[numTerms];
            searchTerms = stList.toArray(searchTerms);
        }
        catch (FileNotFoundException fe) {
            System.out.printf("%s: File not found!\n", filename);
        }
    }

    public SearchTerm[] getSearchTerms() {
        return searchTerms; // careful here!
    }

    public static void main(String[] args) {
        DataAnalyzer da = new DataAnalyzer(citiesData);
        SearchTerm[] searchTerms = da.searchTerms;
        AutoCompletor ac = new AutoCompletor(searchTerms);
        final int k = 10;
        SwingUtilities.invokeLater(
                new Runnable() {
                    public void run() {
                        new AutoCompletorGUI(searchTerms, k).setVisible(true);
                    }
                }
        );
    }
}