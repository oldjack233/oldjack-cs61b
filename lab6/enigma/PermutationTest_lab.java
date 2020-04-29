package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;

import static enigma.TestUtils.*;

/**
 * The suite of all JUnit tests for the Permutation class. For the purposes of
 * this lab (in order to test) this is an abstract class, but in proj1, it will
 * be a concrete class. If you want to copy your tests for proj1, you can make
 * this class concrete by removing the 4 abstract keywords and implementing the
 * 3 abstract methods.
 *
 *  @author
 */
public abstract class PermutationTest_lab {

    /**
     * For this lab, you must use this to get a new Permutation,
     * the equivalent to:
     * new Permutation(cycles, alphabet)
     * @return a Permutation with cycles as its cycles and alphabet as
     * its alphabet
     * @see Permutation for description of the Permutation conctructor
     */
    abstract Permutation getNewPermutation(String cycles, Alphabet alphabet);

    /**
     * For this lab, you must use this to get a new Alphabet,
     * the equivalent to:
     * new Alphabet(chars)
     * @return an Alphabet with chars as its characters
     * @see Alphabet for description of the Alphabet constructor
     */
    abstract Alphabet getNewAlphabet(String chars);

    /**
     * For this lab, you must use this to get a new Alphabet,
     * the equivalent to:
     * new Alphabet()
     * @return a default Alphabet with characters ABCD...Z
     * @see Alphabet for description of the Alphabet constructor
     */
    abstract Alphabet getNewAlphabet();

    /** Testing time limit. */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    /** Check that PERM has an ALPHABET whose size is that of
     *  FROMALPHA and TOALPHA and that maps each character of
     *  FROMALPHA to the corresponding character of FROMALPHA, and
     *  vice-versa. TESTID is used in error messages. */
    private void checkPerm(String testId,
                           String fromAlpha, String toAlpha,
                           Permutation perm, Alphabet alpha) {
        int N = fromAlpha.length();
        assertEquals(testId + " (wrong length)", N, perm.size());
        for (int i = 0; i < N; i += 1) {
            char c = fromAlpha.charAt(i), e = toAlpha.charAt(i);
            assertEquals(msg(testId, "wrong translation of '%c'", c),
                         e, perm.permute(c));
            assertEquals(msg(testId, "wrong inverse of '%c'", e),
                         c, perm.invert(e));
            int ci = alpha.toInt(c), ei = alpha.toInt(e);
            assertEquals(msg(testId, "wrong translation of %d", ci),
                         ei, perm.permute(ci));
            assertEquals(msg(testId, "wrong inverse of %d", ei),
                         ci, perm.invert(ei));
        }
    }

    /* ***** TESTS ***** */

    @Test
    public void checkIdTransform() {
        Alphabet alpha = getNewAlphabet();
        Permutation perm = getNewPermutation("", alpha);
        checkPerm("identity", UPPER_STRING, UPPER_STRING, perm, alpha);
    }

    @Test
    public void checkinvert_char(){
        String from = "JAREDFISH";
        String to = "ARJEFDSHI";
        Permutation perm = getNewPermutation("(JAR) (DF) (ISH)", getNewAlphabet(from));
        assertEquals('J', perm.invert('A'));
        assertEquals('A', perm.invert('R'));
        assertEquals('R', perm.invert('J'));
        assertEquals('E', perm.invert('E'));
        checkPerm("identity", from, to, perm, getNewAlphabet(from));

        String from1 = "ABCD";
        String to1 = "BCDA";
        Permutation perm1 = getNewPermutation("("+from1 +")", getNewAlphabet(from1));
        assertEquals('A', perm1.invert('B'));
        assertEquals('B', perm1.invert('C'));
        assertEquals('C', perm1.invert('D'));
        assertEquals('D', perm1.invert('A'));
        checkPerm("identity", from1, to1, perm1, getNewAlphabet(from1));

    }

