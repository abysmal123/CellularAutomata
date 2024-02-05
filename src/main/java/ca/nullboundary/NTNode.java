package ca.nullboundary;

import ca.catools.Tools;
import guru.nidi.graphviz.attribute.Color;
import guru.nidi.graphviz.attribute.Label;
import guru.nidi.graphviz.attribute.Shape;
import guru.nidi.graphviz.attribute.Style;
import guru.nidi.graphviz.model.MutableNode;

import java.util.*;

public class NTNode {
    private static final Map<Integer, Set<Integer>> ZeroTailSets = new HashMap<>();

    protected int m;

    protected Integer[] tuples;

    protected NTNode(int _m, Integer[] _tuples) {
        m = _m;
        Arrays.sort(_tuples);
        tuples = _tuples;
    }

    protected NTNode(int _m, Set<Integer> set) {
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
            int max = 1 << (_m - r_radius);
            for (int i = 0; i < max; i++) {
                set.add(i << r_radius);
            }
            ZeroTailSets.put(idx, set);
        }
        return ZeroTailSets.get(idx);
    }

    public static NTNode getRootNode(int _m, int l_radius) {
        if (_m < l_radius) {
            throw new IllegalArgumentException("直径、半径参数错误。m: " + _m
                    + ", l_radius: " + l_radius);
        }
        int max = 1 << (_m - l_radius);
        Integer[] _tuples = new Integer[max];
        for (int i = 0; i < max; i++) {
            _tuples[i] = i;
        }
        return new NTNode(_m, _tuples);
    }

    public boolean isEden(int r_radius) {
        boolean ret = true;
        for (int t : tuples) {
            if (getZeroTailSet(m, r_radius).contains(t)) {
                ret = false;
                break;
            }
        }
        return ret;
    }

    public NTNode[] getChildren(boolean[] rule) {
        if (rule.length != (1 << (m + 1))) {
            throw new IllegalArgumentException("规则长度错误。rule.length: " + rule.length
                    + ", m + 1: " + (m + 1));
        }
        NTNode[] children = new NTNode[2];
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
            children[i] = new NTNode(m, chTuples[i]);
        }
        return children;
    }

    public void writeNode(MutableNode mNode, int r_radius) {
        boolean fitsNull = false;
        StringBuilder sb = new StringBuilder();
        for (int t : tuples) {
            if (getZeroTailSet(m, r_radius).contains(t)) {
                fitsNull = true;
                sb.append("<b>&nbsp;").append(Tools.toNBitString(t, m)).append("</b>");
            } else {
                sb.append(Tools.toNBitString(t, m));
            }
            sb.append("<br/>");
        }
        mNode.add(Shape.RECTANGLE, Style.ROUNDED, Label.html(sb.toString()));
        if (!fitsNull) {
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
        if (!(o instanceof NTNode that)) {
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
