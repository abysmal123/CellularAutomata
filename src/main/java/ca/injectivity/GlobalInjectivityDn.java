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
import ca.longbinary.LongBinary;

public final class GlobalInjectivityDn {		// d >= 6
	
// public:
	public static void setD(int _d) {
		d = _d;
	}
	
	public static boolean injectivity(final String r) {
		
		int wolframLen = (1 << d);
		if (r.length() != wolframLen) {
			throw new IllegalArgumentException("规则长度错误 。");
		}
		if (r.charAt(0) != '0' && r.charAt(0) != '1') {
			throw new IllegalArgumentException("规则必须为01串。"
					+ "Input rules must be binary. Input rules: " + r);
		}
		LongBinary rules = new LongBinary(wolframLen);
		for (int i = 0; i < wolframLen; i++) {
			if (r.charAt(i) == '1') {
				rules.setPos(i, 1);
			} else if (r.charAt(i) != '0') {
				throw new IllegalArgumentException("规则必须为01串。"
						+ "Input rules must be binary. Input rules: " + r);
			}
		}
		// Step1:
		List<Integer> toZero = new ArrayList<Integer>();
		List<Integer> toOne = new ArrayList<Integer>();
		for (int i = 0; i < wolframLen; i++) {
			if (rules.getPos(i) == 1) {
				toOne.add(i);
			} else {
				toZero.add(i);
			}
		}
		// Step2&3:
		Set<BoxDn> in = new HashSet<BoxDn>();
		Queue<BoxDn> crossOutList = new ArrayDeque<BoxDn>();
		int numToZero = toZero.size();
		int numToOne = toOne.size();
		for (int i = 0; i < numToZero - 1; i++) {
			for (int j = i + 1; j < numToZero; j++) {
				BoxDn box = new BoxDn(d, toZero.get(i), toZero.get(j), rules);
				if (box.sequentSet.contains(box)) {
					return false;
				}
				if (box.sequentSet.isEmpty()) {
					crossOutList.offer(box);
				} else {
					in.add(box);
				}
			}
		}
		for (int i = 0; i < numToOne - 1; i++) {
			for (int j = i + 1; j < numToOne; j++) {
				BoxDn box = new BoxDn(d, toOne.get(i), toOne.get(j), rules);
				if (box.sequentSet.contains(box)) {
					return false;
				}
				if (box.sequentSet.isEmpty()) {
					crossOutList.offer(box);
				} else {
					in.add(box);
				}
			}
		}
		// Step4:
		while (!crossOutList.isEmpty()) {
			BoxDn curr = crossOutList.poll();
			for (Iterator<BoxDn> it = in.iterator(); it.hasNext();) {
				BoxDn b = it.next();
				if (b.sequentSet.contains(curr)) {
					b.sequentSet.remove(curr);
					if (b.sequentSet.isEmpty()) {
						it.remove();
						crossOutList.offer(b);
					}
				}
			}
		}
		// Step5:
		Map<BoxDn, Integer> weights = new HashMap<BoxDn, Integer>();
		for (int i = 0; i < wolframLen; i++) {
			weights.put(new BoxDn(d, i, i), 0);
		}
		Set<BoxDn> assignedBox = new HashSet<BoxDn>();
		int lastSize = in.size() + 1;
		while (in.size() < lastSize) {
			lastSize = in.size();
			for (Iterator<BoxDn> it1 = in.iterator(); it1.hasNext();) {
				BoxDn b = it1.next();
				int maxWeight = -1;
				boolean flag = true;
				for (Iterator<BoxDn> it2 = b.sequentSet.iterator(); it2.hasNext();) {
					BoxDn s = it2.next();
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
		}
		if (!in.isEmpty()) {
			return false;
		}
		// Step6:
		for (Iterator<BoxDn> it = assignedBox.iterator(); it.hasNext();) {
			BoxDn b = it.next();
			if ((b.n1 >> 1) == (b.n2 >> 1)) {
				return false;
			}
		}
 		return true;
	}
	
	public static void runBatch(String[] cases) {
		
		long begin = System.currentTimeMillis();
		count = 0;
		for (String r : cases) {
			if (injectivity(r)) {
				count++;
				System.out.println(r);
			}
		}
		System.out.println("总计" + cases.length + "条规则，其中" + count + "条单射规则，" + (cases.length - count) + "条非单射规则");
		long end = System.currentTimeMillis();
		System.out.println("执行用时：" + String.valueOf(end - begin) + "ms.");
	}
	
	public static void printInjectiveRules() {
		
		long begin = System.currentTimeMillis();
		count = 0;
		rulesCharArr = new char[1 << d];
		Arrays.fill(rulesCharArr, '0');
		rulesCharArr[0] = '1';
		dfs(1, 1);
		System.out.println("总计" + count + "条规则");
		long end = System.currentTimeMillis();
		System.out.println("执行用时：" + String.valueOf(end - begin) + "ms.");
	}
	
// private:
	private static void dfs(int oneCount, int pos) {
		
		if (oneCount == (1 << (d - 1))) {
			String r = new String(rulesCharArr);
			if (injectivity(r)) {
				count++;
				System.out.println(r + " " + count);
			}
			return;
		}
		if ((1 << (d - 1)) - oneCount > (1 << d) - pos) {
			return;
		}
		rulesCharArr[pos] = '1';
		dfs(oneCount + 1, pos + 1);
		rulesCharArr[pos] = '0';
		dfs(oneCount, pos + 1);
	}
	
	private static int d = 6;
	
	private static char[] rulesCharArr;
	
	private static int count;
	
// main:
//	public static void main(String[] args) {
//		
//		
//	}
	
}
