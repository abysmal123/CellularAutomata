package ca.nullboundaryf3;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

public final class F3NullReversibility {
    public static boolean reversible(String r, int l_radius) {
        int d = checkLength(r);
        int r_radius = d - l_radius - 1;
        int[] RULE = getRule(r);
        Map<F3NTNode, F3NTNode[]> edges = new HashMap<>();
        Queue<F3NTNode> processList = new ArrayDeque<>();
        F3NTNode root = F3NTNode.getRootNode(d - 1, l_radius);
        processList.offer(root);
        edges.put(root, root.getChildren(RULE));
        boolean skipFirst = false;
        while (!processList.isEmpty()) {
            F3NTNode cur = processList.poll();
            if (skipFirst && cur.isEden(r_radius)) {
                return false;
            }
            F3NTNode[] children = edges.get(cur);
            for (int k = 0; k < 3; k++) {
                if (!edges.containsKey(children[k])) {
                    processList.offer(children[k]);
                    edges.put(children[k], children[k].getChildren(RULE));
                }
            }
            skipFirst = true;
        }
        return true;
    }

    // private:

    private static int checkLength(String r) {
        int d = 0, len = r.length();
        while (len > 1) {
            if ((len % 3) != 0) {
                throw new IllegalArgumentException("规则长度不为3的整数幂。");
            }
            d++;
            len /= 3;
        }
        if (d < 3) {
            throw new IllegalArgumentException("直径至少为3。");
        }
        return d;
    }

    private static int[] getRule(String r) {
        int len = r.length();
        int[] rule = new int[len];
        for (int i = 0; i < len; i++) {
            rule[len - i - 1] = r.charAt(i) - '0';
        }
        return rule;
    }
}
