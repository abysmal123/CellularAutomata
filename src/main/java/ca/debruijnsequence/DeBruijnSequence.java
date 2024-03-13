package ca.debruijnsequence;

public final class DeBruijnSequence {
    public static String getSequence(int d) {
        if (d < 1 || d > 20) {
            throw new IllegalArgumentException("valid: [1, 20], input: " + d);
        }
        MASK = (1 << (d - 1)) - 1;
        used = new boolean[1 << d];
        sequence = new StringBuilder();
        dfs(0);
        return sequence.toString();
    }

    private static boolean[] used;
    private static int MASK;
    private static StringBuilder sequence;

    private static void dfs(int cur) {
        for (int i = 0; i < 2; i++) {
            int next = cur * 2 + i;
            if (!used[next]) {
                used[next] = true;
                dfs(next & MASK);
                sequence.append(i);
            }
        }
    }
}
