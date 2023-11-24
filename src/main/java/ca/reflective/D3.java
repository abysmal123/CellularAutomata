package ca.reflective;

import java.util.*;

public class D3 {
    public static Map<Integer, int[]> edges;
    public static Set<Integer> edens;
    public static boolean hasReversibleLayer;
    public static boolean hasIrreversibleLayer;
    public static int threshold = 0;

    public static void initializeRule(final String r) {		// 由规则构造图

        int rule = getRule(r, 1 << 3);
        edges = new HashMap<>();
        edens = new HashSet<>();
        hasReversibleLayer = false;
        hasIrreversibleLayer = false;
        Queue<Integer> processList = new ArrayDeque<>();
        int root = 9;
        processList.offer(root);
        edges.put(root, new int[2]);
        while (!processList.isEmpty()) {
            int cur = processList.poll();
            if ((cur & root) == 0) {
                edens.add(cur);
            }
            int[] children = edges.get(cur);
            for (int i = 0; i < 4; i++) {
                if (((cur >> i) & 1) == 1) {
                    int head = (i << 1);
                    for (int tail = 0; tail < 2; tail++) {
                        children[((rule >> (head + tail)) & 1)] |= (1 << ((head + tail) % 4));
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
        int root = 9;
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
        int[] condMap = new int[1 << 16];
        Arrays.fill(condMap, -1);
        int[] dp = new int[2];
        int root = 3;
        dp[0] = 1 << root;
        for (int i = 0;; i++) {
            int now = i & 1, next = (i + 1) & 1;
            if (condMap[dp[now]] != -1) {
                return i - condMap[dp[now]];
            }
            condMap[dp[now]] = i;
            for (int cur = 0; cur < 16; cur++) {
                if (((dp[now] >> cur) & 1) == 0) continue;
                int[] children = edges.get(cur);
                for (int child : children) {
                    dp[next] |= (1 << child);
                }
            }
            dp[now] = 0;
        }
    }

    private static int getRule(String r, int len) {

        if (r.length() != len) {
            throw new IllegalArgumentException("规则长度必须为" + len + "。 "
                    + "Length of input rule must be " + len + ". Input rule: " + r);
        }
        int rule = 0;
        for (int i = 0; i < len; i++) {
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

    public static void setThreshold(int m) {

        threshold = m;
    }

    // main:
    public static void main(String[] args) throws Exception {

        String r = "01101001";
        int n = 100;
        initializeRule(r);
//		boolean[] result = reversibilityBefore(n);
//		for (int i = 0; i < n; i++) {
//			System.out.println((i + 1) + ":\t" + result[i]);
//		}
        System.out.println(getPeriod());
    }

}
