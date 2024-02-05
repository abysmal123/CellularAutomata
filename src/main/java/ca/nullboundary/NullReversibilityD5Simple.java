package ca.nullboundary;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

public class NullReversibilityD5Simple {
    public static boolean reversible(String r) {
        return reversible(r, 2);
    }
    public static boolean reversible(String r, int l_radius) {
        int rule = getRule(r);
        Set<Integer> nodes = new HashSet<>();
        Queue<Integer> processList = new ArrayDeque<>();
        int ROOT = 0;
        int MASK = 0;
        int r_radius = 4 - l_radius;
        for (int i = (1 << r_radius) - 1; i >= 0; i--) {
            ROOT |= (1 << i);
        }
        for (int i = (1 << l_radius) - 1; i >= 0; i--) {
            MASK |= (1 << (i << r_radius));
        }
        processList.offer(ROOT);
        while (!processList.isEmpty()) {
            int cur = processList.poll();
            if ((cur & MASK) == 0) {
                return false;
            }
            int[] children = new int[2];
            for (int i = 0; i < 16; i++) {
                if (((cur >> i) & 1) == 1) {
                    int head = (i << 1);
                    for (int tail = 0; tail < 2; tail++) {
                        children[((rule >> (head + tail)) & 1)] |= (1 << ((head + tail) % 16));
                    }
                }
            }
            for (int child : children) {
                if (!nodes.contains(child)) {
                    processList.offer(child);
                    nodes.add(child);
                }
            }
        }
        return true;
    }

    private static int getRule(String r) {

        if (r.length() != 32) {
            throw new IllegalArgumentException("规则长度必须为" + 32 + "。 "
                    + "Length of input rule must be " + 32 + ". Input rule: " + r);
        }
        int rule = 0;
        for (int i = 0; i < 32; i++) {
            rule <<= 1;
            if (r.charAt(i) == '1') {
                rule |= 1;
            } else if (r.charAt(i) != '0') {
                throw new IllegalArgumentException("规则必须为01串。"
                        + "Input rule must be binary. Input rule: " + r);
            }
        }
        return rule;
    }
}
