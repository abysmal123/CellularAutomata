package ca.injectivity;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
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
		// Step1:
		List<Integer> toZero = new ArrayList<Integer>();
		List<Integer> toOne = new ArrayList<Integer>();
		for (int i = 0; i < 32; i++) {
			if (((rules >> i) & 1) == 1) {
				toOne.add(i);
			} else {
				toZero.add(i);
			}
		}
		// Step2&3:
		Set<BoxD5> in = new HashSet<BoxD5>();
		Set<BoxD5> out = new HashSet<BoxD5>();
		Queue<BoxD5> crossOutList = new ArrayDeque<BoxD5>();
		for (int i = 0; i < 15; i++) {
			for (int j = i + 1; j < 16; j++) {
				BoxD5 box = new BoxD5(toZero.get(i), toZero.get(j), rules);
				if (box.sequentSet.contains(box)) {
					return false;
				}
				if (box.sequentSet.isEmpty()) {
					out.add(box);
					crossOutList.offer(box);
				} else {
					in.add(box);
				}
				box = new BoxD5(toOne.get(i), toOne.get(j), rules);
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
			BoxD5 curr = crossOutList.poll();
			for (Iterator<BoxD5> it = in.iterator(); it.hasNext();) {
				BoxD5 b = it.next();
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
		Map<BoxD5, Integer> weights = new HashMap<BoxD5, Integer>();
		for (int i = 0; i < 32; i++) {
			weights.put(new BoxD5(i, i), 0);
		}
		Set<BoxD5> assignedBox = new HashSet<BoxD5>();
		int t = in.size();
		while (t > 0) {
			for (Iterator<BoxD5> it1 = in.iterator(); it1.hasNext();) {
				BoxD5 b = it1.next();
				int maxWeight = -1;
				boolean flag = true;
				for (Iterator<BoxD5> it2 = b.sequentSet.iterator(); it2.hasNext();) {
					BoxD5 s = it2.next();
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
		for (Iterator<BoxD5> it = assignedBox.iterator(); it.hasNext();) {
			BoxD5 b = it.next();
			if ((b.n1 >> 1) == (b.n2 >> 1)) {
				return false;
			}
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
				System.out.println(r + " " + count);
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
	
	private static char[] rulesCharArr;
	
	private static int count;
	
// main:
	public static void main(String[] args) {
		
		String r = "11110000111100001111000011110000";
		System.out.println(injectivity(r));
	}
	
}
