package ca.periodicfp;

import java.util.*;

/**
 * 使用Amoroso图判断循环边界F8直径3规则的可逆性
 * @author mjc
 */
public class PeriodicReversibilityF8D3 {

    private static final Map<String ,Boolean> memo;  // 缓存

    static {
        memo = new HashMap<>();
    }

    /**
     * 判断给定规则是否可逆
     * @param r 表示局部规则Wolfram数的字符串
     * @return 规则是否可逆
     */
    public static boolean isReversible(String r) {
        if (memo.containsKey(r)) {
            return memo.get(r);
        }
        PeriodicNodeF8D3.setRule(r);
        Set<PeriodicNodeF8D3> nodeSet = new HashSet<>();
        Queue<PeriodicNodeF8D3> nodeQueue = new ArrayDeque<>();
        PeriodicNodeF8D3 root = PeriodicNodeF8D3.getRoot();
        nodeQueue.offer(root);
        nodeSet.add(root);
        while (!nodeQueue.isEmpty()) {
            PeriodicNodeF8D3 cur = nodeQueue.poll();
            for (PeriodicNodeF8D3 child : cur.getChildren()) {
                if (!nodeSet.contains(child)) {
                    if (child.isGOE()) {
                        memo.put(r, false);
                        return false;
                    }
                    nodeSet.add(child);
                    nodeQueue.offer(child);
                }
            }
        }
        memo.put(r, true);
        return true;
    }

    /**
     * 求给定规则的所有Garden-Of-Eden
     * @param r 表示局部规则Wolfram数的字符串
     * @return 此规则的Garden-Of-Eden的列表
     */
    public static List<String> getAllGOEs(String r) {
        List<String> goeList = new ArrayList<>();
        PeriodicNodeF8D3.setRule(r);
        Map<PeriodicNodeF8D3, String> nodeMap = new HashMap<>();
        Queue<PeriodicNodeF8D3> nodeQueue = new ArrayDeque<>();
        PeriodicNodeF8D3 root = PeriodicNodeF8D3.getRoot();
        nodeQueue.offer(root);
        nodeMap.put(root, "");
        while (!nodeQueue.isEmpty()) {
            PeriodicNodeF8D3 cur = nodeQueue.poll();
            PeriodicNodeF8D3[] children = cur.getChildren();
            for (int i = 0; i < PeriodicNodeF8D3.P; i++) {
                PeriodicNodeF8D3 child = children[i];
                if (!nodeMap.containsKey(child)) {
                    String seq = nodeMap.get(cur) + i;
                    nodeMap.put(child, seq);
                    nodeQueue.offer(child);
                    if (child.isGOE()) {
                        goeList.add(seq);
                    }
                }
            }
        }
        return goeList;
    }
}
