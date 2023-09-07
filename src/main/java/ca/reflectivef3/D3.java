/* 
 * 		F3域、直径3、零边界可逆性
 *  
 *  */
package ca.reflectivef3;

import java.util.*;

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
		int root = 273;		// 00(2^0 = 1), 11(2^4 = 16), 22(2^8 = 256)
		processList.offer(root);
		edges.put(root, new int[3]);
		while (!processList.isEmpty()) {
			int cur = processList.poll();
			if ((cur & root) == 0) {
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
		int root = 273;
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

	public static int getPeriod() throws Exception {		// 计算节点在层中出现规律的周期

		if (edges == null) {
			throw new Exception("未初始化规则。 Rule uninitialized.");
		}
		Map<String, Integer> condMap = new HashMap<>();
		boolean[][] dp = new boolean[2][512];
		int root = 273;
		dp[0][root] = true;
		for (int i = 0;; i++) {
			int now = i & 1, next = (i + 1) & 1;
			String cond = hash(dp[now]);
			if (condMap.containsKey(cond)) {
				return i - condMap.get(cond);
			}
			condMap.put(cond, i);
			for (int cur = 0; cur < 512; cur++) {
				if (!dp[now][cur]) continue;
				int[] children = edges.get(cur);
				for (int child : children) {
					dp[next][child] = true;
				}
				dp[now][cur] = false;
			}
		}
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

	private static String hash(boolean[] distribution) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 8; i++) {
			long temp = 0;
			for (int j = 0; j < 64; j++) {
				temp <<= 1;
				temp += distribution[i * 64 + j] ? 1 : 0;
			}
			sb.append(temp);
			if (i < 7) sb.append(".");
		}
		return sb.toString();
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
		String r = ca.f3.RuleGenerator.linearRule(1, 2, 2, 0);
		int n = 100;
		initializeRule(r);
		boolean[] result = reversibilityBefore(n);
		for (int i = 0; i < n; i++) {
			System.out.println((i + 1) + ":\t" + result[i]);
		}
	}
	
}
