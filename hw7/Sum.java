import com.sun.xml.internal.xsom.impl.scd.Iterators;

/** HW #7, Two-sum problem.
 * @author
 */


public class Sum {

    /** Returns true iff A[i]+B[j] = M for some i and j. */
    public static boolean sumsTo(int[] A, int[] B, int m) {
        MySortingAlgorithms.MergeSort sortAlg = new MySortingAlgorithms.MergeSort();
        sortAlg.sort(A, A.length);
        sortAlg.sort(B, B.length);
        for(int i = 0, j = B.length; i < A.length && j >= 0;) {
            if (A[i] + B[j - 1] == m) {
                return true;
            } else if (A[i] + B[j - 1] > m) {
                j--;
            } else {
                i++;
            }
        }
        return false;
    }
}
