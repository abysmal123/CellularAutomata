package ca.periodic;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

public class PeriodicReversibility {
    public static boolean reversible(String r) {
        int d = checkLength(r);
        boolean[] RULE = getRule(r);
        Map<PTNode, PTNode[]> edges = new HashMap<>();
        Queue<PTNode> processList = new ArrayDeque<>();
        PTNode root = PTNode.getSelfNode(d - 1);
        processList.offer(root);
        edges.put(root, root.getChildren(RULE));
        while (!processList.isEmpty()) {
            PTNode cur = processList.poll();
            if (cur != root && cur.isEden()) {
                return false;
            }
            PTNode[] children = edges.get(cur);
            for (int k = 0; k < 2; k++) {
                if (!edges.containsKey(children[k])) {
                    processList.offer(children[k]);
                    edges.put(children[k], children[k].getChildren(RULE));
                }
            }
        }
        return true;
    }

    // 运行“判断循环边界下的严格可逆性”的算法，并返回算法结束时节点的数量
    public static int countNodeWhenReturn(String r) {
        int d = checkLength(r);
        int count = 0;
        boolean[] RULE = getRule(r);
        Map<PTNode, PTNode[]> edges = new HashMap<>();
        Queue<PTNode> processList = new ArrayDeque<>();
        PTNode root = PTNode.getSelfNode(d - 1);
        processList.offer(root);
        edges.put(root, root.getChildren(RULE));
        while (!processList.isEmpty()) {
            PTNode cur = processList.poll();
            count++;
            if (cur != root && cur.isEden()) {
                return count;
            }
            PTNode[] children = edges.get(cur);
            for (int k = 0; k < 2; k++) {
                if (!edges.containsKey(children[k])) {
                    processList.offer(children[k]);
                    edges.put(children[k], children[k].getChildren(RULE));
                }
            }
        }
        return count;
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
