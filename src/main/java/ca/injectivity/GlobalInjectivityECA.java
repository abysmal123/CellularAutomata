package ca.injectivity;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public final class GlobalInjectivityECA {
	
// public:
	public static boolean injectivity(final String r) {
		
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
		if (Integer.bitCount(rules) != 4) {
			return false;
		}
		// Step1:
		List<Integer> toZero = new ArrayList<Integer>();
		List<Integer> toOne = new ArrayList<Integer>();
		for (int i = 0; i < 8; i++) {
			if (((rules >> i) & 1) == 1) {
				toOne.add(i);
			} else {
				toZero.add(i);
			}
		}
		// Step2&3:
		Set<BoxECA> in = new HashSet<BoxECA>();
		Set<BoxECA> out = new HashSet<BoxECA>();
		Queue<BoxECA> crossOutList = new ArrayDeque<BoxECA>();
		for (int i = 0; i < 3; i++) {
			for (int j = i + 1; j < 4; j++) {
				BoxECA box = new BoxECA(toZero.get(i), toZero.get(j), rules);
				if (box.sequentSet.contains(box)) {
					return false;
				}
				if (box.sequentSet.isEmpty()) {
					out.add(box);
					crossOutList.offer(box);
				} else {
					in.add(box);
				}
				box = new BoxECA(toOne.get(i), toOne.get(j), rules);
				if (box.sequentSet.contains(box)) {
					return false;
				}
				if (box.sequentSet.isEmpty()) {
					out.add(box);
					crossOutList.offer(box);
				} else {
					in.add(box);
				}
			}
		}
		// Step4:
		while (!crossOutList.isEmpty()) {
			BoxECA curr = crossOutList.poll();
			for (Iterator<BoxECA> it = in.iterator(); it.hasNext();) {
				BoxECA b = it.next();
				if (b.sequentSet.contains(curr)) {
					b.sequentSet.remove(curr);
					if (b.sequentSet.isEmpty()) {
						it.remove();
						out.add(b);
						crossOutList.offer(b);
					}
				}
			}
		}
		// Step5:
		Map<BoxECA, Integer> weights = new HashMap<BoxECA, Integer>();
		for (int i = 0; i < 8; i++) {
			weights.put(new BoxECA(i, i), 0);
		}
		Set<BoxECA> assignedBox = new HashSet<BoxECA>();
		int t = in.size();
		while (t > 0) {
			for (Iterator<BoxECA> it1 = in.iterator(); it1.hasNext();) {
				BoxECA b = it1.next();
				int maxWeight = -1;
				boolean flag = true;
				for (Iterator<BoxECA> it2 = b.sequentSet.iterator(); it2.hasNext();) {
					BoxECA s = it2.next();
					if (weights.containsKey(s)) {
						maxWeight = Math.max(maxWeight, weights.get(s));
					} else {
						flag = false;
						break;
					}
				}
				if (flag) {
					weights.put(b, 1 + maxWeight);
					it1.remove();
					assignedBox.add(b);
				}
			}
			t--;
		}
		if (!in.isEmpty()) {
			return false;
		}
		// Step6:
		for (Iterator<BoxECA> it = assignedBox.iterator(); it.hasNext();) {
			BoxECA b = it.next();
			if ((b.n1 >> 1) == (b.n2 >> 1)) {
				return false;
			}
		}
 		return true;
	}
	
	public static void printInjectiveRules() {
		
		long begin = System.currentTimeMillis();
		int count = 0;
		for (int i = 255; i > 128; i--) {
			String r = toEightBitString(i);
			if (injectivity(r)) {
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

}
