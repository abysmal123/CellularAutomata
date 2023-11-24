package ca.finiteconfig;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public final class FiniteConfigECA {
	
// public:
	public static boolean hasEden(String r) {
		
		if (r.length() != 8) {
			throw new IllegalArgumentException("规则长度必须为8 。"
					+ "Length of rules must be 8. Input rules: " + r);
		}
		if (r.charAt(0) != '0' && r.charAt(0) != '1') {
			throw new IllegalArgumentException("规则必须为01串。"
					+ "Input rules must be binary. Input rules: " + r);
		}
		int rules = (r.charAt(0) == '1' ? 1 : 0);
		for (int i = 1; i < 8; i++) {
			rules <<= 1;
			if (r.charAt(i) == '1') {
				rules++;
			} else if (r.charAt(i) != '0') {
				throw new IllegalArgumentException("规则必须为01串。"
						+ "Input rules must be binary. Input rules: " + r);
			}
		}
		if ((rules & 1) == 1) {
			return true;
		}
		int exportSet = getExportSet(rules);
		int importSet = getImportSet(rules);
		Map<Integer, Integer> tree = new HashMap<Integer, Integer>();
		Set<Integer> visited = new HashSet<Integer>();
		Queue<Integer> nodeList = new ArrayDeque<Integer>();
		Integer root = exportSet;
		tree.put(1, root);
		visited.add(root);
		nodeList.offer(2);
		nodeList.offer(3);
		while (!nodeList.isEmpty()) {
			int idx = nodeList.poll();
			int father = tree.get(idx / 2);
			int curr = 0;
			for (int i = 0; i < 4; i++) {
				if (((father >> i) & 1) == 1) {	
					int head = (i << 1);
					for (int tail : new int[]{0, 1}) {
						if (((rules >> (head + tail)) & 1) == (idx & 1)) {
							curr |= (1 << ((head + tail) % 4));
						}
					}
				}
			}
			if (curr == 0 || (curr & importSet) == 0) {
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
		int count = 0;
		for (int i = 0; i < 256; i++) {
			String r = toEightBitString(i);
			if (!hasEden(r)) {
				System.out.println(r);
				count++;
			}
		}
		System.out.println("总计" + count + "条规则");
		long end = System.currentTimeMillis();
		System.out.println("执行用时：" + String.valueOf(end - begin) + "ms.");
	}
	
	public static String toEightBitString(int num) {
		
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < 8; i++) {
			buffer.insert(0, num & 1);
			num >>= 1;
		}
		return buffer.toString();
	}
	
// private:
	private static int getExportSet(int rules) {
		
		int exportSet = 0;
		Queue<Integer> sequence = new ArrayDeque<Integer>(); 
		sequence.offer(0);
		while (!sequence.isEmpty()) {
			int head = (sequence.poll() << 1);
			for (int tail : new int[]{0, 1}) {
				if (((rules >> (head + tail)) & 1) == 0 && (exportSet >> ((head + tail) % 4) & 1) == 0) {
					exportSet |= (1 << ((head + tail) % 4));
					sequence.offer((head + tail) % 4);
				}
			}
		}
		return exportSet;
	}
	
	private static int getImportSet(int rules) {
		
		int importSet = 0;
		Queue<Integer> sequence = new ArrayDeque<Integer>(); 
		sequence.offer(0);
		while (!sequence.isEmpty()) {
			int tail = sequence.poll();
			for (int head : new int[]{0, 1 << 2}) {
				if (((rules >> (head + tail)) & 1) == 0 && ((importSet >> ((head + tail) >> 1)) & 1) == 0) {
					importSet |= (1 << ((head + tail) >> 1));
					sequence.offer((head + tail) >> 1);
				}
			}
		}
		return importSet;
	}
	
}