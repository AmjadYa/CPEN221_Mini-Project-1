package cpen221.mp1.autocompletion;

import cpen221.mp1.searchterm.SearchTerm;

import java.lang.reflect.Array;
import java.util.*;

public class AutoCompletor {

    private static final int DEFAULT_SEARCH_LIMIT = 10;

    //an array of potential auto-completions
    private SearchTerm[] searchTerms;

    private int numberOfMatches;

    /**
     * Create an instance of the AutoCompletor class in order
     * to suggest auto-completions to a search term.
     *
     * @param searchTerms an array of ALL possible queries (whether they be related
     *                    to the start of a search or not). Cannot be empty or null.
     *
     */
    public AutoCompletor(SearchTerm[] searchTerms) {

        this.searchTerms = searchTerms;

    }

    /**
     * Create an instance of the AutoCompletor class in order
     * to suggest auto-completions to a search term. This constructor will
     * convert a list of maps (if passed) into an array.
     *
     * @param searchTerms a list of ALL possible queries (whether they be related
     *                    to the start of a search or not). Cannot be empty or null.
     *
     *
     */
    public AutoCompletor(List<Map<String, Long>> searchTerms) {

        //first lets convert the list of maps to just a plain old list of SearchTerms
        List<SearchTerm> convertToList = new ArrayList<>();

        for(int curMapIndex = 0; curMapIndex < searchTerms.size(); curMapIndex++){

            for(Map.Entry<String, Long> entry : searchTerms.get(curMapIndex).entrySet()){

                String curQuery = entry.getKey();
                long curWeight = entry.getValue();
                SearchTerm curSearchTerm = new SearchTerm(curQuery,curWeight);

                convertToList.add(curSearchTerm);

            }//end nested for

        }//end for

        //now convert the list into an array of SearchTerms
        this.searchTerms = new SearchTerm[convertToList.size()];
        this.searchTerms = convertToList.toArray(this.searchTerms);

    }//end constructor

    /**
     * Given the start of a query, generates all potential endings found
     * This method will not recommend a query if it is exactly an inputted prefix
     *
     * @param prefix the start of the query to be auto-completed
     *               cannot be null
     *               cannot have leading spaces
     * @throws IllegalArgumentException if the prefix is null
     * @return an array of all searchTerms that complete the start of a search
     */
    public SearchTerm[] allMatches(String prefix) {

        if(prefix == null) {
            throw new IllegalArgumentException();
        }

        List<SearchTerm> allMatchesList = new ArrayList<>();

        //iterate through the searchTerms array and find all queries
        //that start with the given prefix
        for (SearchTerm term : searchTerms){

            if(term.getQuery().length() > prefix.length() &&
                    prefix.equals(term.getQuery().substring(0,prefix.length()))){

                allMatchesList.add(term);

            }//end if

        }//end for

        //convert the list of auto-completions to an array
        SearchTerm[] allMatchesArr = new SearchTerm[allMatchesList.size()];
        allMatchesArr = allMatchesList.toArray(allMatchesArr);

        //update the number of matches found
        numberOfMatches = allMatchesArr.length;

        return allMatchesArr;
    }

    /**
     * Based on the weight of the searchTerm (how likely it is to be the
     *
     * @param prefix the start of the query to be auto-completed
     * @param limit the restricted number of auto-completions to suggest
     *              limit is positive
     * @throws IllegalArgumentException if limit is less than one
     * @return The top weighted SearchTerms that match the given prefix.
     *
     */
    public SearchTerm[] topKMatches(String prefix, int limit) {

        if(limit < 1) {
            throw new IllegalArgumentException();
        }

        //get all the possible matches using the allMatches method
        SearchTerm[] allMatchesArr = allMatches(prefix);

        //check if all possible matches are lower than the specified limit
        //and initialize an array to the specified limit
        SearchTerm[] topKMatchesArr = new SearchTerm[Math.min(limit, allMatchesArr.length)];

        //sort all the matches based on weight first, then lexicographic order
        Arrays.sort(allMatchesArr);

        //put the top 'K' matches into a new array
        for(int i = 0; i < Math.min(limit, allMatchesArr.length); i++) {
            topKMatchesArr[i] = allMatchesArr[i];
        }

        //return an array of ordered matches up the specified limit parameter
        return topKMatchesArr;
    }

    /**
     * This method limits the number of auto-completions to suggest if a limit
     * was not specified. It will use the default limit specified at the beginning
     * of this class, and the topKMatches method to do the heavy lifting.
     *
     * @param prefix the start of the query to be auto-completed
     * @return the topKMatches results (the ordered array of the best to worst
     *          possible auto-completions up to the default limit)
     */
    public SearchTerm[] topKMatches(String prefix) {
        return topKMatches(prefix, DEFAULT_SEARCH_LIMIT);
    }

    /**
     * This method returns the total number of possible queries for a given prefix.
     * Before returning any value, it updates the allMatches method to reflect the current prefix.
     *
     * @param prefix the start of the query to be auto-completed
     * @return the number of queries (auto-completions)  found for the given prefix
     *
     */
    public int numberOfMatches(String prefix) {

        //update the allMatches method to match the inputted prefix
        allMatches(prefix);

        return numberOfMatches;
    }

}
