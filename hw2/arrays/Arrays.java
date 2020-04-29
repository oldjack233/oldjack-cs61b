package arrays;

/* NOTE: The file Arrays/Utils.java contains some functions that may be useful
 * in testing your answers. */

/** HW #2 */

/** Array utilities.
 *  @author
 */
class Arrays {

    /* C1. */
    /** Returns a new array consisting of the elements of A followed by the
     *  the elements of B. */
    static int[] catenate(int[] A, int[] B) {
        /* *Replace this body with the solution. */
        int[] result = new int[A.length+B.length];
        System.arraycopy(A,0, result, 0, A.length);
        System.arraycopy(B,0, result, A.length, B.length);
        return result;
    }

    /* C2. */
    /** Returns the array formed by removing LEN items from A,
     *  beginning with item #START. */
    static int[] remove(int[] A, int start, int len) {
        /* *Replace this body with the solution. */
        int[] result = new int[A.length -len];
        System.arraycopy(A, 0, result, 0, start);
        System.arraycopy(A, start+len, result, start , A.length -start-len);
        return result;
    }

    /* C3. */
    /** Returns the array of arrays formed by breaking up A into
     *  maximal ascending lists, without reordering.
     *  For example, if A is {1, 3, 7, 5, 4, 6, 9, 10}, then
     *  returns the three-element array
     *  {{1, 3, 7}, {5}, {4, 6, 9, 10}}. */
    static int[][] naturalRuns(int[] A) {
        /* *Replace this body with the solution. */
        int[][] empty = new int[0][0];
        if(A.length == 0){
            return empty;
        }
        int t = 0;
        int f = 0;
        while(t + 1<A.length){
            if(A[t] >= A[t+1]){
                f +=1;
            }
            t += 1;
        }

        int[][] result = new int[f+1][];
        int ini_val = 0;
        int i =0;
        int j = 0;
        int num_cur_len = 0;
        int sum_len = 0;
        while(i < A.length ){
            while(i < A.length && A[i] > ini_val){
                num_cur_len += 1;
                ini_val = A[i];
                i+=1;

            }
            int[] sub = new int[num_cur_len];
            System.arraycopy(A, sum_len, sub, 0, num_cur_len );
            result[j] = sub;
            j+=1;
            sum_len +=num_cur_len;
            num_cur_len = 0;
            ini_val = 0;
        }
        return result;
    }
}
