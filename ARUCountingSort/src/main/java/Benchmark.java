import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

public class Benchmark {

    // ======================= ARU Counting Sort =======================
    static class ARUCountingSort {
        public static void sort(int[] arr) {
            if (arr == null || arr.length <= 1) return;
            int n = arr.length;
            int k = getMax(arr);
            if (k == 0) return;
            int m = (int) Math.ceil(Math.sqrt(k));
            if (m < 1) m = 1;

            int[] S = sortByRemainder(arr, m, n);
            sortByQuotient(S, arr, m, n);
        }

        // Stable sort by remainder to produce intermediate array S
        private static int[] sortByRemainder(int[] arr, int m, int n) {
            int[] R = new int[m + 1];
            for (int val : arr) R[val % m]++;
            for (int i = 1; i <= m; i++) R[i] += R[i - 1];
            int[] S = new int[n];
            for (int i = n - 1; i >= 0; i--) {
                int r = arr[i] % m;
                int pos = R[r] - 1;
                S[pos] = arr[i];
                R[r]--;
            }
            return S;
        }

        // Stable sort by quotient to get final sorted array
        private static void sortByQuotient(int[] S, int[] result, int m, int n) {
            // Dynamically compute the maximum quotient to avoid index out of bounds
            int maxQ = 0;
            for (int val : S) {
                int q = val / m;
                if (q > maxQ) maxQ = q;
            }
            int[] Q = new int[maxQ + 1];
            for (int val : S) Q[val / m]++;
            for (int i = 1; i < Q.length; i++) Q[i] += Q[i - 1];
            for (int i = n - 1; i >= 0; i--) {
                int q = S[i] / m;
                int pos = Q[q] - 1;
                result[pos] = S[i];
                Q[q]--;
            }
        }

        private static int getMax(int[] arr) {
            int max = arr[0];
            for (int v : arr) if (v > max) max = v;
            return max;
        }
    }

    // ======================= Traditional Counting Sort =======================
    static class TraditionalCountingSort {
        public static void sort(int[] arr) {
            if (arr == null || arr.length <= 1) return;
            int max = getMax(arr);
            int[] count = new int[max + 1];
            for (int val : arr) count[val]++;
            for (int i = 1; i <= max; i++) count[i] += count[i - 1];
            int[] result = new int[arr.length];
            for (int i = arr.length - 1; i >= 0; i--) {
                int val = arr[i];
                result[count[val] - 1] = val;
                count[val]--;
            }
            System.arraycopy(result, 0, arr, 0, arr.length);
        }

        private static int getMax(int[] arr) {
            int max = arr[0];
            for (int v : arr) if (v > max) max = v;
            return max;
        }
    }

    // ======================= Data Generation =======================
    private static int[] randomArray(int n, int k, Random rand) {
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = rand.nextInt(k + 1);
        }
        return arr;
    }

    // ======================= Time Measurement =======================
    private static long measureTime(int[] original, String algorithm) {
        int[] copy = original.clone();
        long start = System.nanoTime();
        try {
            switch (algorithm) {
                case "ARU":
                    ARUCountingSort.sort(copy);
                    break;
                case "Traditional":
                    TraditionalCountingSort.sort(copy);
                    break;
                case "Quick":
                    Arrays.sort(copy);
                    break;
            }
        } catch (OutOfMemoryError | ArrayIndexOutOfBoundsException e) {
            return -1;  // Indicates failure (OOM or index error)
        }
        long end = System.nanoTime();
        return end - start;
    }

    // ======================= Main Experiment =======================
    public static void main(String[] args) throws IOException {
        // Experiment parameters
        int[] ns = {1000, 10000, 100000};          // Array sizes
        int[] ks = {1000, 100000, 10_000_000};     // Maximum values (k)
        int runs = 10;                             // Repetitions per configuration
        int warmup = 3;                            // Warmup iterations

        Random rand = new Random(42);              // Fixed seed for reproducibility

        try (FileWriter csv = new FileWriter("benchmark_results.csv")) {
            csv.append("n,k,algorithm,time_ms\n");

            for (int n : ns) {
                for (int k : ks) {
                    System.out.printf("\n========== n=%d, k=%d ==========\n", n, k);

                    // Generate one original array (shared by all algorithms)
                    int[] original = randomArray(n, k, rand);

                    // Warmup
                    for (int i = 0; i < warmup; i++) {
                        ARUCountingSort.sort(original.clone());
                        try {
                            TraditionalCountingSort.sort(original.clone());
                        } catch (OutOfMemoryError | ArrayIndexOutOfBoundsException ignored) {}
                        Arrays.sort(original.clone());
                    }

                    // Measure ARU
                    long totalARU = 0;
                    int validARU = 0;
                    for (int r = 0; r < runs; r++) {
                        long t = measureTime(original, "ARU");
                        if (t >= 0) {
                            totalARU += t;
                            validARU++;
                        }
                    }
                    double avgARU = (validARU > 0) ? (totalARU / validARU) / 1_000_000.0 : -1;
                    System.out.printf("ARU        : %.3f ms (based on %d runs)\n", avgARU, validARU);
                    csv.append(String.format("%d,%d,ARU,%.3f\n", n, k, avgARU));

                    // Measure Traditional Counting Sort
                    long totalTrad = 0;
                    int validTrad = 0;
                    for (int r = 0; r < runs; r++) {
                        long t = measureTime(original, "Traditional");
                        if (t >= 0) {
                            totalTrad += t;
                            validTrad++;
                        }
                    }
                    double avgTrad = (validTrad > 0) ? (totalTrad / validTrad) / 1_000_000.0 : -1;
                    System.out.printf("Traditional: %.3f ms (based on %d runs)\n", avgTrad, validTrad);
                    csv.append(String.format("%d,%d,Traditional,%.3f\n", n, k, avgTrad));

                    // Measure QuickSort
                    long totalQuick = 0;
                    int validQuick = 0;
                    for (int r = 0; r < runs; r++) {
                        long t = measureTime(original, "Quick");
                        if (t >= 0) {
                            totalQuick += t;
                            validQuick++;
                        }
                    }
                    double avgQuick = (validQuick > 0) ? (totalQuick / validQuick) / 1_000_000.0 : -1;
                    System.out.printf("Quick      : %.3f ms (based on %d runs)\n", avgQuick, validQuick);
                    csv.append(String.format("%d,%d,Quick,%.3f\n", n, k, avgQuick));
                }
            }
        }

        System.out.println("\nExperiment finished. Results saved to benchmark_results.csv");
    }
}