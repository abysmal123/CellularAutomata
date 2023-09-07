package ca.nonlinear;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public final class SpecificNumberZeroD4L1 {
	
// public:
	public static Map<Integer, int[]> edges;
	public static Set<Integer> edens;
	public static boolean hasReversibleLayer;
	public static boolean hasIrreversibleLayer;
	public static int threshold = 0;
	
	public static void initializeRule(final String r) {		// �ɹ�����ͼ
		
		int rule = getRule(r, 1 << 4);
		edges = new HashMap<>();
		edens = new HashSet<>();
		hasReversibleLayer = false;
		hasIrreversibleLayer = false;
		Queue<Integer> processList = new ArrayDeque<>();
		int MASK = 17;		// (000, 100)
		int root = 15;
		processList.offer(root);
		edges.put(root, new int[2]);
		while (!processList.isEmpty()) {
			int cur = processList.poll();
			if ((cur & MASK) == 0) {
				edens.add(cur);
			}
			int[] children = edges.get(cur);
			for (int i = 0; i < 8; i++) {
				if (((cur >> i) & 1) == 1) {	
					int head = (i << 1);
					for (int tail = 0; tail < 2; tail++) {
						children[((rule >> (head + tail)) & 1)] |= (1 << ((head + tail) % 8));
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
	
	public static boolean[] reversibilityBefore(int n) throws Exception {	// ǰn��ÿ��Ŀ�����
		
		if (edges == null) {
			throw new Exception("δ��ʼ������ Rule uninitialized.");
		}
		boolean[] ret = new boolean[n + 1];
		Arrays.fill(ret, true);
		@SuppressWarnings("unchecked")
		Set<Integer>[] dp = new Set[2];
		dp[0] = new HashSet<>();
		dp[1] = new HashSet<>();
		int root = 15;
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

	public static int getPeriod() throws Exception {		// ����ڵ��ڲ��г��ֹ��ɵ�����

		if (edges == null) {
			throw new Exception("δ��ʼ������ Rule uninitialized.");
		}
		Map<String, Integer> condMap = new HashMap<>();
		boolean[][] dp = new boolean[2][256];
		int root = 15;
		dp[0][root] = true;
		for (int i = 0;; i++) {
			int now = i & 1, next = (i + 1) & 1;
			String cond = hash(dp[now]);
			if (condMap.containsKey(cond)) {
				return i - condMap.get(cond);
			}
			condMap.put(cond, i);
			for (int cur = 0; cur < 256; cur++) {
				if (!dp[now][cur]) continue;
				int[] children = edges.get(cur);
				for (int child : children) {
					dp[next][child] = true;
				}
				dp[now][cur] = false;
			}
		}
	}
	
	private static int getRule(String r, int len) {
		
		if (r.length() != len) {
			throw new IllegalArgumentException("���򳤶ȱ���Ϊ" + len + "�� "
					+ "Length of input rule must be " + len + ". Input rule: " + r);
		}
		int rule = 0;
		for (int i = 0; i < len; i++) {
			rule <<= 1;
			if (r.charAt(i) == '1') {
				rule |= 1;
			} else if (r.charAt(i) != '0') {
				throw new IllegalArgumentException("�������Ϊ01����"
						+ "Input rule must be binary. Input rule: " + r);
			}
		}
		return rule;
	}

	private static String hash(boolean[] distribution) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 8; i++) {
			int temp = 0;
			for (int j = 0; j < 32; j++) {
				temp <<= 1;
				temp += distribution[i * 32 + j] ? 1 : 0;
			}
			sb.append(temp);
			if (i < 7) sb.append(".");
		}
		return sb.toString();
	}
	
	public static void setThreshold(int m) {
		
		threshold = m;
	}
	
// main:
	public static void main(String[] args) throws Exception {
		
		String r = "0100110110110010";
		int n = 1000;
		initializeRule(r);
		boolean[] result = reversibilityBefore(n);
		for (int i = 0; i < n; i++) {
			System.out.println((i + 1) + ":\t" + result[i]);
		}
	}
	
}
