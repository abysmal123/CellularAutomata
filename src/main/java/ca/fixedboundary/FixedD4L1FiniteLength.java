package ca.fixedboundary;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class FixedD4L1FiniteLength {
	
// public:
	public static void setBoundary(int left, int[] right) {
		
		if (right.length != 2) {
			throw new IllegalArgumentException("�ұ߽糤�ȱ���Ϊ2��"
					+ "Length of right boundary must be 2. Input length: " + right.length);
		}
		if(left != 0 && left != 1) {
			throw new IllegalArgumentException("�߽�״̬����Ϊ0��1 ��"
					+ "Boundary states must be 0 or 1.");
		}
		for (int e : right) {
			if(e != 0 && e != 1) {
				throw new IllegalArgumentException("�߽�״̬����Ϊ0��1 ��"
						+ "Boundary states must be 0 or 1.");
			}
		}
		leftBoundary = left;
		System.arraycopy(right, 0, rightBoundary, 0, 2);
	}
	
	public static boolean hasEden(String r) {
		
		if (r.length() != 16) {
			throw new IllegalArgumentException("���򳤶ȱ���Ϊ16 ��"
					+ "Length of rules must be 16. Input rules: " + r);
		}
		if (r.charAt(0) != '0' && r.charAt(0) != '1') {
			throw new IllegalArgumentException("�������Ϊ01����"
					+ "Input rules must be binary. Input rules: " + r);
		}
		int rules = (r.charAt(0) == '1' ? 1 : 0);
		for (int i = 1; i < 16; i++) {
			rules <<= 1;
			if (r.charAt(i) == '1') {
				rules++;
			} else if (r.charAt(i) != '0') {
				throw new IllegalArgumentException("�������Ϊ01����"
						+ "Input rules must be binary. Input rules: " + r);
			}
		}
//		if ((rules & 1) == 1) {
//			return true;
//		}
		Map<Integer, Integer> tree = new HashMap<Integer, Integer>();
		Set<Integer> visited = new HashSet<Integer>();
		Queue<Integer> nodeList = new ArrayDeque<Integer>();
		int MASK = calMask();		// (000, 100)
		int root = (leftBoundary == 1 ? 240 : 15);
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
		System.out.println("left(" + leftBoundary + 
				")right(" + rightBoundary[0] + rightBoundary[1] + ")");
		count = 0;
		rulesCharArr = new char[16];
		Arrays.fill(rulesCharArr, '0');
		dfs(0, 0);
		System.out.println("�ܼ�" + count + "������");
		long end = System.currentTimeMillis();
		System.out.println("ִ����ʱ��" + String.valueOf(end - begin) + "ms.");
		System.out.println("===============");
	}
	
// private:
	private static int calMask() {
		
		int factor = (rightBoundary[0] << 1) + rightBoundary[1];
		return 17 * (1 << factor);	// 2^0 + 2^4 (000, 100)
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
	
	private static int leftBoundary = 0;
	
	private static int[] rightBoundary = {0, 0};

	private static char[] rulesCharArr;
	
	private static int count;
	
	private static LinearSet ls = new LinearSet(4);
	
// main:
	public static void main(String[] args) {
		
//		setBoundary(0, new int[]{0, 0});
//		printSurjectiveRules();
		setBoundary(0, new int[]{0, 1});
		printSurjectiveRules();
//		setBoundary(0, new int[]{1, 0});
//		printSurjectiveRules();
//		setBoundary(0, new int[]{1, 1});
//		printSurjectiveRules();
//		setBoundary(1, new int[]{0, 0});
//		printSurjectiveRules();
//		setBoundary(1, new int[]{0, 1});
//		printSurjectiveRules();
//		setBoundary(1, new int[]{1, 0});
//		printSurjectiveRules();
//		setBoundary(1, new int[]{1, 1});
//		printSurjectiveRules();
	}
}
