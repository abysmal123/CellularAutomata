package ca.specialinjectivity;

import java.util.ArrayList;
import java.util.List;

public final class SpecialInjectivity {

// public:

	// 判断pattern是否为单射规则
	public static boolean isSpecial(final String pattern) {
			
		int n = pattern.length(), varPos = -1;
		char[] arr = pattern.toCharArray();
		for (int i = 0; i < n; i++) {
			if (arr[i] == 'x') {
				if (varPos != -1) {
					throw new IllegalArgumentException("The pattern contains more than one 'x'.");
				}
				varPos = i;
			}
		}
		if (varPos == -1) {
			throw new IllegalArgumentException("The pattern doesn't contain an 'x'.");
		}
		int pace = Math.max(varPos, n - 1 - varPos);
		for (int factor : new int[]{-1, 1}) {
			for (int i = 1; i <= pace; i++) {
				boolean matched = true;
				for (int j = 0; j < n; j++) {
					int k = j + factor * i;
					if (k < 0 || arr[k] == arr[j] || arr[k] == 'x' || arr[j] == 'x') {
						continue;
					}
					matched = false;
					break;
				}
				if (matched) {
					return false;
				}
			}
		}
		return true;
	}

	// 构造一个pattern，x（从左到右的）位置为xPos，且除去x后的数字部分为n位二进制数，十进制表示为num
	public static String toPattern(int num, int n, int xPos) {
		
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < n; i++) {
			buffer.insert(0, num & 1);
			num >>= 1;
		}
		buffer.insert(xPos, 'x');
		return buffer.toString();
	}

	// 把pattern转换为对应的wolfram数
	public static String patternToWolfram(String pattern) {
		
		int n = pattern.length();
		int rule0 = 0, rule1 = 0, xPos = 0;
		for (int i = 0; i < n; i++) {
			rule0 <<= 1;
			rule1 <<= 1;
			if (pattern.charAt(i) == '1') {
				rule0++;
				rule1++;
			} else if (pattern.charAt(i) == 'x') {
				rule1++;
				xPos = i;
			}
		}
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < (1 << n); i++) {
			int bit;
			if (i == rule0) {
				bit = 1;
			} else if (i == rule1) {
				bit = 0;
			} else {
				bit = (i >> (n - 1 - xPos)) & 1;
			}
			buffer.append(bit);
		}
		return buffer.reverse().toString();
	}

	// 返回一个list，其包含所有直径n+1的单射模式, 每条以(wolfram, pattern)二元组记录
	public static List<String[]> toList(int n) {
		
		List<String[]> ruleList = new ArrayList<>();
		for (int xPos = 0; xPos < n; xPos++) {
			for (int i = 0; i < (1 << n); i++) {
				String pattern = toPattern(i, n, xPos);
				if (isSpecial(pattern)) {
					String wolfram = patternToWolfram(pattern);
					String[] pair = {wolfram, pattern};
					ruleList.add(pair);
				}
			}
		}
		return ruleList;
	}

	// 把所有直径n+1的单射模式打印到控制台
	public static void printAll(int n) {
		
		int count = 0;
		for (int xPos = 0; xPos < n; xPos++) {
			for (int i = 0; i < (1 << n); i++) {
				String pattern = toPattern(i, n, xPos);
				if (isSpecial(pattern)) {
					count++;
					String wolfram = patternToWolfram(pattern);
					System.out.println(wolfram + ": " + pattern);
				}
			}
		}
		System.out.println("count: " + count);
	}

// main:
	public static void main(String[] args) {
		
		printAll(4);
		
	}
	
}
