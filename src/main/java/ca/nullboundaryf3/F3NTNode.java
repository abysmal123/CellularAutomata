package ca.nullboundaryf3;

import ca.catools.Tools;
import guru.nidi.graphviz.attribute.Color;
import guru.nidi.graphviz.attribute.Label;
import guru.nidi.graphviz.attribute.Shape;
import guru.nidi.graphviz.attribute.Style;
import guru.nidi.graphviz.model.MutableNode;

import java.util.*;

public class F3NTNode {
    private static final Map<Integer, Set<Integer>> ZeroTailSets = new HashMap<>();

    protected int m;

    protected Integer[] tuples;

    protected F3NTNode(int _m, Integer[] _tuples) {
        m = _m;
        Arrays.sort(_tuples);
        tuples = _tuples;
    }

    protected F3NTNode(int _m, Set<Integer> set) {
        Integer[] _tuples = set.toArray(new Integer[0]);
        m = _m;
        Arrays.sort(_tuples);
        tuples = _tuples;
    }

    public static Set<Integer> getZeroTailSet(int _m, int r_radius) {
        if (_m < r_radius) {
            throw new IllegalArgumentException("直径、半径参数错误。m: " + _m
                    + ", r_radius: " + r_radius);
        }
        int idx = (_m << 4) + r_radius;
        if (!ZeroTailSets.containsKey(idx)) {
            Set<Integer> set = new HashSet<>();
            int max = powOfThree(_m - r_radius);
            for (int i = 0; i < max; i++) {
                set.add(i * powOfThree(r_radius));
            }
            ZeroTailSets.put(idx, set);
        }
        return ZeroTailSets.get(idx);
    }

    public static F3NTNode getRootNode(int _m, int l_radius) {
        if (_m < l_radius) {
            throw new IllegalArgumentException("直径、半径参数错误。m: " + _m
                    + ", l_radius: " + l_radius);
        }
        int max = powOfThree(_m - l_radius);
        Integer[] _tuples = new Integer[max];
        for (int i = 0; i < max; i++) {
            _tuples[i] = i;
        }
        return new F3NTNode(_m, _tuples);
    }

    public boolean isEden(int r_radius) {
        int palindromeCnt = 0;
        for (int t : tuples) {
            if (getZeroTailSet(m, r_radius).contains(t)) {
                palindromeCnt++;
            }
        }
        return palindromeCnt != 1;
    }

    public F3NTNode[] getChildren(int[] rule) {
        if (rule.length != (powOfThree(m + 1))) {
            throw new IllegalArgumentException("规则长度错误。rule.length: " + rule.length
                    + ", m + 1: " + (m + 1));
        }
        F3NTNode[] children = new F3NTNode[3];
        Set<Integer>[] chTuples= new Set[3];
        for (int i = 0; i < 3; i++) {
            chTuples[i] = new HashSet<>();
        }
        for (int t : tuples) {
            int tt = t * 3;
            for (int i = 0; i < 3; i++) {
                chTuples[rule[tt + i]].add((tt + i) % powOfThree(m));
            }
        }
        for (int i = 0; i < 3; i++) {
            children[i] = new F3NTNode(m, chTuples[i]);
        }
        return children;
    }

    public void writeNode(MutableNode mNode, int r_radius) {
        StringBuilder sb = new StringBuilder();
        for (int t : tuples) {
            if (getZeroTailSet(m, r_radius).contains(t)) {
                sb.append("<b>&nbsp;").append(Tools.toNBitTernaryString(t, m)).append("</b>");
            } else {
                sb.append(Tools.toNBitTernaryString(t, m));
            }
            sb.append("<br/>");
        }
        mNode.add(Shape.RECTANGLE, Style.ROUNDED, Label.html(sb.toString()));
        if (isEden(r_radius)) {
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
        if (!(o instanceof F3NTNode that)) {
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

    protected static int powOfThree(int k) {
        if (powCache[k] == -1) {
            int res = 1;
            for (int i = 0; i < k; i++) {
                res *= 3;
            }
            powCache[k] = res;
        }
        return powCache[k];
    }

    protected final static int[] powCache;
    static {
        powCache = new int[10];
        Arrays.fill(powCache, -1);
    }

}
