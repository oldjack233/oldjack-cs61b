package enigma;

import org.junit.Test;
import ucb.junit.textui;
import static org.junit.Assert.*;

import static enigma.TestUtils.*;

/** The suite of all JUnit tests for the enigma package.
 *  @author Ziyuan Tang
 */
public class AlphabetTest {

    /** Run the JUnit tests in this package. Add xxxTest.class entries to
     *  the arguments of runClasses to run other JUnit tests. */



    @Test
    public void checksize() {
        Alphabet alphabet = new Alphabet("ABCDEFG");
        int test = 7;
        int actual = alphabet.size();
        assertEquals(msg("1", "Wrong length of string"), test, actual);

        Alphabet alphabet1 = new Alphabet("abushgkd");
        int test1 = 8;
        int actual1 = alphabet1.size();
        assertEquals(msg("1", "Wrong length of string"), test1, actual1);

        Alphabet alphabet2 = new Alphabet(".,/[]_");
        int test2 = 6;
        int actual2 = alphabet2.size();
        assertEquals(msg("1", "Wrong length of string"), test2, actual2);
    }


    @Test
    public  void checkcontain() {
        Alphabet alphabet = new Alphabet("ABCDEFG");
        char test = 'F';
        char test1 = 'f';
        boolean actual = alphabet.contains(test);
        boolean actual1 = alphabet.contains(test1);
        assertTrue(msg("1", "should contain but not"), actual);
        assertFalse(msg("1", "should contain but not"), actual1);

        Alphabet alphabet1 = new Alphabet("abushgkd");
        char test2 = '#';
        char test3 = '%';
        boolean actual2 = alphabet1.contains(test2);
        boolean actual3 = alphabet1.contains(test3);
        assertFalse(msg("1", "should contain but not"), actual2);
        assertFalse(msg("1", "should contain but not"), actual3);

    }
    @Test
    public  void checktochar() {
        Alphabet alphabet = new Alphabet("abcdef");
        char test = 'f';
        char test1 = 'F';
        char actual = alphabet.toChar(5);
        System.out.println(actual);
        char actual1 = alphabet.toChar(4);
        System.out.println(actual1);
        assertEquals(msg("1", "should contain but not"), test, actual);
        assertNotEquals(msg("1", "should contain but not"), test1, actual1);

    }
    @Test
    public  void checktoint() {
        Alphabet alphabet = new Alphabet("abcdef");
        char ch1 = 'f';
        char ch2 = 'O';
        int test = 5;
        int test1 = 0;
        int actual = alphabet.toInt(ch1);
        System.out.println(actual);
        int actual1 = alphabet.toInt(ch2);
        System.out.println(actual1);
        assertEquals(msg("1", "should contain but not"), test, actual);
        assertNotEquals(msg("1", "should contain but not"), test1, actual1);


    }
    public static void main(String[] ignored) {
        System.exit(textui.runClasses(AlphabetTest.class
                ));
    }


}


