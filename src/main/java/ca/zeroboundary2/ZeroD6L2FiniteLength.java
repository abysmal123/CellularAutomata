package ca.zeroboundary2;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class ZeroD6L2FiniteLength {
	
// public:
	public static boolean hasEden(String r) {
		
		if (r.length() != 64) {
			throw new IllegalArgumentException("规则长度必须为64。"
					+ "Length of rules must be 32. Input rules: " + r);
		}
		if (r.charAt(0) != '0' && r.charAt(0) != '1') {
			throw new IllegalArgumentException("规则必须为01串。"
					+ "Input rules must be binary. Input rules: " + r);
		}
		long rules = (r.charAt(0) == '1' ? 1 : 0);
		for (int i = 1; i < 64; i++) {
			rules <<= 1;
			if (r.charAt(i) == '1') {
				rules++;
			} else if (r.charAt(i) != '0') {
				throw new IllegalArgumentException("规则必须为01串。"
						+ "Input rules must be binary. Input rules: " + r);
			}
		}
//			if ((rules & 1) == 1) {
//				return true;
//			}
		Map<Long, Long> tree = new HashMap<Long, Long>();
		Set<Long> visited = new HashSet<Long>();
		Queue<Long> nodeList = new ArrayDeque<Long>();
		long MASK = 16843009;		// 2^0 + 2^8 + 2^16 + 2^24 (00000, 01000, 10000, 11000)
		long root = 255;				// (00000, 00001, 00010, 00011, 00100, 00101, 00110, 00111)
		tree.put((long)1, root);
		visited.add(root);
		nodeList.offer((long)2);
		nodeList.offer((long)3);
		while (!nodeList.isEmpty()) {
			long idx = nodeList.poll();
			long father = tree.get(idx / 2);
			long curr = 0;
			for (int i = 0; i < 32; i++) {
				if (((father >> i) & 1) == 1) {	
					long head = (i << 1);
					for (long tail : new long[]{0, 1}) {
						if (((rules >> (head + tail)) & 1) == (idx & 1)) {
							curr |= (1 << ((head + tail) % 32));
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
		count = 0;
		rulesCharArr = new char[64];
		Arrays.fill(rulesCharArr, '0');
		dfs(0, 0);
		System.out.println("总计" + count + "条规则");
		long end = System.currentTimeMillis();
		System.out.println("执行用时：" + String.valueOf(end - begin) + "ms.");
	}
	
// private:
	private static void dfs(int oneCount, int pos) {
		
		if (oneCount == 32) {
			String r = new String(rulesCharArr);
			if (!hasEden(r)) {
				count++;
				System.out.println(r + " " + count);
			}
			return;
		}
		if (32 - oneCount > 64 - pos) {
			return;
		}
		rulesCharArr[pos] = '1';
		dfs(oneCount + 1, pos + 1);
		rulesCharArr[pos] = '0';
		dfs(oneCount, pos + 1);
	}

	private static char[] rulesCharArr;
	
	private static int count;
	
// main:
	public static void main(String[] args) {
		
		String r = "1011111101000000111111110000000011111111000000001111111100000000";
		System.out.println(!hasEden(r));
	}
	
}
