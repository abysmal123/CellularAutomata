package ca.catools;

public final class Tools {

	// 返回规则的整型表示
	public static long getRule(String r, int len) {
		
		if (r.length() != len) {
			throw new IllegalArgumentException("规则长度必须为" + len + "。 "
					+ "Length of input rule must be " + len + ". Input rule: " + r);
		}
		long rule = 0;
		for (int i = 0; i < len; i++) {
			rule <<= 1;
			if (r.charAt(i) == '1') {
				rule |= 1;
			} else if (r.charAt(i) != '0') {
				throw new IllegalArgumentException("规则必须为01串。"
						+ "Input rule must be binary. Input rule: " + r);
			}
		}
		return rule;
	}

	public static boolean[] getRule(String r) {
		int d = 0, len = r.length();
		while (len > 1) {
			if ((len & 1) == 1) {
				throw new IllegalArgumentException("规则长度不为2的整数幂。");
			}
			d++;
			len >>= 1;
		}
		if (d < 3) {
			throw new IllegalArgumentException("直径至少为3。");
		}
		boolean[] RULE = new boolean[r.length()];
		for (int i = r.length() - 1, j = 0; i >= 0; i--, j++) {
			RULE[i] = r.charAt(j) == '1';
		}
		return RULE;
	}

	// 将int转换为二进制表示的String
	public static String toNBitString(int num, int n) {
		
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < n; i++) {
			buffer.append(num & 1);
			num >>= 1;
		}
		return buffer.reverse().toString();
	}

	// 将long转换为二进制表示的String
	public static String toNBitString(long num, int n) {

		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < n; i++) {
			buffer.append(num & 1);
			num >>= 1;
		}
		return buffer.reverse().toString();
	}

	// 将二进制表示的String转换为int
	public static int toInteger(String binary) {

		int dec = 0, n = binary.length();
		for (int i = 0; i < n; i++) {
			dec <<= 1;
			if (binary.charAt(i) == '1') {
				dec |= 1;
			} else if (binary.charAt(i) != '0') {
				throw new IllegalArgumentException("必须为01串。"
						+ "Must be binary. Input: " + binary);
			}
		}
		return dec;
	}
}
