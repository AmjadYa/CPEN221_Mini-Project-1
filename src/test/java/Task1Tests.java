import cpen221.mp1.ngrams.NGrams;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Task1Tests {

    // smoke tests
    @Test
    public void simpleTestCount() {
        String text1 = "the blue cow jumped over the blue cow moon!";
        String text2 = "The Blue Period of Picasso is the period between 1900 and 1904, when he painted essentially monochromatic paintings in shades of blue and blue-green, only occasionally warmed by other colors.";

        long expectedCount = 130;

        NGrams ng = new NGrams(new String[]{text1, text2});

        assertEquals(expectedCount, ng.getTotalNGramCount(4));
    }

    @Test
    public void simpleTestGetNGrams() {
        String text1 = "great class";
        String text2 = "good textbook written by him";

        List<Map<String, Long>> expectedNGrams = List.of(
                Map.of("great", 1L, "class", 1L, "good", 1L, "textbook", 1L, "written", 1L, "by", 1L, "him", 1L),
                Map.of("great class", 1L, "good textbook", 1L, "textbook written", 1L, "written by", 1L, "by him", 1L),
                Map.of("good textbook written", 1L, "textbook written by", 1L, "written by him", 1L),
                Map.of("good textbook written by", 1L, "textbook written by him", 1L),
                Map.of("good textbook written by him", 1L)
        );

        NGrams ng = new NGrams(new String[]{text1, text2});
        assertEquals(expectedNGrams, ng.getAllNGrams());
    }

    // custom tests
    @Test
    public void dupeTests() {
        String text1 = "great great great great";
        String text2 = "good textbook written by him";

        List<Map<String, Long>> expectedNGrams = List.of(
                Map.of("great", 4L, "good", 1L, "textbook", 1L, "written", 1L, "by", 1L, "him", 1L),
                Map.of("great great", 3L, "good textbook", 1L, "textbook written", 1L, "written by", 1L, "by him", 1L),
                Map.of("great great great", 2L,"good textbook written", 1L, "textbook written by", 1L, "written by him", 1L),
                Map.of("great great great great", 1L,"good textbook written by", 1L, "textbook written by him", 1L),
                Map.of("good textbook written by him", 1L)
        );

        NGrams ng = new NGrams(new String[]{text1, text2});
        assertEquals(expectedNGrams, ng.getAllNGrams());
    }
    @Test
    public void specialCharTest() {
        String text1 = "(*&*(&#(@$^)!";
        String text2 = "good textbook written by him";

        List<Map<String, Long>> expectedNGrams = List.of(
                Map.of("good", 1L, "textbook", 1L, "written", 1L, "by", 1L, "him", 1L),
                Map.of("good textbook", 1L, "textbook written", 1L, "written by", 1L, "by him", 1L),
                Map.of("good textbook written", 1L, "textbook written by", 1L, "written by him", 1L),
                Map.of("good textbook written by", 1L, "textbook written by him", 1L),
                Map.of("good textbook written by him", 1L)
        );

        NGrams ng = new NGrams(new String[]{text1, text2});
        assertEquals(expectedNGrams, ng.getAllNGrams());
    }

    @Test
    public void wordStuckTest() {
        String text1 = "hello@mynameis@steven";
        String text2 = "good textbook written by him";

        List<Map<String, Long>> expectedNGrams = List.of(
                Map.of("hello",1L,"mynameis",1L,"steven",1L,"good", 1L, "textbook", 1L, "written", 1L, "by", 1L, "him", 1L),
                Map.of("hello mynameis", 1L, "mynameis steven", 1L,"good textbook", 1L,"textbook written", 1L, "written by", 1L, "by him", 1L),
                Map.of("hello mynameis steven", 1L, "good textbook written", 1L,"textbook written by", 1L, "written by him", 1L),
                Map.of("good textbook written by", 1L, "textbook written by him", 1L),
                Map.of("good textbook written by him", 1L)
        );

        NGrams ng = new NGrams(new String[]{text1, text2});
        assertEquals(expectedNGrams, ng.getAllNGrams());
    }
    @Test
    public void spaceTest() {
        String text1 = " great ";
        String text2 = "good textbook written by him";

        List<Map<String, Long>> expectedNGrams = List.of(
                Map.of("great", 1L, "good", 1L, "textbook", 1L, "written", 1L, "by", 1L, "him", 1L),
                Map.of("good textbook", 1L, "textbook written", 1L, "written by", 1L, "by him", 1L),
                Map.of("good textbook written", 1L, "textbook written by", 1L, "written by him", 1L),
                Map.of("good textbook written by", 1L, "textbook written by him", 1L),
                Map.of("good textbook written by him", 1L)
        );

        NGrams ng = new NGrams(new String[]{text1, text2});
        assertEquals(expectedNGrams, ng.getAllNGrams());
    }
    @Test
    public void spaceTest2() {
        String text1 = " great              better ";
        String text2 = "good textbook written by him";

        List<Map<String, Long>> expectedNGrams = List.of(
                Map.of("great", 1L, "better", 1L,"good", 1L, "textbook", 1L, "written", 1L, "by", 1L, "him", 1L),
                Map.of("great better", 1L,"good textbook", 1L, "textbook written", 1L, "written by", 1L, "by him", 1L),
                Map.of("good textbook written", 1L, "textbook written by", 1L, "written by him", 1L),
                Map.of("good textbook written by", 1L, "textbook written by him", 1L),
                Map.of("good textbook written by him", 1L)
        );

        NGrams ng = new NGrams(new String[]{text1, text2});
        assertEquals(expectedNGrams, ng.getAllNGrams());
    }
    @Test
    public void multiStringTest() {
        String text1 = "great class";
        String text2 = "good textbook";
        String text3 = "written by him";
        String text4 = "lmao kekw";

        List<Map<String, Long>> expectedNGrams = List.of(
                Map.of("great", 1L, "class", 1L, "good", 1L, "textbook", 1L, "written", 1L, "by", 1L,"kekw", 1L,"lmao", 1L, "him", 1L),
                Map.of("great class", 1L, "good textbook", 1L, "written by", 1L, "by him", 1L, "lmao kekw", 1L),
                Map.of("written by him", 1L)

        );

        NGrams ng = new NGrams(new String[]{text1, text2,text3,text4});
        assertEquals(expectedNGrams, ng.getAllNGrams());
    }

    @Test
    public void dashTest() {
        String text1 = "great-class";
        String text2 = "good textbook";

        List<Map<String, Long>> expectedNGrams = List.of(
                Map.of("great-class", 1L,  "good", 1L, "textbook", 1L),
                Map.of("good textbook", 1L)
        );

        NGrams ng = new NGrams(new String[]{text1, text2});
        assertEquals(expectedNGrams, ng.getAllNGrams());
    }
    @Test
    public void apostropheTest() {
        String text1 = "class's";
        String text2 = "good textbook";

        List<Map<String, Long>> expectedNGrams = List.of(
                Map.of("class's", 1L,  "good", 1L, "textbook", 1L),
                Map.of("good textbook", 1L)
        );

        NGrams ng = new NGrams(new String[]{text1, text2});
        assertEquals(expectedNGrams, ng.getAllNGrams());
    }

    @Test
    public void caseTest() {
        String text1 = "gOod TextBook";
        String text2 = "good textbook";

        List<Map<String, Long>> expectedNGrams = List.of(
                Map.of("good", 2L, "textbook", 2L),
                Map.of("good textbook", 2L)
        );

        NGrams ng = new NGrams(new String[]{text1, text2});
        assertEquals(expectedNGrams, ng.getAllNGrams());
    }

    @Test
    public void getLines() {
        String text1 = "this is a line";
        String text2 = "line a is this";

        List<List<String>> expectedNGrams = List.of(
                List.of("this", "is", "a", "line"),
                List.of("line", "a", "is", "this")
        );

        NGrams ng = new NGrams(new String[]{text1, text2});
        assertEquals(expectedNGrams, ng.getLineList());
    }

}


