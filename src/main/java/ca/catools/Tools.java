package ca.catools;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.NoSuchElementException;

public final class Tools {

	// 返回规则的整型表示
	public static long getRuleAsLong(String r, int len) {
		
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

	// 返回规则的整型数组表示
	public static int[] getRuleAsIntArray(String r, int len) {
		if (r.length() != len) {
			throw new IllegalArgumentException("规则长度必须为" + len + "。 "
					+ "Length of input rule must be " + len + ". Input rule: " + r);
		}
		int[] RULE = new int[r.length()];
		for (int i = r.length() - 1, j = 0; i >= 0; i--, j++) {
			int bit = r.charAt(j) - '0';
			if (bit != 0 && bit != 1) {
				throw new IllegalArgumentException("规则必须为01串。"
						+ "Input rule must be binary. Input rule: " + r);
			}
			RULE[i] = bit;
		}
		return RULE;
	}

	// 返回规则的布尔数组表示
	public static boolean[] getRuleAsBooleanArray(String r, int len) {
		if (r.length() != len) {
			throw new IllegalArgumentException("规则长度必须为" + len + "。 "
					+ "Length of input rule must be " + len + ". Input rule: " + r);
		}
		boolean[] RULE = new boolean[r.length()];
		for (int i = r.length() - 1, j = 0; i >= 0; i--, j++) {
			char bit = r.charAt(j);
			if (bit != '0' && bit != '1') {
				throw new IllegalArgumentException("规则必须为01串。"
						+ "Input rule must be binary. Input rule: " + r);
			}
			RULE[i] = bit == '1';
		}
		return RULE;
	}

	// 返回规则的布尔数组表示
	public static boolean[] getRuleAsBooleanArray(String r) {
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
			char bit = r.charAt(j);
			if (bit != '0' && bit != '1') {
				throw new IllegalArgumentException("规则必须为01串。"
						+ "Input rule must be binary. Input rule: " + r);
			}
			RULE[i] = bit == '1';
		}
		return RULE;
	}

	// 将int转换为二进制表示的String
	public static String toNBitString(int num, int n) {
		
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < n; i++) {
			sb.append(num & 1);
			num >>= 1;
		}
		return sb.reverse().toString();
	}

	// 将long转换为二进制表示的String
	public static String toNBitString(long num, int n) {

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < n; i++) {
			sb.append(num & 1);
			num >>= 1;
		}
		return sb.reverse().toString();
	}

	// 将int转换为三进制表示的String
	public static String toNBitTernaryString(int num, int n) {

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < n; i++) {
			sb.append(num % 3);
			num /= 3;
		}
		return sb.reverse().toString();
	}

	// 将long转换为三进制表示的String
	public static String toNBitTernaryString(long num, int n) {

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < n; i++) {
			sb.append(num % 3);
			num /= 3;
		}
		return sb.reverse().toString();
	}

	// 将二进制表示的String转换为int
	public static int toInteger(String binary) {

		int dec = 0, n = binary.length();
		for (int i = 0; i < n; i++) {
			dec <<= 1;
			if (binary.charAt(i) == '1') {
				dec++;
			} else if (binary.charAt(i) != '0') {
				throw new IllegalArgumentException("必须为01串。"
						+ "Must be binary. Input: " + binary);
			}
		}
		return dec;
	}

	// 将二进制表示的String转换为long
	public static long toLong(String binary) {

		long dec = 0;
		int n = binary.length();
		for (int i = 0; i < n; i++) {
			dec <<= 1;
			if (binary.charAt(i) == '1') {
				dec++;
			} else if (binary.charAt(i) != '0') {
				throw new IllegalArgumentException("必须为01串。"
						+ "Must be binary. Input: " + binary);
			}
		}
		return dec;
	}

	// 检查规则长度是否为2的整数次幂，返回规则直径
	public static int checkLength(String r) {
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
		return d;
	}

	// 返回控制台日志记录器
	public static Logger getConsoleLogger() {
		return LoggerFactory.getLogger("consoleLogger");
	}

	// 返回输出.log文件日志记录器
	public static Logger getFileLogger() {
		return LoggerFactory.getLogger("fileLogger");
	}

	// 返回一个用于生成平衡二进制数的迭代器
	public static Iterator<Long> getBalancedRuleIterator(int m) {
		return new BalancedRuleIterator(m);
	}

	private static class BalancedRuleIterator implements Iterator<Long> {
        private long cur;
        private final long limit;

		public BalancedRuleIterator(int m) {
            int n = 1 << m;
			cur = (1L << (n >> 1)) - 1;
			limit = 1L << n;
		}

		@Override
		public boolean hasNext() {
			return cur < limit;
		}

		@Override
		public Long next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			long temp = cur;
			long lb = cur & -cur;
			long h = cur + lb;
			cur = (((h ^ cur) >> 2) / lb) | h;
			return temp;
		}
	}
}
