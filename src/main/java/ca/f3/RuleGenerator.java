package ca.f3;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public final class RuleGenerator {
	
	public static String randomRule(Random rn) {
		int[] arr = {2,2,2,2,2,2,2,2,2,1,1,1,1,1,1,1,1,1,0,0,0,0,0,0,0,0,0};
		StringBuilder sb = new StringBuilder();
		for (int i = 26; i >= 0; i--) {
			int j = rn.nextInt(i + 1);
			int temp = arr[j];
			arr[j] = arr[i];
			arr[i] = temp;
			sb.append(temp);
		}
		return sb.toString();
	}

	public static String linearRule(int i, int j, int k, int a) {
		if (i < 0|| j < 0 || k < 0 || a < 0 || i > 2 || j > 2 || k > 2 || a > 2) {
			throw new IllegalArgumentException("所有参数取值范围均为{0, 1, 2}。 "
					+ "All arguments must be valued in {0, 1, 2}. ");
		}
		StringBuilder sb = new StringBuilder();
		for (int b = 0; b < 27; b++) {
			sb.append((b / 9 * i + b / 3 * j + (b % 3) * k + a) % 3);
		}
		return sb.reverse().toString();
	}
	
	public static String[] allLinearRules() {
		Set<String> rs = new HashSet<>();
		for (int a = 0; a < 3; a++) {
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					for (int k = 0; k < 3; k++) {
						StringBuilder sb = new StringBuilder();
						for (int b = 0; b < 27; b++) {
							sb.append((b / 9 * i + b / 3 * j + (b % 3) * k + a) % 3);
						}
						rs.add(sb.reverse().toString());
					}
				}
			}
		}
		String[] ret = new String[rs.size()];
		int i = 0;
		for (String r : rs) {
			ret[i++] = r;
		}
		return ret;
	}
}
