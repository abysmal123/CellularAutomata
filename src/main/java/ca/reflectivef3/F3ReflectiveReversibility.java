package ca.reflectivef3;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

public final class F3ReflectiveReversibility {
    public static boolean reversible(String r) {
        int d = checkLength(r);
        int[] RULE = getRule(r);
        Map<F3RTNode, F3RTNode[]> edges = new HashMap<>();
        Queue<F3RTNode> processList = new ArrayDeque<>();
        F3RTNode root = F3RTNode.getPalindromeNode(d - 1);
        processList.offer(root);
        edges.put(root, root.getChildren(RULE));
        while (!processList.isEmpty()) {
            F3RTNode cur = processList.poll();
            if (cur.isEden()) {
                return false;
            }
            F3RTNode[] children = edges.get(cur);
            for (int k = 0; k < 3; k++) {
                if (!edges.containsKey(children[k])) {
                    processList.offer(children[k]);
                    edges.put(children[k], children[k].getChildren(RULE));
                }
            }
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
