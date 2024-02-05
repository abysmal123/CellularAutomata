package ca.nullboundary;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

public class NullReversibility {
    public static boolean reversible(String r, int l_radius) {
        int d = checkLength(r);
        int r_radius = d - l_radius - 1;
        boolean[] RULE = getRule(r);
        Map<NTNode, NTNode[]> edges = new HashMap<>();
        Queue<NTNode> processList = new ArrayDeque<>();
        NTNode root = NTNode.getRootNode(d - 1, l_radius);
        processList.offer(root);
        edges.put(root, root.getChildren(RULE));
        while (!processList.isEmpty()) {
            NTNode cur = processList.poll();
            if (cur.isEden(r_radius)) {
                return false;
            }
            NTNode[] children = edges.get(cur);
            for (int k = 0; k < 2; k++) {
                if (!edges.containsKey(children[k])) {
                    processList.offer(children[k]);
                    edges.put(children[k], children[k].getChildren(RULE));
                }
            }
        }
        return true;
    }

    private static int checkLength(String r) {
        int d = 0, len = r.length();
        while (len > 1) {
            if ((len & 1) == 1) {
                throw new IllegalArgumentException("规则长度不为2的整数幂。");
            }
            d++;
            len >>= 1;
        }
        if (d < 3) {
            throw new IllegalArgumentException("直径至少为3。");
        }
        return d;
    }

    private static boolean[] getRule(String r) {
        int len = r.length();
        boolean[] rule = new boolean[len];
        for (int i = 0; i < len; i++) {
            rule[len - i - 1] = (r.charAt(i) == '1');
        }
        return rule;
    }
}
