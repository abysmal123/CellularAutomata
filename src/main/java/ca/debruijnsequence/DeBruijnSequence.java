package ca.debruijnsequence;

public final class DeBruijnSequence {
    public static String getSequence(int d) {
        if (d < 1 || d > 20) {
            throw new IllegalArgumentException("valid: [1, 20], input: " + d);
        }
        int len = 1 << d, MASK = (1 << (d - 1)) - 1;
        boolean[] used = new boolean[len];
        StringBuilder sequence = new StringBuilder();
        for (int i = 0, cur = 0; i < len; i++) {
            int edge = cur * 2 + 1;
            if (!used[edge]) {
                used[edge] = true;
                sequence.append(1);
                cur = edge & MASK;
                continue;
            }
            edge--;
            used[edge] = true;
            sequence.append(0);
            cur = edge & MASK;
        }
        return sequence.toString();
    }
}
