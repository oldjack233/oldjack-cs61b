package image;

//import lists.IntList;
//import lists.IntListList;
//import lists.Lists;
//import lists.ListsTest;
import org.junit.Test;
import static org.junit.Assert.*;

/** FIXME
 *  @author FIXME
 */

public class MatrixUtilsTest {
    /** @Test
     */
    @Test
    public void testAV(){
        double[][] test = {{1000000,    1000000,  1000000, 1000000},
                {1000000,    75990,  30003,  1000000},
                {1000000,    30002,  103046, 1000000},
                {1000000,   29515,  38273,  1000000},
                {1000000,     73403,     35399,   1000000},
                {1000000,   1000000,   1000000,   1000000}};

        double[][] result = MatrixUtils.accumulateVertical(test);
        double[][] correct ={{1000000,   1000000,   1000000,  1000000},
                {2000000   ,1075990   ,1030003   ,2000000},
                {2075990   ,1060005   ,1133049   ,2030003},
                {2060005   ,1089520   ,1098278   ,2133049},
                {2089520   ,1162923   ,1124919,   2098278},
                {2162923   ,2124919   ,2124919   ,2124919}};

        assertArrayEquals(result, correct);}

    @Test
    public void testA(){
    double[][] test = {{10,    10,  10, 10},
                        {10,   1,  2,   10},
                        {10,   2,  4,   10},
                        {10,   3,  6,   10},
                        {10,   4,  8,   10},
                        {10,  10,  10,  10}};

    double[][] result = MatrixUtils.accumulate(test, MatrixUtils.Orientation.HORIZONTAL);
    double[][] correct ={{10,  20,  21,   23},
                        {10,   11,  13,   23},
                        {10,   12,  15,   23},
                        {10,   13,  18,   25},
                        {10,   14,  21,   28},
                        {10,  20,  24,  31}};

    assertArrayEquals(correct, result);}

        public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(MatrixUtilsTest.class));
    }
}



