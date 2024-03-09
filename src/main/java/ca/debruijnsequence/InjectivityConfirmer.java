package ca.debruijnsequence;

import ca.catools.Tools;

public final class InjectivityConfirmer {
    private final int d;
    private int[] rule;

    public InjectivityConfirmer(int _d) {
        d = _d;
    }

    public boolean confirmT2(String r) {
        int len = 1 << d;
        rule = Tools.getRuleAsIntArray(r, len);
        int[] c0 = parseIntArray(DeBruijnSequence.getSequence(d));
        int[] c2 = nextConfig(nextConfig(c0));
        return cycleEquals(c0, c2);
    }

    private int[] nextConfig(int[] conf) {
        int m = conf.length, ne = 0, MASK = (1 << (d - 1)) - 1;
        int[] ret = new int[m];
        for (int i = 0; i < d - 1; i++) {
            ne = (ne << 1) + conf[i];
        }
        for (int i = 0; i < m; i++) {
            ne = ((ne << 1) & MASK) + conf[(i + d - 1) % m];
            ret[i] = rule[ne];
        }
        return ret;
    }

    private static int[] parseIntArray(String seq) {
        int m = seq.length();
        int[] ret = new int[m];
        for (int i = 0; i < m; i++) {
            ret[i] = seq.charAt(i) - '0';
        }
        return ret;
    }

    private static String parseString(int[] conf) {
        StringBuilder ret = new StringBuilder();
        for (int state : conf) {
            ret.append(state);
        }
        return ret.toString();
    }

    private static boolean cycleEquals(int[] conf1, int[] conf2) {
        String s1 = parseString(conf1), s2 = parseString(conf2);
        s2 += s2;
        return s2.contains(s1);
    }
}
