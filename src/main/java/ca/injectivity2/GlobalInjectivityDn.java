package ca.injectivity2;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import ca.longbinary.LongBinary;

public final class GlobalInjectivityDn {		// d >= 6
	
// public:
	public static void setD(int _d) {
		d = _d;
	}
	
	public static boolean injectivity(final String r) {
		
		int wolframLen = (1 << d), half = (1 << (d - 1));
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

		Queue<LongBinary[]> nodeList = new ArrayDeque<LongBinary[]>();
		Set<String> visited = new HashSet<String>();
		
		LongBinary constOne = LongBinary.bOR(new LongBinary(half), new LongBinary("1"));
		
		LongBinary[] root = new LongBinary[half];
		root[0] = constOne;
		for (int i = 1; i < half; i++) {
			root[i] = LongBinary.SHL(root[i - 1], 1);
		}
		nodeList.offer(root);
		while(!nodeList.isEmpty()) {
			LongBinary[] curr = nodeList.poll();
			if (visited.contains(hash(curr))) {
				continue;
			}
			visited.add(hash(curr));
			LongBinary[] zeroChild = new LongBinary[half];
			LongBinary[] oneChild = new LongBinary[half];
			for (int i = 0; i < half; i++) {
				zeroChild[i] = new LongBinary(half);
				oneChild[i] = new LongBinary(half);
			}
			int zeroTupleCount = 0, oneTupleCount = 0, 
					zeroPeriodicCount = 0, onePeriodicCount = 0;
			for (int k = 0; k < half; k++) {
				for (int i = 0; i < half; i++) {
					if (curr[k].getPos(i) == 1) {	
						int pos = (i << 1);
						for (int j : new int[]{0, 1}) {
							if (rules.getPos(pos + j) == 0) {
								zeroTupleCount++;
								zeroChild[k].setPos((pos + j) % half, 1);
								if ((pos + j) % half == k) {
									zeroPeriodicCount++;
									if (zeroPeriodicCount > 1) {
										return false;
									}
								}
							} else {
								oneTupleCount++;
								oneChild[k].setPos((pos + j) % half, 1);
								if ((pos + j) % half == k) {
									onePeriodicCount++;
									if (onePeriodicCount > 1) {
										return false;
									}
								}
							}
						}
					}
				}
				int quarter = (half >> 1);
				for (int i = 0; i < quarter; i++) {
					if ((zeroChild[k].getPos(i) == 1 && zeroChild[k].getPos(i + quarter) == 1) || 
						(oneChild[k].getPos(i) == 1 && oneChild[k].getPos(i + quarter) == 1)) {
						for (int j : new int[]{0, 1}) {
							if (rules.getPos((i << 1) + j) == rules.getPos(((i + quarter) << 1) + j)) {
								return false;
							}
						}
					}
				}
			}
			if (zeroTupleCount == 0 || oneTupleCount == 0) {
				return false;
			}
			nodeList.offer(zeroChild);
			nodeList.offer(oneChild);
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
				System.out.println(new String(rulesCharArr) + " " + count);
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

	private static String hash(LongBinary[] vs) {
		
		StringBuffer buffer = new StringBuffer();
		for (LongBinary v : vs) {
			buffer.append(v.hashString());
		}
		return buffer.toString();
	}
	
	private static int d = 6;
	
	private static char[] rulesCharArr;
	
	private static int count;
	
// main:
	public static void main(String[] args) {
		
		setD(7);
		String r = "11111111000000001111111100000000111111110000000011111111000000001111111100000000111111110000000011111111000000001111111100000000";
		System.out.println(injectivity(r));
	}
	
}
