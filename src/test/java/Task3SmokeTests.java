import cpen221.mp1.autocompletion.AutoCompletor;
import cpen221.mp1.cities.DataAnalyzer;
import cpen221.mp1.ngrams.NGrams;
import cpen221.mp1.searchterm.SearchTerm;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;


public class Task3SmokeTests {

    private static String citiesData = "data/cities.txt";
    private static DataAnalyzer cityAnalyzer;
    private static DataAnalyzer testFileAnalyzer;
    private static AutoCompletor ac;
    private static AutoCompletor ac2;
    private static AutoCompletor ac3;

    @BeforeAll
    public static void setupTests() {
        cityAnalyzer = new DataAnalyzer(citiesData);
        ac = new AutoCompletor(cityAnalyzer.getSearchTerms());
        testFileAnalyzer = new DataAnalyzer("data/lees_tees.txt");
        ac2 = new AutoCompletor(testFileAnalyzer.getSearchTerms());
    }


    @Test
    public void test_San_3() {
        SearchTerm[] st = ac.topKMatches("San", 3);

        SearchTerm santiago = new SearchTerm("Santiago, Chile", 4837295);
        SearchTerm santoDomingo = new SearchTerm("Santo Domingo, Dominican Republic", 2201941);
        SearchTerm sanaa = new SearchTerm("Sanaa, Yemen", 1937451);
        SearchTerm[] expectedST = new SearchTerm[] { santiago, santoDomingo, sanaa };

        Assertions.assertArrayEquals(expectedST, st);
    }

    @Test
    public void testCities2() {
        SearchTerm[] st = ac.topKMatches("Saint Petersburg", 3);

        SearchTerm russia = new SearchTerm("Saint Petersburg, Russia", 4039745);
        SearchTerm usa = new SearchTerm("Saint Petersburg, Florida, United States", 244769);
        SearchTerm[] expectedST = new SearchTerm[] { russia, usa };

        Assertions.assertArrayEquals(expectedST, st);
    }

    @Test
    public void testEmptyPrefix() {
        SearchTerm[] st = ac.topKMatches("", 2);

        SearchTerm china = new SearchTerm("Shanghai, China", 14608512);
        SearchTerm argentina = new SearchTerm("Buenos Aires, Argentina", 13076300);
        SearchTerm[] expectedST = new SearchTerm[] { china, argentina };

        Assertions.assertArrayEquals(expectedST, st);
    }

    @Test
    public void testThrowException() {
        try {
            SearchTerm[] st = ac.topKMatches(null, 2);
        }
        catch (Exception fnf) {
            assertTrue(true);
        }
    }

    @Test
    public void testSpacePrefix() {

        SearchTerm[] st = ac.topKMatches("             ", 2);
        SearchTerm[] expectedST = new SearchTerm[] { } ;

        Assertions.assertArrayEquals(expectedST, st);
    }

    @Test
    public void testNoLimit() {
        SearchTerm[] st = ac2.topKMatches("almost");

        SearchTerm redShirt = new SearchTerm("almost red shirt", 9);
        SearchTerm blackShirt = new SearchTerm("almost black shirt", 8);
        SearchTerm almost = new SearchTerm("almost almost red", 4);
        SearchTerm[] expectedST = new SearchTerm[] { redShirt, blackShirt, almost};

        Assertions.assertArrayEquals(expectedST, st);
    }

    @Test
    public void testNumberOfMatches() {
        int st = ac2.numberOfMatches("red");
        int expectedST = 1;

        Assertions.assertEquals(expectedST, st);
    }

    @Test
    public void getAllNumberMatches() {
        int st = ac2.numberOfMatches("");
        int expectedST = 10;

        Assertions.assertEquals(expectedST, st);
    }

    @Test
    public void testLimitIsZero() {
        try {
            SearchTerm[] st = ac.topKMatches("Saint Petersburg", 0);
        }
        catch (Exception fnf) {
            assertTrue(true);
        }
    }

    @Test
    public void exactPrefixFound() {
        SearchTerm[] st = ac2.topKMatches("nice shirt");

        SearchTerm niceShirt = new SearchTerm("nice shirt that looks funny too", 6);
        SearchTerm[] expectedST = new SearchTerm[] { niceShirt };

        Assertions.assertArrayEquals(expectedST, st);
    }

    @Test
    public void useNGrams() {

        List< Map<String, Long> > listMaps = new ArrayList<>();
        HashMap<String, Long> oneGrams = new HashMap<>(Map.of(
                "cat", 3L
        ));
        HashMap<String, Long> twoGrams = new HashMap<>(Map.of(
                "cat cat", 2L
        ));
        HashMap<String, Long> threeGrams = new HashMap<>(Map.of(
                "cat cat cat", 1L
        ));

        listMaps.add(oneGrams);
        listMaps.add(twoGrams);
        listMaps.add(threeGrams);

        ac3 = new AutoCompletor(listMaps);
        SearchTerm[] st = ac3.topKMatches("ca", 3);

        SearchTerm cat = new SearchTerm("cat", 3);
        SearchTerm catcat = new SearchTerm("cat cat", 2);
        SearchTerm catcatcat = new SearchTerm("cat cat cat", 1);

        SearchTerm[] expectedST = new SearchTerm[] { cat, catcat, catcatcat };

        Assertions.assertArrayEquals(expectedST, st);
    }

}