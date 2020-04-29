import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;

/**
 * Test of a BST-based String Set.
 * @author Ziyuan Tang
 */
public class ECHashStringSetTest  {

    @Test
    public void testECHash() {
        ECHashStringSet test = new ECHashStringSet();
        test.put("aaaa");
        test.put("bbbbb");
        test.put("ccccccc");
        test.put("dddd");
        test.put("ee");
        test.put("ffffffffffffff");
        assertTrue(test.contains("aaaa"));
        assertFalse(test.contains("c"));

    }
}
