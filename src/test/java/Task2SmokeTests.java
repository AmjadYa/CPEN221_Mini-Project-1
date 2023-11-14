
import cpen221.mp1.ratemyprofessor.DataAnalyzer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Task2SmokeTests {

    private static DataAnalyzer da1;
    private static DataAnalyzer da2;

    @BeforeAll
    public static void setUpTests() throws FileNotFoundException {
        da1 = new DataAnalyzer("data/reviews1.txt");
        da2 = new DataAnalyzer("data/reviews2.txt");
    }

    @Test
    public void testGood() {
        String query = "good";
        Map<String, Long> expected = Map.of(
                "ML", 0L,
                "WL", 1L,
                "MM", 0L,
                "WM", 0L,
                "MH", 1L,
                "WH", 1L
        );
        assertEquals(expected, da1.getHistogram(query));
    }

    @Test
    public void testPartWord() {
        String query = "go";
        Map<String, Long> expected = Map.of(
                "ML", 0L,
                "WL", 0L,
                "MM", 0L,
                "WM", 0L,
                "MH", 0L,
                "WH", 0L
        );
        assertEquals(expected, da1.getHistogram(query));
    }

    @Test
    public void specialChar() {
        String query = ",";
        Map<String, Long> expected = Map.of(
                "ML", 0L,
                "WL", 0L,
                "MM", 0L,
                "WM", 0L,
                "MH", 0L,
                "WH", 0L
        );
        assertEquals(expected, da2.getHistogram(query));
    }
    @Test
    public void endWord() {
        String query = "cares";
        Map<String, Long> expected = Map.of(
                "ML", 0L,
                "WL", 0L,
                "MM", 0L,
                "WM", 0L,
                "MH", 0L,
                "WH", 1L
        );
        assertEquals(expected, da1.getHistogram(query));
    }
    @Test
    public void multiWord() {
        String query = "he is";
        Map<String, Long> expected = Map.of(
                "ML", 1L,
                "WL", 0L,
                "MM", 3L,
                "WM", 0L,
                "MH", 3L,
                "WH", 0L
        );
        assertEquals(expected, da2.getHistogram(query));
    }
    @Test
    public void oneChar() {
        String query = "i";
        Map<String, Long> expected = Map.of(
                "ML", 0L,
                "WL", 0L,
                "MM", 1L,
                "WM", 0L,
                "MH", 4L,
                "WH", 0L
        );
        assertEquals(expected, da2.getHistogram(query));
    }

    @Test
    public void testHeIs() {
        String query = "he is";
        Map<String, Long> expected = Map.of(
                "ML", 1L,
                "WL", 0L,
                "MM", 3L,
                "WM", 0L,
                "MH", 3L,
                "WH", 0L
        );
        assertEquals(expected, da2.getHistogram(query));
    }

    @Test
    public void testProfessorWasBad() {
        String query = "professor was bad";
        Map<String, Long> expected = Map.of(
                "ML", 0L,
                "WL", 0L,
                "MM", 0L,
                "WM", 0L,
                "MH", 0L,
                "WH", 0L
        );
        assertEquals(expected, da2.getHistogram(query));
    }

}