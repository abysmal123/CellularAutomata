package ca.injectivity2;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public final class GlobalInjectivityD4 {
	
// public:
	public static boolean injectivity(final String r) {
		
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
		if (Integer.bitCount(rules) != 8) {
			return false;
		}
		Map<Integer, ValueSet> tree = new HashMap<Integer, ValueSet>();
		Set<String> visited = new HashSet<String>();
		Queue<Integer> nodeList = new ArrayDeque<Integer>();
		ValueSet root = new ValueSet(new int[]{1, 2, 4, 8, 16, 32, 64, 128});
		tree.put(1, root);
		visited.add(hash(root));
		nodeList.offer(2);
		nodeList.offer(3);
		while (!nodeList.isEmpty()) {
			int idx = nodeList.poll();
			ValueSet father = tree.get(idx / 2);
			int[] subValues = new int[8];
			int tupleCount = 0;
			int periodicCount = 0;
			for (int k = 0; k < 8; k++) {
				for (int i = 0; i < 8; i++) {
					if (((father.subValues[k] >> i) & 1) == 1) {	
						int pos = (i << 1);
						for (int j : new int[]{0, 1}) {
							if (((rules >> (pos + j)) & 1) == (idx & 1)) {
								tupleCount++;
								subValues[k] |= (1 << ((pos + j) % 8));
								if ((pos + j) % 8 == k) {
									periodicCount++;
									if (periodicCount > 1) {
										return false;
									}
								}
							}
						}
					}
				}
				for (int i = 0; i < 4; i++) {
					if (((subValues[k] >> i) & 1) == 1 && ((subValues[k] >> (i + 4)) & 1) == 1) {
						for (int j : new int[]{0, 1}) {
							if (((rules >> ((i << 1) + j)) & 1) == ((rules >> (((i + 4) << 1) + j)) & 1)) {
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
		int count = 0;
		for (int i = 65535; i >= 32768; i--) {
			String r = toSixteenBitString(i);
			if (injectivity(r)) {
				System.out.println(r);
				count++;
			}
		}
		System.out.println("总计" + count + "条规则");
		long end = System.currentTimeMillis();
		System.out.println("执行用时：" + String.valueOf(end - begin) + "ms.");
	}
	
	public static String toSixteenBitString(int num) {
		
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < 16; i++) {
			buffer.insert(0, num & 1);
			num >>= 1;
		}
		return buffer.toString();
	}

// private:
	private static String hash(ValueSet vs) {
		
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < vs.subValues.length; i++) {
			buffer.append(vs.subValues[i] + ".");
		}
		return buffer.toString();
	}
	
}
