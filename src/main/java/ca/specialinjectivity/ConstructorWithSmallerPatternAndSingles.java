package ca.specialinjectivity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import ca.injectivity2.GlobalInjectivityDn;
import ca.catools.Tools;

// ʹ��ֱ��С��6��pattern�����ɸ�ֱ��6�ġ�������ת������ֱ��6�ĵ������
public final class ConstructorWithSmallerPatternAndSingles {

    // pattern�����һ������λ
    public static String leftWrap(String pattern) {
        return "c" + pattern;
    }

    // pattern�����һ������λ
    public static String rightWrap(String pattern) {
        return pattern + "c";
    }

    // ����������pattern�б仯��λ��ʮ����
    public static List<Integer> changedBitsIdx(String pattern) {
        List<Integer> ret = new ArrayList<>();
        StringBuffer buffer = new StringBuffer();
        dfs(pattern, buffer, 0, ret);
        return ret;
    }

    // ��changedBits()���ã������������б仯��λ
    private static void dfs(String pattern, StringBuffer buffer, int pos, List<Integer> ret) {
        if (pos == pattern.length()) {
            ret.add(Tools.toInteger(buffer.toString()));
            return;
        }
        char cur = pattern.charAt(pos);
        if (cur == 'c' || cur == 'x') {
            buffer.append('0');
            dfs(pattern, buffer, pos + 1, ret);
            buffer.setCharAt(buffer.length() - 1, '1');
        } else {
            buffer.append(cur);
        }
        dfs(pattern, buffer, pos + 1, ret);
        buffer.deleteCharAt(buffer.length() - 1);
    }

    // ʹ��һ��ֱ��5��pattern������ֱ��6�ġ�������ת������
    public static List<String[]> injectiveRulesWithPattern(String pattern) {

        int d = 6;
        int n = pattern.length(), xPos = pattern.indexOf('x');
        int len = (1 << n);
        Set<Integer> changedBitsIdx = new HashSet<>(changedBitsIdx(pattern));
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < len; i++) {
            int bit = (i >> (n - 1 - xPos)) & 1;
            if (changedBitsIdx.contains(i)) {
                bit = 1 - bit;
            }
            buffer.append(bit);
        }
        List<String[]> injectiveRules = new ArrayList<>();
        for (int i = 0; i < len; i++) {
            if (changedBitsIdx.contains(i) || buffer.charAt(i) == '1') {
                continue;
            }
            for (int j = 0; j < len; j++) {
                if (changedBitsIdx.contains(j) || buffer.charAt(j) == '0') {
                    continue;
                }
                buffer.setCharAt(i, '1');
                buffer.setCharAt(j, '0');
                String r = new StringBuffer(buffer).reverse().toString();
                buffer.setCharAt(i, '0');
                buffer.setCharAt(j, '1');
                GlobalInjectivityDn.setD(d);
                if (GlobalInjectivityDn.injectivity(r)) {
                    String[] ruleInfo = new String[4];
                    ruleInfo[0] = r;
                    ruleInfo[1] = pattern;
                    ruleInfo[2] = Tools.toNBitString(i, d);
                    ruleInfo[3] = Tools.toNBitString(j, d);
                    injectiveRules.add(ruleInfo);
                }
            }
        }
        return injectiveRules;
    }

}
