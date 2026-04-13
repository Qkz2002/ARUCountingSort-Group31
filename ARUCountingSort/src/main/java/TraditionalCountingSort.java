public class TraditionalCountingSort {
    public static void sort(int[] arr) {
        if (arr == null || arr.length <= 1) {
            return;
        }
        int max = getMax(arr);
        int[] count = new int[max + 1];
        for (int i :arr) {
            count[arr[i]]++;
        }
        for (int i = 0; i < max; i++) {
            count[i] += count[i-1];
        }
        int[] result = new int[arr.length];
        for (int i = arr.length-1; i >=0; i--) {
            result[count[arr[i]]-1] = arr[i];
            count[arr[i]]--;
        }
        System.arraycopy(result, 0, arr, 0, arr.length);
    }
    private static int getMax(int[] arr) {
        int max = arr[0];
        for(int i : arr) {
            max = Math.max(max, i);
        }
        return max;
    }
}
