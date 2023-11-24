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
		// Step1:
		List<Integer> toZero = new ArrayList<Integer>();
		List<Integer> toOne = new ArrayList<Integer>();
		for (int i = 0; i < 16; i++) {
			if (((rules >> i) & 1) == 1) {
				toOne.add(i);
			} else {
				toZero.add(i);
			}
		}
		// Step2&3:
		Set<BoxD4> in = new HashSet<BoxD4>();
		Set<BoxD4> out = new HashSet<BoxD4>();
		Queue<BoxD4> crossOutList = new ArrayDeque<BoxD4>();
		for (int i = 0; i < 7; i++) {
			for (int j = i + 1; j < 8; j++) {
				BoxD4 box = new BoxD4(toZero.get(i), toZero.get(j), rules);
				if (box.sequentSet.contains(box)) {
					return false;
				}
				if (box.sequentSet.isEmpty()) {
					out.add(box);
					crossOutList.offer(box);
				} else {
					in.add(box);
				}
				box = new BoxD4(toOne.get(i), toOne.get(j), rules);
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
			BoxD4 curr = crossOutList.poll();
			for (Iterator<BoxD4> it = in.iterator(); it.hasNext();) {
				BoxD4 b = it.next();
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
		Map<BoxD4, Integer> weights = new HashMap<BoxD4, Integer>();
		for (int i = 0; i < 16; i++) {
			weights.put(new BoxD4(i, i), 0);
		}
		Set<BoxD4> assignedBox = new HashSet<BoxD4>();
		int t = in.size();
		while (t > 0) {
			for (Iterator<BoxD4> it1 = in.iterator(); it1.hasNext();) {
				BoxD4 b = it1.next();
				int maxWeight = -1;
				boolean flag = true;
				for (Iterator<BoxD4> it2 = b.sequentSet.iterator(); it2.hasNext();) {
					BoxD4 s = it2.next();
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
		for (Iterator<BoxD4> it = assignedBox.iterator(); it.hasNext();) {
			BoxD4 b = it.next();
			if ((b.n1 >> 1) == (b.n2 >> 1)) {
				return false;
			}
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

}
