package ca.specialinjectivity;

import java.util.ArrayList;
import java.util.List;

public final class SpecialInjectivity {

// public:
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
	
	public static String toPattern(int num, int n, int xPos) {
		
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < n; i++) {
			buffer.insert(0, num & 1);
			num >>= 1;
		}
		buffer.insert(xPos, 'x');
		return buffer.toString();
	}
	
	public static String toWolfram(String pattern, int xPos) {
		
		int n = pattern.length();
		int rule0, rule1;
		if (pattern.charAt(0) == '1') {
			rule0 = rule1 = 1;
		} else if (pattern.charAt(0) == '0') {
			rule0 = rule1 = 0;
		} else {
			rule0 = 0;
			rule1 = 1;
		}
		for (int i = 1; i < n; i++) {
			rule0 <<= 1;
			rule1 <<= 1;
			if (pattern.charAt(i) == '1') {
				rule0++;
				rule1++;
			} else if (pattern.charAt(i) == 'x') {
				rule1++;
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
			buffer.insert(0, bit);
		}
		return buffer.toString();
	}
	
	public static List<String[]> toList(int n) {
		
		List<String[]> ruleList = new ArrayList<>();
		for (int xPos = 0; xPos < n; xPos++) {
			for (int i = 0; i < (1 << n); i++) {
				String pattern = toPattern(i, n, xPos);
				if (isSpecial(pattern)) {
					String wolfram = toWolfram(pattern, xPos);
					String[] pair = {wolfram, pattern};
					ruleList.add(pair);
				}
			}
		}
		return ruleList;
	}

	public static void printAll(int n) {
		
		int count = 0;
		for (int xPos = 0; xPos < n; xPos++) {
			for (int i = 0; i < (1 << n); i++) {
				String pattern = toPattern(i, n, xPos);
				if (isSpecial(pattern)) {
					count++;
					String wolfram = toWolfram(pattern, xPos);
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
