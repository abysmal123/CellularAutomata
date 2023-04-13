import ca.injectivity2.GlobalInjectivityDn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class TestTwoAlgorithms {
	
	public static void main(String[] args) {
		
		int d = 16, num = 10;
		ca.injectivity3.GlobalInjectivityDn.setD(d);
		GlobalInjectivityDn.setD(d);
		String[] cases = getRandomBalancedCases(d, num);
//		String[] cases = getAllBalancedCases(d);
		
		System.out.println("新算法：");
		GlobalInjectivityDn.runBatch(cases);
//		
//		System.out.println();
//		
//		System.out.println("旧算法(使用查找表删除box)：");
//		injectivity3.GlobalInjectivityDn.runBatch(cases);
	}
	
	public static String[] getRandomBalancedCases(int d, int num) {
		
		String[] cases = new String[num];
		int wolframLen = (1 << d);
		Random random = new Random(System.currentTimeMillis() % 10007);
		for (int i = 0; i < num; i++) {
			StringBuffer buffer = new StringBuffer();
			int zeroCount = 0, oneCount = 0;
			for(int j = 0; j < wolframLen; j++) {
				int bit = random.nextInt(2);
				if (bit == 0) {
					buffer.append('0');
					zeroCount++;
					if (zeroCount == wolframLen / 2) {
						for (j++; j < wolframLen; j++) {
							buffer.append('1');
						}
						break;
					}
				} else {
					buffer.append('1');
					oneCount++;
					if (oneCount == wolframLen / 2) {
						for (j++; j < wolframLen; j++) {
							buffer.append('0');
						}
						break;
					}
				}
			}
			cases[i] = buffer.toString();
		}
		return cases;
	}
	
	public static String[] getRandomCases(int d, int num) {
		
		String[] cases = new String[num];
		int wolframLen = (1 << d);
		Random random = new Random(System.currentTimeMillis() % 10007);
		for (int i = 0; i < num; i++) {
			StringBuffer buffer = new StringBuffer();
			for(int j = 0; j < wolframLen; j++) {
				buffer.append(random.nextInt(2) == 1 ? '1' : '0');
			}
			cases[i] = buffer.toString();
		}
		return cases;
	}
	
	public static String[] getAllBalancedCases(int d) {
		
		int wolframLen = (1 << d);
		rulesCharArr = new char[wolframLen];
		Arrays.fill(rulesCharArr, '0');
		List<String> cases = new ArrayList<String>();
		dfs(0, 0, d, cases);
		int n = cases.size();
		String[] ret = new String[n];
		for (int i = 0; i < n; i++) {
			ret[i] = cases.get(i);
		}
		return ret;
	}
	
	private static void dfs(int oneCount, int pos, int d, List<String> cases) {
		
		if (oneCount == (1 << (d - 1))) {
			String r = new String(rulesCharArr);
			cases.add(r);
			return;
		}
		if ((1 << (d - 1)) - oneCount > (1 << d) - pos) {
			return;
		}
		rulesCharArr[pos] = '1';
		dfs(oneCount + 1, pos + 1, d, cases);
		rulesCharArr[pos] = '0';
		dfs(oneCount, pos + 1, d, cases);
	}
	
	private static char[] rulesCharArr;
} 
