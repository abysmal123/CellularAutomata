package ca.nonlinear;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public final class SpecificNumberZeroECA {
	
// public:
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
		int MASK = 5;
		int root = 3;
		processList.offer(root);
		edges.put(root, new int[2]);
		while (!processList.isEmpty()) {
			int cur = processList.poll();
			if ((cur & MASK) == 0) {
				edens.add(cur);
			}
			int[] childs = edges.get(cur);
			for (int i = 0; i < 4; i++) {
				if (((cur >> i) & 1) == 1) {	
					int head = (i << 1);
					for (int tail = 0; tail < 2; tail++) {
						childs[((rule >> (head + tail)) & 1)] |= (1 << ((head + tail) % 4));
					}
				}
			}
			for (int child : childs) {
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
		int root = 3;
		dp[0].add(root);
		for (int i = 0; i < n; i++) {
			boolean flag = true;
			int now = i & 1, next = (i + 1) & 1;
			for (Iterator<Integer> it = dp[now].iterator(); it.hasNext();) {
				int cur = it.next();
				int[] childs = edges.get(cur);
				for (int child : childs) {
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
		
		String r = "1001011000011110";
		int n = 1000;
		initializeRule(r);
		boolean[] result = reversibilityBefore(n);
		for (int i = 0; i < n; i++) {
			System.out.println((i + 1) + ":\t" + result[i]);
		}
	}
	
}
