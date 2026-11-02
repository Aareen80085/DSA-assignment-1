import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * DSA-II Assignment 1 solutions.
 * Compile: javac Assignment1.java
 * Run:     java Assignment1
 *
 * Array indices printed by this program are zero-based.
 */
public class Assignment1 {
    private static final class PairResult {
        final int first;
        final int second;
        PairResult(int first, int second) { this.first = first; this.second = second; }
        @Override public String toString() { return "(" + first + ", " + second + ")"; }
    }

    // Assignment 1A: simple reference implementation. Input must be sorted.
    // Returns the distinct count; the input array itself is left unchanged.
    static int removeDuplicatesReference(int[] sorted) {
        List<Integer> unique = new ArrayList<>();
        for (int value : sorted) {
            if (!unique.contains(value)) unique.add(value);
        }
        for (int i = 0; i < unique.size(); i++) sorted[i] = unique.get(i);
        return unique.size();
    }

    // Assignment 1A: in-place slow/fast pointer solution. Input must be sorted.
    static int removeDuplicatesInPlace(int[] sorted) {
        if (sorted.length == 0) return 0;
        int write = 1;
        for (int read = 1; read < sorted.length; read++) {
            if (sorted[read] != sorted[write - 1]) sorted[write++] = sorted[read];
        }
        return write;
    }

    // Assignment 1B: brute-force search. Returns zero-based indices or (-1, -1).
    static PairResult pairSumReference(int[] sorted, long target) {
        for (int i = 0; i < sorted.length; i++) {
            for (int j = i + 1; j < sorted.length; j++) {
                if ((long) sorted[i] + sorted[j] == target) return new PairResult(i, j);
            }
        }
        return new PairResult(-1, -1);
    }

    // Assignment 1B: opposite-end pointers. Input must be sorted.
    static PairResult pairSumTwoPointers(int[] sorted, long target) {
        int left = 0, right = sorted.length - 1;
        while (left < right) {
            long sum = (long) sorted[left] + sorted[right];
            if (sum == target) return new PairResult(left, right);
            if (sum < target) left++;
            else right--;
        }
        return new PairResult(-1, -1);
    }

    private static int[] copy(int[] a) { return Arrays.copyOf(a, a.length); }

    public static void main(String[] args) {
        System.out.println("Assignment 1A: remove duplicates");
        int[] duplicateInput = {1, 1, 2, 2, 2, 3, 4, 4};
        int[] ref = copy(duplicateInput), opt = copy(duplicateInput);
        int refCount = removeDuplicatesReference(ref);
        int optCount = removeDuplicatesInPlace(opt);
        System.out.println("Reference: k=" + refCount + ", prefix=" + Arrays.toString(Arrays.copyOf(ref, refCount)));
        System.out.println("In-place:  k=" + optCount + ", prefix=" + Arrays.toString(Arrays.copyOf(opt, optCount)));

        System.out.println("\nAssignment 1B: pair sum");
        int[] pairInput = {1, 2, 4, 7, 11, 15};
        System.out.println("Reference indices: " + pairSumReference(pairInput, 15));
        System.out.println("Two-pointer indices: " + pairSumTwoPointers(pairInput, 15));
        System.out.println("No pair: " + pairSumTwoPointers(new int[]{2, 4, 6}, 20));
    }
}