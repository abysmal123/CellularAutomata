/* 
 * 		F3域、直径3、零边界可逆性
 *  
 *  */
package ca.nullboundaryf3;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public final class D3 {
	public static int[] RULE;
	public static Map<Integer, int[]> edges;
	public static Set<Integer> edens;
	public static boolean hasReversibleLayer;
	public static boolean hasIrreversibleLayer;
	public static int threshold = 0;
	
// public:
	public static void initializeRule(final String r) {		// 由规则构造图
		RULE = getRule(r);
		edges = new HashMap<>();
		edens = new HashSet<>();
		hasReversibleLayer = false;
		hasIrreversibleLayer = false;
		Queue<Integer> processList = new ArrayDeque<>();
		int MASK = 73;
		int root = 7;
		processList.offer(root);
		edges.put(root, new int[3]);
		while (!processList.isEmpty()) {
			int cur = processList.poll();
			if ((cur & MASK) == 0) {
				edens.add(cur);
			}
			int[] childs = edges.get(cur);
			for (int i = 0; i < 9; i++) {
				if (((cur >> i) & 1) == 1) {	
					int head = i * 3;
					for (int tail = 0; tail < 3; tail++) {
						childs[RULE[head + tail]] |= (1 << ((head + tail) % 9));
					}
				}
			}
			for (int child : childs) {
				if (!edges.containsKey(child)) {
					processList.offer(child);
					edges.put(child, new int[3]);
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
		int root = 7;
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
	
	
	public static int[] getRule(String r) {
		if (r.length() != 27) {
			throw new IllegalArgumentException("规则长度必须为27。 输入长度：" + r.length());
		}
		int[] rule = new int[27];
		for (int i = 0; i < 27; i++) {
			int b = r.charAt(26 - i) - '0';
			if (b < 0 || b > 2) {
				throw new IllegalArgumentException("规则串必须仅由012组成。");
			}
			rule[i] = b;
		}
		return rule;
	}
	
	public static String toTernaryString(int[] rule) {
		StringBuilder sb = new StringBuilder();
		for (int b : rule) {
			sb.append(b);
		}
		return sb.reverse().toString();
	}
	
	public static void setThreshold(int m) {
		threshold = m;
	}
	
// main:
	public static void main(String[] args) throws Exception {
		String r = "222122122001001000110210211";
		int n = 10005;
		initializeRule(r);
		boolean[] result = reversibilityBefore(n);
		for (int i = n - 1; i < n; i++) {
			System.out.println((i + 1) + ":\t" + result[i]);
		}
//		System.out.println(hasReversibleLayer);
		
	}
	
}
