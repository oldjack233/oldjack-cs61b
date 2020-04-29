package lists;

import org.junit.Test;
import static org.junit.Assert.*;

/** FIXME
 *
 *  @author FIXME
 */

public class ListsTest {
    /** FIXME
     */
    @Test
    public void testNaturalRun(){
        IntList test = IntList.list(1,2,1,0);
        IntListList func_result = Lists.naturalRuns(test);
        int[][] result = {{1,2}, {1}, {0}};
        assertEquals(IntListList.list(result), func_result);


    }}



    // It might initially
    //
    //
    // seem daunting to try to set up
    // IntListList expected.
    //
    // There is an easy way to get the IntListList that you want in just
    // few lines of code! Make note of the IntListList.list method that
    // takes as input a 2D array.


    // It might initially seem daunting to try to set up
    // IntListList expected.
    //
    // There is an easy way to get the IntListList that you want in just
    // few lines of code! Make note of the IntListList.list method that
    // takes as input a 2D array.


