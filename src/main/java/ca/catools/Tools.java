package ca.catools;

public final class Tools {

	// ���ع�������ͱ�ʾ
	public static long getRule(String r, int len) {
		
		if (r.length() != len) {
			throw new IllegalArgumentException("���򳤶ȱ���Ϊ" + len + "�� "
					+ "Length of input rule must be " + len + ". Input rule: " + r);
		}
		long rule = 0;
		for (int i = 0; i < len; i++) {
			rule <<= 1;
			if (r.charAt(i) == '1') {
				rule |= 1;
			} else if (r.charAt(i) != '0') {
				throw new IllegalArgumentException("�������Ϊ01����"
						+ "Input rule must be binary. Input rule: " + r);
			}
		}
		return rule;
	}

	// ���ع���Ĳ��������ʾ
	public static boolean[] getRule(String r) {
		int d = 0, len = r.length();
		while (len > 1) {
			if ((len & 1) == 1) {
				throw new IllegalArgumentException("���򳤶Ȳ�Ϊ2�������ݡ�");
			}
			d++;
			len >>= 1;
		}
		if (d < 3) {
			throw new IllegalArgumentException("ֱ������Ϊ3��");
		}
		boolean[] RULE = new boolean[r.length()];
		for (int i = r.length() - 1, j = 0; i >= 0; i--, j++) {
			char bit = r.charAt(j);
			if (bit != '0' && bit != '1') {
				throw new IllegalArgumentException("�������Ϊ01����"
						+ "Input rule must be binary. Input rule: " + r);
			}
			RULE[i] = bit == '1';
		}
		return RULE;
	}

	// ��intת��Ϊ�����Ʊ�ʾ��String
	public static String toNBitString(int num, int n) {
		
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < n; i++) {
			sb.append(num & 1);
			num >>= 1;
		}
		return sb.reverse().toString();
	}

	// ��longת��Ϊ�����Ʊ�ʾ��String
	public static String toNBitString(long num, int n) {

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < n; i++) {
			sb.append(num & 1);
			num >>= 1;
		}
		return sb.reverse().toString();
	}

	// �������Ʊ�ʾ��Stringת��Ϊint
	public static int toInteger(String binary) {

		int dec = 0, n = binary.length();
		for (int i = 0; i < n; i++) {
			dec <<= 1;
			if (binary.charAt(i) == '1') {
				dec |= 1;
			} else if (binary.charAt(i) != '0') {
				throw new IllegalArgumentException("����Ϊ01����"
						+ "Must be binary. Input: " + binary);
			}
		}
		return dec;
	}
}
