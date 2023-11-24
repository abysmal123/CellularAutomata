package ca.fixedboundary;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class FixedD4L2FiniteLength {
	
// public:
	public static void setBoundary(int[] left, int right) {
		
		if (left.length != 2) {
			throw new IllegalArgumentException("左边界长度必须为2。"
					+ "Length of left boundary must be 2. Input length: " + left.length);
		}
		for (int e : left) {
			if(e != 0 && e != 1) {
				throw new IllegalArgumentException("边界状态必须为0或1 。"
						+ "Boundary states must be 0 or 1.");
			}
		}
		if(right != 0 && right != 1) {
			throw new IllegalArgumentException("边界状态必须为0或1 。"
					+ "Boundary states must be 0 or 1.");
		}
		System.arraycopy(left, 0, leftBoundary, 0, 2);
		rightBoundary = right;
	}
	
	public static boolean hasEden(String r) {
		
		if (r.length() != 16) {
			throw new IllegalArgumentException("规则长度必须为16 。"
					+ "Length of rules must be 16. Input rules: " + r);
		}
		if (r.charAt(0) != '0' && r.charAt(0) != '1') {
			throw new IllegalArgumentException("规则必须为01串。"
					+ "Input rules must be binary. Input rules: " + r);
		}
		int rules = (r.charAt(0) == '1' ? 1 : 0);
		for (int i = 1; i < 16; i++) {
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
		int MASK = (rightBoundary == 1 ? 170 : 85);		// (000, 010, 100, 110)
		int root = calRoot();
		tree.put(1, root);
		visited.add(0);
		visited.add(root);
		nodeList.offer(2);
		nodeList.offer(3);
		while (!nodeList.isEmpty()) {
			int idx = nodeList.poll();
			int father = tree.get(idx / 2);
			int curr = 0;
			for (int i = 0; i < 8; i++) {
				if (((father >> i) & 1) == 1) {	
					int head = (i << 1);
					for (int tail : new int[]{0, 1}) {
						if (((rules >> (head + tail)) & 1) == (idx & 1)) {
							curr |= (1 << ((head + tail) % 8));
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
				")right(" + rightBoundary + ")");
		count = 0;
		rulesCharArr = new char[16];
		Arrays.fill(rulesCharArr, '0');
		dfs(0, 0);
		System.out.println("总计" + count + "条规则");
		long end = System.currentTimeMillis();
		System.out.println("执行用时：" + String.valueOf(end - begin) + "ms.");
		System.out.println("===============");
	}
	
// private:
	private static int calRoot() {
		
		int factor = (leftBoundary[0] << 2) + (leftBoundary[1] << 1);
		return 3 * (1 << factor);	// 2^0 + 2^1 (000, 001)
	}
	
	private static void dfs(int oneCount, int pos) {
		
		if (oneCount == 8) {
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
		if (8 - oneCount > 16 - pos) {
			return;
		}
		rulesCharArr[pos] = '1';
		dfs(oneCount + 1, pos + 1);
		rulesCharArr[pos] = '0';
		dfs(oneCount, pos + 1);
	}
	
	private static int[] leftBoundary = {0, 0};
	
	private static int rightBoundary = 0;

	private static char[] rulesCharArr;
	
	private static int count;
	
	private static LinearSet ls = new LinearSet(4);
	
// main:
	public static void main(String[] args) {
		
//		setBoundary(new int[]{0, 0}, 0);
//		printSurjectiveRules();
//		setBoundary(new int[]{0, 0}, 1);
//		printSurjectiveRules();
		setBoundary(new int[]{0, 1}, 0);
		printSurjectiveRules();
//		setBoundary(new int[]{0, 1}, 1);
//		printSurjectiveRules();
//		setBoundary(new int[]{1, 0}, 0);
//		printSurjectiveRules();
//		setBoundary(new int[]{1, 0}, 1);
//		printSurjectiveRules();
//		setBoundary(new int[]{1, 1}, 0);
//		printSurjectiveRules();
//		setBoundary(new int[]{1, 1}, 1);
//		printSurjectiveRules();
	}
}