    @Test
    public void checkpermute_char(){
        String from = "JAREDFISH";
        String to = "ARJEFDSHI";
        Permutation perm = getNewPermutation("(JAR) (DF) (ISH)", getNewAlphabet(from));
        assertEquals('A', perm.permute('J'));
        assertEquals('R', perm.permute('A'));
        assertEquals('J', perm.permute('R'));
        assertEquals('E', perm.permute('E'));
        checkPerm("identity", from, to, perm, getNewAlphabet(from));



        String from1 = "ABCD";
        String to1 = "BCDA";
        Permutation perm1 = getNewPermutation("("+from1 +")", getNewAlphabet(from1));
        assertEquals('C', perm1.permute('B'));
        assertEquals('D', perm1.permute('C'));
        assertEquals('A', perm1.permute('D'));
        assertEquals('B', perm1.permute('A'));
        checkPerm("identity", from1, to1, perm1, getNewAlphabet(from1));
    }


    @Test
    public void checksize(){
        Alphabet alp0 =  getNewAlphabet("");
        assertEquals(0, alp0.size());
        Alphabet alp1 =  getNewAlphabet("ABCD");
        assertEquals(4, alp1.size());
        Alphabet alp2 =  getNewAlphabet("ABCDE");
        assertEquals(5, alp2.size());
        Alphabet alp3 =  getNewAlphabet("ABCDEF");
        assertEquals(6, alp3.size());
    }

    @Test
    public void checkperm_int(){

        String from = "JAREDFISH";
        String to = "ARJEFDSHI";
        Permutation perm = getNewPermutation("(JAR) (DF) (ISH)", getNewAlphabet(from));
        assertEquals(1, perm.permute(0));
        assertEquals(6, perm.permute(8));
        assertEquals(5, perm.permute(4));
        assertEquals(3, perm.permute(3));
        checkPerm("identity", from, to, perm, getNewAlphabet(from));


        String from1 = "ABCD";
        String to1 = "BCDA";
        Permutation perm1 = getNewPermutation("("+from1+")", getNewAlphabet(from1));
        assertEquals(3, perm1.permute(2));
        assertEquals(0, perm1.permute(3));
        assertEquals(1, perm1.permute(0));
        assertEquals(2, perm1.permute(1));
        checkPerm("identity", from1, to1, perm1, getNewAlphabet(from1));
    }

    @Test
    public void checkpinvert_int(){
        String from = "JAREDFISH";
        String to = "ARJEFDSHI";
        Permutation perm = getNewPermutation("(JAR) (DF) (ISH)", getNewAlphabet(from));
        assertEquals(0, perm.invert(1));
        assertEquals(8, perm.invert(6));
        assertEquals(5, perm.invert(4));
        assertEquals(3, perm.invert(3));
        checkPerm("identity", from, to, perm, getNewAlphabet(from));


        String from1 = "ABCD";
        String to1= "BCDA";
        Permutation perm1 = getNewPermutation("("+from1 +")", getNewAlphabet(from1));
        assertEquals(0, perm1.invert(1));
        assertEquals(1, perm1.invert(2));
        assertEquals(2, perm1.invert(3));
        assertEquals(3, perm1.invert(0));
        checkPerm("identity", from1, to1, perm1, getNewAlphabet(from1));
    }

    @Test
    public void checkAlpha(){
        Alphabet abcd = getNewAlphabet("ABCD");
        Permutation perm = getNewPermutation("(ABCD)", abcd);
        assertEquals(abcd, perm.alphabet());
        Alphabet fish = getNewAlphabet("JAREDFISH");
        Permutation perm1 = getNewPermutation("(JAR) (DF) (ISH)", fish);
        assertEquals(fish, perm1.alphabet());
    }
    @Test
    public void checkDeArr(){
        Alphabet test = getNewAlphabet("ABCD");
        Permutation perm = getNewPermutation("(ABCD)", test);
        assertTrue(perm.derangement());

        Alphabet fish = getNewAlphabet("JAREDFISH");
        Permutation perm1 = getNewPermutation("(JAR) (DF) (ISH)", fish);
        assertFalse(perm1.derangement());
    }


    // FIXME: Add tests here that pass on a correct Permutation and fail on buggy Permutations.
}

