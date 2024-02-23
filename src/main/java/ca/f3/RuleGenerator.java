package ca.f3;

import ca.catools.Tools;

import java.util.*;

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

	public static Map<String, String> allLinearRuleEntries() {
		Map<String, String> ret = new HashMap<>();
		for (int a = 0; a < 3; a++) {
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					for (int k = 0; k < 3; k++) {
						StringBuilder value = new StringBuilder();
						for (int b = 0; b < 27; b++) {
							value.append((b / 9 * i + b / 3 * j + (b % 3) * k + a) % 3);
						}
						StringBuilder key = new StringBuilder(4).append(i).append(j).append(k).append(a);
						ret.put(key.toString(), value.reverse().toString());
					}
				}
			}
		}
		return ret;
	}

	public static String[] allD5LinearRules() {
		Set<String> rs = new HashSet<>();
		for (int a = 0; a < 3; a++) {
			for (int h = 0; h < 3; h++) {
				for (int i = 0; i < 3; i++) {
					for (int j = 0; j < 3; j++) {
						for (int k = 0; k < 3; k++) {
							for (int l = 0; l < 3; l++) {
								StringBuilder sb = new StringBuilder();
								for (int b = 0; b < 243; b++) {
									sb.append((b / 81 * h + b / 27 * i +  b / 9 * j + b / 3 * k + (b % 3) * l + a) % 3);
								}
								rs.add(sb.reverse().toString());
							}
						}
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

	public static Map<String, String> allD5LinearRuleEntries() {
		Map<String, String> ret = new HashMap<>();
		for (int a = 0; a < 3; a++) {
			for (int h = 0; h < 3; h++) {
				for (int i = 0; i < 3; i++) {
					for (int j = 0; j < 3; j++) {
						for (int k = 0; k < 3; k++) {
							for (int l = 0; l < 3; l++) {
								StringBuilder value = new StringBuilder();
								for (int b = 0; b < 243; b++) {
									value.append((b / 81 * h + b / 27 * i +  b / 9 * j + b / 3 * k + (b % 3) * l + a) % 3);
								}
								StringBuilder key = new StringBuilder(6).append(h).append(i).append(j).append(k).append(l).append(a);
								ret.put(key.toString(), value.reverse().toString());
							}
						}
					}
				}
			}
		}
		return ret;
	}

	public static String[] allD7LinearRules(boolean allowsConstant) {
		Set<String> rs = new HashSet<>();
		for (int a = 0; a < 3; a++) {
			if (allowsConstant && a > 0) {
				break;
			}
			for (int g = 0; g < 3; g++) {
				for (int h = 0; h < 3; h++) {
					for (int i = 0; i < 3; i++) {
						for (int j = 0; j < 3; j++) {
							for (int k = 0; k < 3; k++) {
								for (int l = 0; l < 3; l++) {
									for (int m = 0; m < 3; m++) {
										StringBuilder sb = new StringBuilder();
										for (int b = 0; b < 2187; b++) {
											sb.append((b / 729 * g + b / 243 * h + b / 81 * i + b / 27 * j +  b / 9 * k + b / 3 * l + (b % 3) * m + a) % 3);
										}
										rs.add(sb.reverse().toString());
									}
								}
							}
						}
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

	public static Map<String, String> allD7LinearRuleEntries(boolean allowsConstant) {
		Map<String, String> ret = new HashMap<>();
		for (int a = 0; a < 3; a++) {
			if (!allowsConstant && a > 0) {
				break;
			}
			for (int g = 0; g < 3; g++) {
				for (int h = 0; h < 3; h++) {
					for (int i = 0; i < 3; i++) {
						for (int j = 0; j < 3; j++) {
							for (int k = 0; k < 3; k++) {
								for (int l = 0; l < 3; l++) {
									for (int m = 0; m < 3; m++) {
										StringBuilder value = new StringBuilder();
										for (int b = 0; b < 2187; b++) {
											value.append((b / 729 * g + b / 243 * h + b / 81 * i + b / 27 * j +  b / 9 * k + b / 3 * l + (b % 3) * m + a) % 3);
										}
										StringBuilder key = new StringBuilder(6).append(g).append(h).append(i).append(j).append(k).append(l).append(m);
										if (allowsConstant) {
											key.append(a);
										}
										ret.put(key.toString(), value.reverse().toString());
									}
								}
							}
						}
					}
				}
			}
		}
		return ret;
	}
}
