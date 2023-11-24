package ca.reflective;

import java.util.*;

public class D5 {
    public static Map<Integer, int[]> edges;
    public static Set<Integer> edens;
    public static boolean hasReversibleLayer;
    public static boolean hasIrreversibleLayer;
    public static int threshold = 0;

    public static void initializeRule(final String r) {		// 由规则构造图

        int[] rule = getRule(r);
        edges = new HashMap<>();
        edens = new HashSet<>();
        hasReversibleLayer = false;
        hasIrreversibleLayer = false;
        Queue<Integer> processList = new ArrayDeque<>();
        int root = 33345;
        processList.offer(root);
        edges.put(root, new int[2]);
        while (!processList.isEmpty()) {
            int cur = processList.poll();
            if ((cur & root) == 0) {
                edens.add(cur);
            }
            int[] children = edges.get(cur);
            for (int i = 0; i < 16; i++) {
                if (((cur >> i) & 1) == 1) {
                    int head = (i << 1);
                    for (int tail = 0; tail < 2; tail++) {
                        children[rule[head + tail]] |= (1 << ((head + tail) % 16));
                    }
                }
            }
            for (int child : children) {
                if (!edges.containsKey(child)) {
                    processList.offer(child);
                    edges.put(child, new int[2]);
                }
            }
        }
    }

    public static boolean[] reversibilityBefore(int n) throws Exception {	// 前n层每层的可逆性

        if (edges == null) {
            throw new Exception("未初始化规则。 Rule uninitialized.");
        }
        boolean[] ret = new boolean[n + 1];
        Arrays.fill(ret, true);
        @SuppressWarnings("unchecked")
        Set<Integer>[] dp = new Set[2];
        dp[0] = new HashSet<>();
        dp[1] = new HashSet<>();
        int root = 33345;
        dp[0].add(root);
        for (int i = 0; i < n; i++) {
            boolean flag = true;
            int now = i & 1, next = (i + 1) & 1;
            for (Iterator<Integer> it = dp[now].iterator(); it.hasNext();) {
                int cur = it.next();
                int[] children = edges.get(cur);
                for (int child : children) {
                    dp[next].add(child);
                    if (edens.contains(child)) {
                        ret[i] = false;
                        flag = false;
                        if (i < threshold) continue;
                        hasIrreversibleLayer = true;
                    }
                }
                it.remove();
            }
            if (i < threshold) continue;
            if (flag) {
                hasReversibleLayer = true;
            }
        }
        return ret;
    }

    public static int getPeriod() throws Exception {		// 计算节点在层中出现规律的周期

        if (edges == null) {
            throw new Exception("未初始化规则。 Rule uninitialized.");
        }
        Map<String, Integer> condMap = new HashMap<>();
        boolean[][] dp = new boolean[2][65536];
        int root = 33345;
        dp[0][root] = true;
        for (int i = 0;; i++) {
            int now = i & 1, next = (i + 1) & 1;
            String cond = hash(dp[now]);
            if (condMap.containsKey(cond)) {
                return i - condMap.get(cond);
            }
            condMap.put(cond, i);
            for (int cur = 0; cur < 65536; cur++) {
                if (!dp[now][cur]) continue;
                int[] children = edges.get(cur);
                for (int child : children) {
                    dp[next][child] = true;
                }
                dp[now][cur] = false;
            }
        }
    }

    private static int[] getRule(String r) {

        if (r.length() != 32) {
            throw new IllegalArgumentException("规则长度必须为32。 "
                    + "Length of input rule must be 32. Input rule: " + r);
        }
        int[] rule = new int[32];
        for (int i = 0; i < 32; i++) {
            if (r.charAt(i) == '1') {
                rule[31 - i] = 1;
            } else if (r.charAt(i) != '0') {
                throw new IllegalArgumentException("规则必须为01串。"
                        + "Input rule must be binary. Input rule: " + r);
            }
        }
        return rule;
    }

    private static String hash(boolean[] distribution) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 1024; i++) {
            long temp = 0;
            for (int j = 0; j < 64; j++) {
                temp <<= 1;
                temp += distribution[i * 64 + j] ? 1 : 0;
            }
            sb.append(temp);
            if (i < 1023) sb.append(".");
        }
        return sb.toString();
    }

    public static void setThreshold(int m) {

        threshold = m;
    }

    // main:
    public static void main(String[] args) throws Exception {

    }

}
