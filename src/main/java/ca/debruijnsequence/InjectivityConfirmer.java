package ca.debruijnsequence;

import ca.catools.Tools;

import java.util.Arrays;

public final class InjectivityConfirmer {
    private final int d;
    private final String deBruijnSequence;
    private final int[] c0;
    private int[] rule;

    public void setRule(int[] rule) {
        this.rule = rule;
    }

    public int[] printNextConfig(int[] conf) {
        int[] c1 = nextConfig(conf);
        System.out.println(Arrays.toString(c1));
        return c1;
    }

    public InjectivityConfirmer(int _d) {
        d = _d;
        deBruijnSequence = DeBruijnSequence.getSequence(_d);
        c0 = parseIntArray(deBruijnSequence);
    }

    public boolean confirmT2(String r) {
        rule = Tools.getRuleAsIntArray(r, 1 << d);
        int[] c2 = nextConfig(nextConfig(c0));
        return cycleEquals(c2);
    }

    private int[] nextConfig(int[] conf) {
        int m = conf.length, ne = 0, MASK = (1 << d) - 1;
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

    private boolean cycleEquals(int[] conf2) {
        String s2 = parseString(conf2);
        s2 += s2;
        return s2.contains(deBruijnSequence);
    }
}
