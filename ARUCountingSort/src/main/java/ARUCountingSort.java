public class ARUCountingSort {
    public static void sort(int[] arr){
        if(arr==null || arr.length<=1){
            return;
        }
        int n = arr.length;
        //find max value k
        int k = getMax(arr);
        if(k==0){
            return;
        }
        // compute m = ceil(sqrt(k))
        int m = (int) Math.ceil(Math.sqrt(k));
        if(m<1){
            m = 1;
        }
       //Step 1:Sort stably by the remainder to obtain the middle array S
        int[] S = sortByRemainder(arr, m, n);
        //Step 2:Sort stably by quotient and return to the original array from S
        sortByQuotient(S, arr, m, n);
    }
    private static int[] sortByRemainder(int[] arr, int m, int n){
        int[] R = new int[m+1];
        //Count the frequency of the remainder
        for (int val : arr) {
            R[val % m]++;
        }
        //pre
        for(int i = 1; i <= m; i++){
            R[i] += R[i-1];
        }
        //Fill S from back to front (to ensure stability)
        int[] S = new int[n];
        for (int i = n-1; i >=0; i--) {
            int r = arr[i] % m;
            int pos = R[r] - 1;
            S[pos] = arr[i];
            R[r]--;
        }
        return S;
    }
    private static void sortByQuotient(int[] S, int[] result, int n,int m){
        int[] Q = new int[m+1];
        //Statistical quotient frequency
        for (int val : S) {
            Q[val/m]++;
        }
        //pre
        for (int i = 1; i <= m; i++) {
            Q[i] += Q[i - 1];
        }
        //Fill "result" from back to front
        for (int i = n - 1; i >= 0; i--) {
            int q = S[i] / m;
            int pos = Q[q] - 1;
            result[pos] = S[i];
            Q[q]--;
        }
    }
    private static int getMax(int[] arr){
        int max = arr[0];
        for(int val : arr){
            if(val>max){
                max = val;
            }
        }
        return max;
    }
}
