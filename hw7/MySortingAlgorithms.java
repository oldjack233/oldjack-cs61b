import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Note that every sorting algorithm takes in an argument k. The sorting 
 * algorithm should sort the array from index 0 to k. This argument could
 * be useful for some of your sorts.
 *
 * Class containing all the sorting algorithms from 61B to date.
 *
 * You may add any number instance variables and instance methods
 * to your Sorting Algorithm classes.
 *
 * You may also override the empty no-argument constructor, but please
 * only use the no-argument constructor for each of the Sorting
 * Algorithms, as that is what will be used for testing.
 *
 * Feel free to use any resources out there to write each sort,
 * including existing implementations on the web or from DSIJ.
 *
 * All implementations except Counting Sort adopted from Algorithms,
 * a textbook by Kevin Wayne and Bob Sedgewick. Their code does not
 * obey our style conventions.
 */
public class MySortingAlgorithms {

    /**
     * Java's Sorting Algorithm. Java uses Quicksort for ints.
     */
    public static class JavaSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            Arrays.sort(array, 0, k);
        }

        @Override
        public String toString() {
            return "Built-In Sort (uses quicksort for ints)";
        }
    }

    /** Insertion sorts the provided data. */
    public static class InsertionSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {

            for (int mark = 0; mark < k - 1; mark++){
                int curArr = array[mark + 1];
                for (int x = mark; x >= 0; x--) {
                    if (curArr < array[x]) {
                        array[x + 1] = array[x];
                        array[x] = curArr;
                }
            // FIXME
        }
    }
        }

        @Override
        public String toString() {
            return "Insertion Sort";
        }
    }

    /**
     * Selection Sort for small K should be more efficient
     * than for larger K. You do not need to use a heap,
     * though if you want an extra challenge, feel free to
     * implement a heap based selection sort (i.e. heapsort).
     */
    public static class SelectionSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            for (int i = 0; i < k; i++) {
                int small = Integer.MAX_VALUE;
                int mark = 0;
                for (int j = i; j < k; j++) {
                    if (array[j] <= small) {
                        small = array[j];
                        mark = j;
                    }
                }
                array[mark] = array[i];
                array[i] = small;
            }
            // FIXME
        }

        @Override
        public String toString() {
            return "Selection Sort";
        }
    }

    /** Your mergesort implementation. An iterative merge
      * method is easier to write than a recursive merge method.
      * Note: I'm only talking about the merge operation here,
      * not the entire algorithm, which is easier to do recursively.
      */
    public static class MergeSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {

            sort(array, 0, k-1);// FIXME
        }

        public void sort(int[] array, int start, int end) {
            if (start < end) {
                int mid = (start + end) / 2;
                sort(array, start, mid);
                sort(array, mid+1, end);
                merge(array, start, mid, end);
            }

        }

            // FIXME
            private void merge(int[] array, int left, int mid, int right) {
                int[] temp = new int[right - left + 1];
                int index = 0;
                int leftIndex = left;
                int rightIndex = mid+1;
                while (leftIndex <= mid && rightIndex <= right) {
                    if (array[leftIndex] < array[rightIndex]) {
                        temp[index++] = array[leftIndex++];
                    } else {
                        temp[index++] = array[rightIndex++];
                    }
                }
                while (leftIndex <= mid) {
                    temp[index++] = array[leftIndex++];
                }
                while (rightIndex <= right) {
                    temp[index++] = array[rightIndex++];
                }
                if (temp.length >= 0) System.arraycopy(temp, 0, array, left, temp.length);

            }


        // may want to add additional methods

        @Override
        public String toString() {
            return "Merge Sort";
        }
    }

    /**
     * Your Counting Sort implementation.
     * You should create a count array that is the
     * same size as the value of the max digit in the array.
     */
    public static class CountingSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            // FIXME: to be implemented
        }

        // may want to add additional methods

        @Override
        public String toString() {
            return "Counting Sort";
        }
    }

    /** Your Heapsort implementation.
     */
    public static class HeapSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            // FIXME
        }

        @Override
        public String toString() {
            return "Heap Sort";
        }
    }

    /** Your Quicksort implementation.
     */
    public static class QuickSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            // FIXME
        }

        @Override
        public String toString() {
            return "Quicksort";
        }
    }

    /* For radix sorts, treat the integers as strings of x-bit numbers.  For
     * example, if you take x to be 2, then the least significant digit of
     * 25 (= 11001 in binary) would be 1 (01), the next least would be 2 (10)
     * and the third least would be 1.  The rest would be 0.  You can even take
     * x to be 1 and sort one bit at a time.  It might be interesting to see
     * how the times compare for various values of x. */

    /**
     * LSD Sort implementation.
     */
    public static class LSDSort implements SortingAlgorithm {
        @Override
        public void sort(int[] a, int k) {
            int N = Integer.min(k, a.length);
            int max = Integer.MIN_VALUE;
            for (int i = 0; i < N; i++) {
                max = Integer.max(max, a[i]);
            }

            int maxD = 0;
            for(; Math.pow(10, maxD) <= max; maxD += 1);

            ArrayList<Integer> result = new ArrayList<>();
            for (int i = 0; i < N; i++) {
                result.add(a[i]);
            }
            for (int i = 0; i < maxD; i++) {
                result = DSort(result, i);
            }
            for (int i = 0; i < N; i++) {
                a[i] = result.get(i);
            }
            // FIXME
        }

        private ArrayList<Integer> DSort(List<Integer> origin, int digit) {
            ArrayList[] buckets = new ArrayList[10];
            for (int i = 0; i < 10; i++) {
                buckets[i] = new ArrayList<Integer>();
            }
            for (int elem : origin) {
                buckets[Dsub(elem, digit)].add(elem);
            }
            ArrayList<Integer> result = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                result.addAll(buckets[i]);
            }
            return result;
        }

        private int Dsub(int N, int n) {
            String cur = String.valueOf(N);
            if (n + 1 > cur.length()) {
                return 0;
            } else {
                int now = cur.length() - 1 - n;
                return Integer.parseInt(cur.substring(now, now + 1));
            }
        }
        @Override
        public String toString() {
            return "LSD Sort";
        }
    }

    /**
     * MSD Sort implementation.
     */
    public static class MSDSort implements SortingAlgorithm {
        @Override
        public void sort(int[] a, int k) {
            // FIXME
        }

        @Override
        public String toString() {
            return "MSD Sort";
        }
    }

    /** Exchange A[I] and A[J]. */
    private static void swap(int[] a, int i, int j) {
        int swap = a[i];
        a[i] = a[j];
        a[j] = swap;
    }

}
