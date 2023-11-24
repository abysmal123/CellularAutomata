package ca.injectivity2;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public final class GlobalInjectivityD5 {
	
// public:
	public static boolean injectivity(final String r) {
		
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
//		if (Integer.bitCount(rules) != 16) {
//			return false;
//		}
		Map<Integer, ValueSet> tree = new HashMap<Integer, ValueSet>();
		Set<String> visited = new HashSet<String>();
		Queue<Integer> nodeList = new ArrayDeque<Integer>();
		int[] rootSubValues = new int[16];
		rootSubValues[0] = 1;
		for (int i = 1; i < 16; i++) {
			rootSubValues[i] = rootSubValues[i - 1] * 2;
		}
		ValueSet root = new ValueSet(rootSubValues);
		tree.put(1, root);
		visited.add(hash(root));
		nodeList.offer(2);
		nodeList.offer(3);
		while (!nodeList.isEmpty()) {
			int idx = nodeList.poll();
			ValueSet father = tree.get(idx / 2);
			int[] subValues = new int[16];
			int tupleCount = 0;
			int periodicCount = 0;
			for (int k = 0; k < 16; k++) {
				for (int i = 0; i < 16; i++) {
					if (((father.subValues[k] >> i) & 1) == 1) {	
						int pos = (i << 1);
						for (int j : new int[]{0, 1}) {
							if (((rules >> (pos + j)) & 1) == (idx & 1)) {
								tupleCount++;
								subValues[k] |= (1 << ((pos + j) % 16));
								if ((pos + j) % 16 == k) {
									periodicCount++;
									if (periodicCount > 1) {
										return false;
									}
								}
							}
						}
					}
				}
				for (int i = 0; i < 8; i++) {
					if (((subValues[k] >> i) & 1) == 1 && ((subValues[k] >> (i + 8)) & 1) == 1) {
						for (int j : new int[]{0, 1}) {
							if (((rules >> ((i << 1) + j)) & 1) == ((rules >> (((i + 8) << 1) + j)) & 1)) {
								return false;
							}
						}	
					}
				}
			}
			if (tupleCount == 0) {
				return false;
			}
			ValueSet curr = new ValueSet(subValues);
			if (visited.contains(hash(curr))) {
				continue;
			}
			visited.add(hash(curr));
			tree.put(idx, curr);
			nodeList.add(2 * idx);
			nodeList.add(2 * idx + 1);
		}
		return true;
	}
	
	public static void printInjectiveRules() {
		
		long begin = System.currentTimeMillis();
		count = 0;
		rulesCharArr = new char[32];
		Arrays.fill(rulesCharArr, '0');
		rulesCharArr[0] = '1';
		dfs(1, 1);
		System.out.println("总计" + count + "条规则");
		long end = System.currentTimeMillis();
		System.out.println("执行用时：" + String.valueOf(end - begin) + "ms.");
	}
	
// private:
	private static void dfs(int oneCount, int pos) {
		
		if (oneCount == 16) {
			String r = new String(rulesCharArr);
			if (injectivity(r)) {
				count++;
				System.out.println(new String(rulesCharArr) + " " + count);
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

	private static String hash(ValueSet vs) {
		
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < vs.subValues.length; i++) {
			buffer.append(vs.subValues[i] + ".");
		}
		return buffer.toString();
	}
	
	private static char[] rulesCharArr;
	
	private static int count;
	
// main:
	public static void main(String[] args) {
		
//		String r = "11110000111100001111000011110000";
//		System.out.println(injectivity(r));
		printInjectiveRules();
	}
	
}
