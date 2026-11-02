/**
 * DSA-II Assignment 2 solutions.
 * Compile: javac Assignment2.java
 * Run:     java Assignment2
 *
 * Array indices printed by this program are zero-based.
 */
public class Assignment2 {
    private static final class WindowResult {
        final int length;
        final int start;
        final int end;
        WindowResult(int length, int start, int end) {
            this.length = length; this.start = start; this.end = end;
        }
        @Override public String toString() {
            return length == 0 ? "length=0, no non-empty window" :
                    "length=" + length + ", indices=" + start + ".." + end;
        }
    }

    // Assignment 2A: recompute every size-k sum from scratch.
    static long maxFixedWindowReference(int[] values, int k) {
        validateWindowSize(values.length, k);
        long best = Long.MIN_VALUE;
        for (int start = 0; start <= values.length - k; start++) {
            long sum = 0;
            for (int i = start; i < start + k; i++) sum += values[i];
            best = Math.max(best, sum);
        }
        return best;
    }

    // Assignment 2A: keep a running sum, adding the entering value and removing
    // the value that leaves. Long sums avoid overflow for ordinary int arrays.
    static long maxFixedWindow(int[] values, int k) {
        validateWindowSize(values.length, k);
        long sum = 0;
        for (int i = 0; i < k; i++) sum += values[i];
        long best = sum;
        for (int right = k; right < values.length; right++) {
            sum += values[right] - (long) values[right - k];
            best = Math.max(best, sum);
        }
        return best;
    }

    // Assignment 2B: enumerate every start and extend each subarray.
    // The input values must be non-negative and limit must be non-negative.
    static WindowResult longestAtMostKReference(int[] values, long limit) {
        validateNonNegativeInput(values, limit);
        int bestLength = 0, bestStart = -1, bestEnd = -1;
        for (int start = 0; start < values.length; start++) {
            long sum = 0;
            for (int end = start; end < values.length; end++) {
                sum += values[end];
                if (sum <= limit && end - start + 1 > bestLength) {
                    bestLength = end - start + 1;
                    bestStart = start;
                    bestEnd = end;
                }
            }
        }
        return new WindowResult(bestLength, bestStart, bestEnd);
    }

    // Assignment 2B: variable-size window. Correct because values are
    // non-negative: removing from the left cannot increase the running sum.
    static WindowResult longestAtMostK(int[] values, long limit) {
        validateNonNegativeInput(values, limit);
        int left = 0, bestLength = 0, bestStart = -1, bestEnd = -1;
        long sum = 0;
        for (int right = 0; right < values.length; right++) {
            sum += values[right];
            while (left <= right && sum > limit) sum -= values[left++];
            int length = right - left + 1;
            if (length > bestLength) {
                bestLength = length;
                bestStart = left;
                bestEnd = right;
            }
        }
        return new WindowResult(bestLength, bestStart, bestEnd);
    }

    private static void validateWindowSize(int n, int k) {
        if (k < 1 || k > n) throw new IllegalArgumentException("Require 1 <= K <= array length.");
    }

    private static void validateNonNegativeInput(int[] values, long limit) {
        if (limit < 0) throw new IllegalArgumentException("K must be non-negative.");
        for (int value : values) {
            if (value < 0) throw new IllegalArgumentException("Variable-window input values must be non-negative.");
        }
    }

    public static void main(String[] args) {
        System.out.println("================================================================================");
        System.out.println("      PROGRAMMING ASSIGNMENT 2: SLIDING WINDOW TECHNIQUE (JAVA)");
        System.out.println("================================================================================\n");

        // --- PART A ---
        System.out.println("################################################################################");
        System.out.println("PART A: MAXIMUM SUM SUBARRAY OF FIXED SIZE K");
        System.out.println("################################################################################");

        int[][] testsA = {
            {2, 1, 5, 1, 3, 2},
            {-4, -2, -7},
            {1, 4, 2, 10, 2, 3, 1, 0, 20},
            {4, 7, 2, 9, 1},
            {3, 2, 5}
        };
        int[] ksA = {3, 2, 4, 1, 3};
        long[] expectedA = {9, -6, 24, 9, 10};

        for (int i = 0; i < testsA.length; i++) {
            long ref = maxFixedWindowReference(testsA[i], ksA[i]);
            long opt = maxFixedWindow(testsA[i], ksA[i]);
            boolean pass = (ref == expectedA[i] && opt == expectedA[i]);
            System.out.printf("Test A.%d: [%s] k=%d | Ref=%d | Opt=%d | Expected=%d%n",
                    (i + 1), (pass ? "PASS" : "FAIL"), ksA[i], ref, opt, expectedA[i]);
        }

        System.out.println("\n--- Part A Dry-Run: values=[2, 1, 5, 1, 3, 2], k=3 ---");
        int[] demoA = {2, 1, 5, 1, 3, 2};
        int demoK = 3;
        long winSum = 0;
        for (int i = 0; i < demoK; i++) winSum += demoA[i];
        long winBest = winSum;
        System.out.println("Step 1: Initial window [0..2] sum=" + winSum + ", max=" + winBest);
        for (int r = demoK; r < demoA.length; r++) {
            int outVal = demoA[r - demoK];
            int inVal = demoA[r];
            winSum += inVal - outVal;
            winBest = Math.max(winBest, winSum);
            System.out.printf("Step %d: Window [%d..%d] out=%d, in=%d, sum=%d, max=%d%n",
                    (r - demoK + 2), (r - demoK + 1), r, outVal, inVal, winSum, winBest);
        }

        // --- PART B ---
        System.out.println("\n################################################################################");
        System.out.println("PART B: LONGEST SUBARRAY WITH SUM AT MOST K (NON-NEGATIVE)");
        System.out.println("################################################################################");

        int[][] testsB = {
            {1, 2, 1, 1, 3},
            {4, 2, 1},
            {0, 0, 1, 0},
            {1, 1, 1, 1},
            {5}
        };
        long[] limitsB = {4, 0, 1, 5, 5};
        int[] expectedLenB = {3, 0, 4, 4, 1};

        for (int i = 0; i < testsB.length; i++) {
            WindowResult ref = longestAtMostKReference(testsB[i], limitsB[i]);
            WindowResult opt = longestAtMostK(testsB[i], limitsB[i]);
            boolean pass = (ref.length == expectedLenB[i] && opt.length == expectedLenB[i]);
            System.out.printf("Test B.%d: [%s] limit=%d | Ref: %s | Opt: %s | Expected Len=%d%n",
                    (i + 1), (pass ? "PASS" : "FAIL"), limitsB[i], ref, opt, expectedLenB[i]);
        }

        System.out.println("\n================================================================================");
        System.out.println("                     ALL TESTS COMPLETED SUCCESSFULLY!");
        System.out.println("================================================================================");
    }
}