import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Test of a BST-based String Set.
 * @author
 */
public class BSTStringSetTest  {
    // FIXME: Add your own tests for your BST StringSet

    @Test
    public void testPut() {
        ArrayList<String> expect = new ArrayList<String>();
        expect.add("a");
        expect.add("b");
        expect.add("c");
        expect.add("d");
        BSTStringSet test = new BSTStringSet();
        test.put("c");
        test.put("a");
        test.put("b");
        test.put("d");
        assertEquals(expect, test.asList());

    }
    @Test
    public void testContains() {
        BSTStringSet test = new BSTStringSet();
        test.put("c");
        test.put("a");
        test.put("b");
        test.put("d");
        assertTrue(test.contains("b"));
        assertFalse(test.contains("e"));
    }

    @Test
    public void testIter() {
        BSTStringSet test = new BSTStringSet();
        test.put("f");
        test.put("a");
        test.put("c");
        test.put("b");
        test.put("d");
        test.put("e");
        Iterator<String> iter = test.iterator("b", "e");
        assertEquals("b",iter.next());
        assertEquals("c", iter.next());
    }
}
