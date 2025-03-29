package ca.reflective;

import java.util.*;

import ca.catools.Tools;
import guru.nidi.graphviz.attribute.Color;
import guru.nidi.graphviz.attribute.Label;
import guru.nidi.graphviz.attribute.Shape;
import guru.nidi.graphviz.attribute.Style;
import guru.nidi.graphviz.model.MutableNode;

public class RTNode {
    private static final Map<Integer, Set<Integer>> palindromeSets = new HashMap<>();

    protected int m;

    protected Integer[] tuples;

    protected RTNode(int _m, Integer[] _tuples) {
        m = _m;
        Arrays.sort(_tuples);
        tuples = _tuples;
    }

    protected RTNode(int _m, Set<Integer> set) {
        Integer[] _tuples = set.toArray(new Integer[0]);
        m = _m;
        Arrays.sort(_tuples);
        tuples = _tuples;
    }

    public static Set<Integer> getPalindromeSet(int _m) {
        if (!palindromeSets.containsKey(_m)) {
            Set<Integer> set = new HashSet<>();
            int max = 1 << (_m >> 1), hf = _m >> 1;
            for (int i = 0; i < max; i++) {
                int num = i;
                for (int j = 0; j < hf; j++) {
                    if (((i >> j) & 1) == 1) {
                        num += (1 << (_m - j - 1));
                    }
                }
                set.add(num);
            }
            palindromeSets.put(_m, set);
        }
        return palindromeSets.get(_m);
    }

    public static RTNode getEmptyNode(int _m) {
        return new RTNode(_m, new HashSet<>());
    }

    public static RTNode getPalindromeNode(int _m) {
        return new RTNode(_m, getPalindromeSet(_m));
    }

    public boolean isEden() {
        int palindromeCount = 0;
        for (int t : tuples) {
            if (getPalindromeSet(m).contains(t)) {
                palindromeCount++;
            }
        }
        return palindromeCount != 1;
    }

    public RTNode[] getChildren(boolean[] rule) {
        if (rule.length != (1 << (m + 1))) {
            throw new IllegalArgumentException("规则长度错误。rule.length: " + rule.length
                    + ", m + 1: " + (m + 1));
        }
        RTNode[] children = new RTNode[2];
        Set<Integer>[] chTuples= new Set[2];
        for (int i = 0; i < 2; i++) {
            chTuples[i] = new HashSet<>();
        }
        for (int t : tuples) {
            int tt = t << 1;
            chTuples[rule[tt] ? 1 : 0].add(tt % (1 << m));
            chTuples[rule[tt + 1] ? 1 : 0].add((tt + 1) % (1 << m));
        }
        for (int i = 0; i < 2; i++) {
            children[i] = new RTNode(m, chTuples[i]);
        }
        return children;
    }

    public void writeNode(MutableNode mNode) {
        StringBuilder sb = new StringBuilder();
        for (int t : tuples) {
            if (getPalindromeSet(m).contains(t)) {
                sb.append("<b>&nbsp;").append(Tools.toNBitString(t, m)).append("</b>");
            } else {
                sb.append(Tools.toNBitString(t, m));
            }
            sb.append("<br/>");
        }
        mNode.add(Shape.RECTANGLE, Style.ROUNDED, Label.html(sb.toString()));
        if (isEden()) {
            mNode.add(Style.combine(Style.FILLED, Style.ROUNDED), Color.RED);
        }
    }

    public void print() {
        for (int t : tuples) {
            System.out.println(Tools.toNBitString(t, m));
        }
    }

    @Override
    public int hashCode() {
        int h = 0, MOD = 1000000007;
        for (int t : tuples) {
            h = (h + t) % MOD;
        }
        return h;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof RTNode that)) {
            return false;
        }
        if (m != that.m || tuples.length != that.tuples.length) {
            return false;
        }
        int len = tuples.length;
        for (int i = 0; i < len; i++) {
            if (!Objects.equals(tuples[i], that.tuples[i])) {
                return false;
            }
        }
        return true;
    }

}
