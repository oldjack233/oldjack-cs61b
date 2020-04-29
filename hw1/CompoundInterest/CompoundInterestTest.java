import static org.junit.Assert.*;
import org.junit.Test;

public class CompoundInterestTest {

    private static final int THIS_YEAR = 2020;

    @Test
    public void testNumYears() {
        /** Sample assert statement for comparing integers.

        assertEquals(0, 0); */
        assertEquals(4,CompoundInterest.numYears(2024));
    }

    @Test
    public void testFutureValue() {
            double tolerance = 0.01;
       assertEquals(10, CompoundInterest.futureValue(10,-10,2020), tolerance);
        assertEquals(11.2, CompoundInterest.futureValue(10,12,2021), tolerance);
        assertEquals(10, CompoundInterest.futureValue(10,10,2020), tolerance);
    }


    @Test
    public void testFutureValueReal() {
        double tolerance = 0.01;
        assertEquals(10.864, CompoundInterest.futureValueReal
                (10,12,2021, 3), tolerance);
    }


    @Test
    public void testTotalSavings() {
        double tolerance = 0.01;
        assertEquals(10500, CompoundInterest.totalSavings(5000, 2021, 10), tolerance);
    }

    @Test
    public void testTotalSavingsReal() {
        double tolerance = 1;
        assertEquals(14936.375, CompoundInterest.totalSavingsReal(5000, THIS_YEAR+2, 10, 5), tolerance);
        assertEquals(10185, CompoundInterest.totalSavingsReal(5000, 2021, 10, 3), tolerance);
    }


    /* Run the unit tests in this file. */
    public static void main(String... args) {
        System.exit(ucb.junit.textui.runClasses(CompoundInterestTest.class));
    }
}
