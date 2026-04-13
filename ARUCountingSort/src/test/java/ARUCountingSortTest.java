public class ARUCountingSortTest {

    /**
     * Sorts an array of non-negative integers using a two-pass counting sort.
     * The algorithm first sorts by remainder (value % m), then by quotient (value / m),
     * where m = ceil(sqrt(max)). This yields a stable sort with linear time complexity.
     *
     * @param arr the input array (modified in place)
     */
    public static void sort(int[] arr) {
        // Edge cases: null or small arrays are already sorted
        if (arr == null || arr.length <= 1) {
            return;
        }
        int n = arr.length;

        // Find the maximum value in the array
        int k = getMax(arr);
        if (k == 0) {
            return; // all elements are zero, already sorted
        }

        // Compute m = ceil(sqrt(k)). This balances the two passes.
        int m = (int) Math.ceil(Math.sqrt(k));
        if (m < 1) {
            m = 1;
        }

        // Step 1: Sort stably by the remainder (arr[i] % m), producing a temporary array S
        int[] S = sortByRemainder(arr, m, n);

        // Step 2: Sort stably by the quotient (arr[i] / m) from S, writing back into arr
        sortByQuotient(S, arr, m, n);
    }

    /**
     * Performs a stable counting sort on the input array based on the remainder (value % m).
     *
     * @param arr the original array
     * @param m   the divisor (used for remainder calculation)
     * @param n   length of the array
     * @return a new array sorted by remainder (stable)
     */
    private static int[] sortByRemainder(int[] arr, int m, int n) {
        // Counting array for remainders: indices 0..m (inclusive because remainder can be m? Actually remainder is in [0, m-1], but we allocate m+1 for safety)
        int[] R = new int[m + 1];

        // Count frequency of each remainder
        for (int val : arr) {
            R[val % m]++;
        }

        // Prefix sum to compute starting positions (right boundaries)
        for (int i = 1; i <= m; i++) {
            R[i] += R[i - 1];
        }

        // Build the result array S by scanning from right to left (ensures stability)
        int[] S = new int[n];
        for (int i = n - 1; i >= 0; i--) {
            int r = arr[i] % m;
            int pos = R[r] - 1; // position to place the element (0‑based)
            S[pos] = arr[i];
            R[r]--; // move the boundary left
        }
        return S;
    }

    /**
     * Performs a stable counting sort on the array S based on the quotient (value / m),
     * and writes the result back into the original array.
     *
     * @param S      array sorted by remainder (from previous step)
     * @param result the original array (will be overwritten with final sorted order)
     * @param n      length of the array
     * @param m      the divisor (used for quotient calculation)
     */
    private static void sortByQuotient(int[] S, int[] result, int n, int m) {
        // Counting array for quotients: indices 0..m (quotients range from 0 to max/m, but m is safe upper bound)
        int[] Q = new int[m + 1];

        // Count frequency of each quotient
        for (int val : S) {
            Q[val / m]++;
        }

        // Prefix sum to compute starting positions
        for (int i = 1; i <= m; i++) {
            Q[i] += Q[i - 1];
        }

        // Fill the result array from back to front (stable sort)
        for (int i = n - 1; i >= 0; i--) {
            int q = S[i] / m;
            int pos = Q[q] - 1;
            result[pos] = S[i];
            Q[q]--;
        }
    }

    /**
     * Returns the maximum value in the given array.
     *
     * @param arr non‑empty array
     * @return the largest element
     */
    private static int getMax(int[] arr) {
        int max = arr[0];
        for (int val : arr) {
            if (val > max) {
                max = val;
            }
        }
        return max;
    }
}
