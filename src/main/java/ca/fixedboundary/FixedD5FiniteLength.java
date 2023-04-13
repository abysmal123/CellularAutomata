package ca.fixedboundary;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class FixedD5FiniteLength {
	
// public:
	public static void setBoundary(int[] left, int[] right) {
		
		if (left.length != 2) {
			throw new IllegalArgumentException("左边界长度必须为2。"
					+ "Length of left boundary must be 2. Input length: " + left.length);
		}
		if (right.length != 2) {
			throw new IllegalArgumentException("右边界长度必须为2。"
					+ "Length of right boundary must be 2. Input length: " + right.length);
		}
		for (int e : left) {
			if(e != 0 && e != 1) {
				throw new IllegalArgumentException("边界状态必须为0或1 。"
						+ "Boundary states must be 0 or 1.");
			}
		}
		for (int e : right) {
			if(e != 0 && e != 1) {
				throw new IllegalArgumentException("边界状态必须为0或1 。"
						+ "Boundary states must be 0 or 1.");
			}
		}
		System.arraycopy(left, 0, leftBoundary, 0, 2);
		System.arraycopy(right, 0, rightBoundary, 0, 2);
	}
	
	public static boolean hasEden(String r) {
		
		if (r.length() != 32) {
			throw new IllegalArgumentException("规则长度必须为32 。"
					+ "Length of rules must be 32. Input rules: " + r);
		}
		if (r.charAt(0) != '0' && r.charAt(0) != '1') {
			throw new IllegalArgumentException("规则必须为01串。"
					+ "Input rules must be binary. Input rules: " + r);
		}
		int rules = (r.charAt(0) == '1' ? 1 : 0);
		for (int i = 1; i < 32; i++) {
			rules <<= 1;
			if (r.charAt(i) == '1') {
				rules++;
			} else if (r.charAt(i) != '0') {
				throw new IllegalArgumentException("规则必须为01串。"
						+ "Input rules must be binary. Input rules: " + r);
			}
		}
//		if ((rules & 1) == 1) {
//			return true;
//		}
		Map<Integer, Integer> tree = new HashMap<Integer, Integer>();
		Set<Integer> visited = new HashSet<Integer>();
		Queue<Integer> nodeList = new ArrayDeque<Integer>();
		int MASK = calMask();
		int root = calRoot();
		tree.put(1, root);
		visited.add(root);
		nodeList.offer(2);
		nodeList.offer(3);
		while (!nodeList.isEmpty()) {
			int idx = nodeList.poll();
			int father = tree.get(idx / 2);
			int curr = 0;
			for (int i = 0; i < 16; i++) {
				if (((father >> i) & 1) == 1) {	
					int head = (i << 1);
					for (int tail : new int[]{0, 1}) {
						if (((rules >> (head + tail)) & 1) == (idx & 1)) {
							curr |= (1 << ((head + tail) % 16));
						}
					}
				}
			}
			if ((curr & MASK) == 0) {
				return true;
			}
			if (visited.contains(curr)) {
				continue;
			}
			visited.add(curr);
			tree.put(idx, curr);
			nodeList.add(2 * idx);
			nodeList.add(2 * idx + 1);
		}
		return false;
	}
	
	public static void printSurjectiveRules() {
		
		long begin = System.currentTimeMillis();
		System.out.println("left(" + leftBoundary[0] + leftBoundary[1] + 
				")right(" + rightBoundary[0] + rightBoundary[1] + ")");
		count = 0;
		rulesCharArr = new char[32];
		Arrays.fill(rulesCharArr, '0');
		dfs(0, 0);
		System.out.println("总计" + count + "条规则");
		long end = System.currentTimeMillis();
		System.out.println("执行用时：" + String.valueOf(end - begin) + "ms.");
		System.out.println("===============");
	}
	
// private:
	private static void dfs(int oneCount, int pos) {
		
		if (oneCount == 16) {
			String r = new String(rulesCharArr);
			if (!hasEden(r)) {
				count++;
				System.out.print(r);
				if (ls.contains(r)) {
					System.out.print("(linear)");
				}
				System.out.println();
			}
			return;
		}
		if (16 - oneCount > 32 - pos) {
			return;
		}
		rulesCharArr[pos] = '1';
		dfs(oneCount + 1, pos + 1);
		rulesCharArr[pos] = '0';
		dfs(oneCount, pos + 1);
	}
	
	private static int calMask() {
		
		int factor = (rightBoundary[0] << 1) + rightBoundary[1];
		return 4369 * (1 << factor);	// 2^0 + 2^4 + 2^8 + 2^12 (0000, 0100, 1000, 1100)
	}
	
	private static int calRoot() {
		
		int factor = (leftBoundary[0] << 3) + (leftBoundary[1] << 2);
		return 15 * (1 << factor);	// 2^0 + 2^1 + 2^2 + 2^3 (0000, 0001, 0010, 0011)
	}
	
	private static int[] leftBoundary = {0, 0};
	
	private static int[] rightBoundary = {0, 0};

	private static char[] rulesCharArr;
	
	private static int count;
	
	private static LinearSet ls = new LinearSet(5);
	
// main:
	public static void main(String[] args) {
		
		setBoundary(new int[]{0, 0}, new int[]{0, 0});
		printSurjectiveRules();
		setBoundary(new int[]{0, 0}, new int[]{0, 1});
		printSurjectiveRules();
		setBoundary(new int[]{0, 0}, new int[]{1, 0});
		printSurjectiveRules();
		setBoundary(new int[]{0, 0}, new int[]{1, 1});
		printSurjectiveRules();
		setBoundary(new int[]{0, 1}, new int[]{0, 0});
		printSurjectiveRules();
		setBoundary(new int[]{0, 1}, new int[]{0, 1});
		printSurjectiveRules();
		setBoundary(new int[]{0, 1}, new int[]{1, 0});
		printSurjectiveRules();
		setBoundary(new int[]{0, 1}, new int[]{1, 1});
		printSurjectiveRules();
		setBoundary(new int[]{1, 0}, new int[]{0, 0});
		printSurjectiveRules();
		setBoundary(new int[]{1, 0}, new int[]{0, 1});
		printSurjectiveRules();
		setBoundary(new int[]{1, 0}, new int[]{1, 0});
		printSurjectiveRules();
		setBoundary(new int[]{1, 0}, new int[]{1, 1});
		printSurjectiveRules();
		setBoundary(new int[]{1, 1}, new int[]{0, 0});
		printSurjectiveRules();
		setBoundary(new int[]{1, 1}, new int[]{0, 1});
		printSurjectiveRules();
		setBoundary(new int[]{1, 1}, new int[]{1, 0});
		printSurjectiveRules();
		setBoundary(new int[]{1, 1}, new int[]{1, 1});
		printSurjectiveRules();
	}
}
