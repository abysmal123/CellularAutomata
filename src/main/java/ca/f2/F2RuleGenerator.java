package ca.f2;

import ca.catools.Tools;

import java.util.*;

public final class F2RuleGenerator {
	
	public static String randomRule(Random rn, int diameter) {
		int len = 1 << diameter, half = len >> 1;
		int[] arr = new int[len];
		for (int i = 0; i < half; i++) {
			arr[i] = 1;
		}
		StringBuilder sb = new StringBuilder();
		for (int i = len - 1; i >= 0; i--) {
			int j = rn.nextInt(i + 1);
			int temp = arr[j];
			arr[j] = arr[i];
			arr[i] = temp;
			sb.append(temp);
		}
		return sb.toString();
	}

	public static String linearRule(int[] coefficients, boolean reverse) {
		int diameter = coefficients.length;
		int mask = 0;
		for (int coefficient : coefficients) {
			if (coefficient > 1) {
				throw new IllegalArgumentException("所有参数取值范围均为{0, 1}。 "
						+ "All arguments must be valued in {0, 1}. ");
			}
			mask <<= 1;
			mask += coefficient;
		}
		int len = 1 << diameter, a = reverse ? 1 : 0;
		StringBuilder sb = new StringBuilder(len);
		for (int i = len - 1; i >= 0; i--) {
			sb.append((Integer.bitCount(mask & i) - 1) & 1);
		}
		return sb.toString();
	}
	
	public static List<String> allLinearRules(int diameter) {
		int max = 1 << diameter;
		List<String> ret = new ArrayList<>();
		for (int mask = 0; mask < max; mask++) {
			StringBuilder sb = new StringBuilder(max);
			for (int i = max - 1; i >= 0; i--) {
				sb.append(Integer.bitCount(mask & i) & 1);
			}
			ret.add(sb.toString());
		}
		return ret;
	}

	public static List<String> allLinearRulesAllowReverse(int diameter) {
		int max = 1 << diameter;
		List<String> ret = new ArrayList<>();
		for (int mask = 0; mask < max; mask++) {
			StringBuilder sb = new StringBuilder(max);
			StringBuilder r_sb = new StringBuilder(max);
			for (int i = max - 1; i >= 0; i--) {
				int bit = Integer.bitCount(mask & i) & 1;
				sb.append(bit);
				r_sb.append(1 - bit);
			}
			ret.add(sb.toString());
			ret.add(r_sb.toString());
		}
		return ret;
	}

	public static Map<String, String> allLinearRuleEntries(int diameter) {
		int max = 1 << diameter;
		Map<String, String> ret = new HashMap<>();
		for (int mask = 0; mask < max; mask++) {
			StringBuilder sb = new StringBuilder(max);
			for (int i = max - 1; i >= 0; i--) {
				sb.append(Integer.bitCount(mask & i) & 1);
			}
			ret.put(Tools.toNBitString(mask, diameter), sb.toString());
		}
		return ret;
	}
}
