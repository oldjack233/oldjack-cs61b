package arrays;

import org.junit.Test;
import static org.junit.Assert.*;

/** FIXME
 *  @author FIXME
 */

public class ArraysTest {
    /** FIXME
     */
    @Test
    public void testCatenate(){
        int[] head = new int[]{1,2,3,4,5};
        int[] tail = new int[]{6,7,8,9,10};
        int[] result = Arrays.catenate(head, tail);
        int[] correct = new int[]{1,2,3,4,5,6,7,8,9,10};
        assertArrayEquals(result,correct);
    }

    @Test
    public void testRemove(){
        int[] head = new int[]{1,2,3,4,5,6,7,8,9};
        int[] result = Arrays.remove(head, 2,2);
        int[] correct = new int[]{1,2,5,6,7,8,9};
        assertArrayEquals(result,correct);
    }

    @Test
    public void testNatureRun(){
        int[] head = new int[]{1, 3, 7, 5, 4, 6, 9, 10, 10, 11};
        int[][]result = Arrays.naturalRuns(head);
        int[][] correct = new int[][]{{1, 3, 7}, {5}, {4, 6, 9, 10}, {10, 11}};
        assertArrayEquals(result,correct);}

    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(ArraysTest.class));
    }
}
