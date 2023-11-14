import cpen221.mp1.sentimentanalysis.SentimentAnalyzer;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import static org.junit.jupiter.api.Assertions.*;

public class SentimentAnalysisTests {

    // provided smoke tests
    @Test
    public void testRating1() {
        try {
            SentimentAnalyzer rmp_sa = new SentimentAnalyzer("data/ratemyprofessor_data.txt");
            assertEquals(1f, rmp_sa.getPredictedRating("oh no it was so difficult"));
        }
        catch (FileNotFoundException fnf) {
            fail("Data file is not in the right place!");
        }
    }

    @Test
    public void testRating2() {
        try {
            SentimentAnalyzer rmp_sa = new SentimentAnalyzer("data/ratemyprofessor_data.txt");
            assertEquals(2f, rmp_sa.getPredictedRating("soft voice sit in the front"));
        }
        catch (FileNotFoundException fnf) {
            fail("Data file is not in the right place!");
        }
    }

    @Test
    public void testRating3() {
        try {
            SentimentAnalyzer rmp_sa = new SentimentAnalyzer("data/ratemyprofessor_data.txt");
            assertEquals(3f, rmp_sa.getPredictedRating("slept but okay"));
        }
        catch (FileNotFoundException fnf) {
            fail("Data file is not in the right place!");
        }
    }


    @Test
    public void testRating4() {
        try {
            SentimentAnalyzer rmp_sa = new SentimentAnalyzer("data/ratemyprofessor_data.txt");
            assertEquals(4f, rmp_sa.getPredictedRating("good lecturer but assigns a lot of work"));
        }
        catch (FileNotFoundException fnf) {
            fail("Data file is not in the right place!");
        }
    }

    @Test
    public void testRating5() {
        try {
            SentimentAnalyzer rmp_sa = new SentimentAnalyzer("data/ratemyprofessor_data.txt");
            assertEquals(5f, rmp_sa.getPredictedRating("very fun and kind"));
        }
        catch (FileNotFoundException fnf) {
            fail("Data file is not in the right place!");
        }
    }

    @Test
    public void testBadFile() {
        try {
            SentimentAnalyzer rmp_sa = new SentimentAnalyzer("data/ratmyprofessor_data.txt");
        }
        catch (Exception fnf) {
            assertTrue(true);
        }
    }

    //custom tests
    @Test
    public void otherFileTest1() {
        try {
            SentimentAnalyzer rmp_sa = new SentimentAnalyzer("data/mock_review_data.txt");
            assertEquals(1f, rmp_sa.getPredictedRating("dogwater absolutely terrible"));
        }
        catch (FileNotFoundException fnf) {
            fail("Data file is not in the right place!");
        }
    }

    @Test
    public void ratingInRMPDecimal1() {
        try {
            SentimentAnalyzer rmp_sa = new SentimentAnalyzer("data/ratemyprofessor_data.txt");
            assertEquals(1.5f, rmp_sa.getPredictedRating("lectures were extremely unclear " +
                    "the first midterm was a disaster no one knew what he was asking tries to be helpful " +
                    "but makes students uncomfortable instead he will interrupt you throughout your entire " +
                    "presentation making it very difficult to keep things flowing the content of the class is " +
                    "good but the instruction of it was very poor"));
        }
        catch (FileNotFoundException fnf) {
            fail("Data file is not in the right place!");
        }
    }

    @Test
    public void ratingInRMPDecimal2() {
        try {
            SentimentAnalyzer rmp_sa = new SentimentAnalyzer("data/ratemyprofessor_data.txt");
            assertEquals(2.5f, rmp_sa.getPredictedRating("he knows his stuff but not there " +
                    "for his students very distant professor and not very approachable very harsh grader do not " +
                    "take if you are not an english major"));
        }
        catch (FileNotFoundException fnf) {
            fail("Data file is not in the right place!");
        }
    }

    @Test
    public void ratingInRMPDecimal3() {
        try {
            SentimentAnalyzer rmp_sa = new SentimentAnalyzer("data/ratemyprofessor_data.txt");
            assertEquals(3.5f, rmp_sa.getPredictedRating("i took studies in writing as a required " +
                    "course for english ed but this is a creative writing course wrote creative papers which i had " +
                    "never done before she grades easily and pities you if you a creative writing major does not do " +
                    "much during class time besides talk about the books but there are no testsquizzes sweet lady"));
        }
        catch (FileNotFoundException fnf) {
            fail("Data file is not in the right place!");
        }
    }

    @Test
    public void ratingInRMPDecimal4(){
        try {
            SentimentAnalyzer rmp_sa = new SentimentAnalyzer("data/ratemyprofessor_data.txt");
            assertEquals(4.5f, rmp_sa.getPredictedRating("very nice woman projects are fun and " +
                    "easy to complete imo always there for students but sometimes to the extent of being a pushover " +
                    "re due dates and quality like most of art courses critiques are too lax and the quality of work " +
                    "is severely overrated she is also the advisor for the graphic design club"));
        } catch (FileNotFoundException fnf) {
            fail("Data file is not in the right place!");
        }
    }

    @Test
    public void ratingInRMPShort() {
        try {
            SentimentAnalyzer rmp_sa = new SentimentAnalyzer("data/ratemyprofessor_data.txt");
            assertEquals(5f, rmp_sa.getPredictedRating("mrs dameron is a wonderful teacher she " +
                    "makes it easy and fun to pass her class anyone who need to take math class should take it with " +
                    "her"));
        } catch (FileNotFoundException fnf) {
            fail("Data file is not in the right place!");
        }
    }

    @Test
    public void ratingInRMPLong() {
        try {
            SentimentAnalyzer rmp_sa = new SentimentAnalyzer("data/ratemyprofessor_data.txt");
            assertEquals(2.5f, rmp_sa.getPredictedRating("great guy horrible professor he would be " +
                    "a great professor for students not taking an intro course he is very unclear in his " +
                    "expectations and has major mood swings kicked whole class out twice for things we have no " +
                    "control over probably the reason the theatre department is not strong at ua would take as a sr " +
                    "but maybe not as a fresh or soph"));
        } catch (FileNotFoundException fnf) {
            fail("Data file is nto in the right place!");
        }
    }

}