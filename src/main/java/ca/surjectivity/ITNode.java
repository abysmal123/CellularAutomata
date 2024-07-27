package ca.surjectivity;

import ca.catools.Tools;
import guru.nidi.graphviz.attribute.Color;
import guru.nidi.graphviz.attribute.Label;
import guru.nidi.graphviz.attribute.Shape;
import guru.nidi.graphviz.attribute.Style;
import guru.nidi.graphviz.model.MutableNode;

import java.util.*;

public class ITNode {

    protected int m;

    protected Integer[] tuples;

    protected ITNode(int _m, Integer[] _tuples) {
        m = _m;
        Arrays.sort(_tuples);
        tuples = _tuples;
    }

    protected ITNode(int _m, Set<Integer> set) {
        Integer[] _tuples = set.toArray(new Integer[0]);
        m = _m;
        Arrays.sort(_tuples);
        tuples = _tuples;
    }

    public static ITNode getRootNode(int _m, boolean[] rule) {
        int max = rule.length, half = max >> 1;
        if (max != (1 << (_m + 1))) {
            throw new IllegalArgumentException("规则长度错误。rule.length: " + max
                    + ", m + 1: " + (_m + 1));
        }
        Set<Integer> _set = new HashSet<>();
        for (int i = 0; i < half; i++) {
            if (!(rule[i] && rule[i | half])) {
                _set.add(i);
            }
        }
        return new ITNode(_m, _set);
    }

    public boolean isEden() {
        return tuples.length == 0;
    }

    public ITNode[] getChildren(boolean[] rule) {
        if (rule.length != (1 << (m + 1))) {
            throw new IllegalArgumentException("规则长度错误。rule.length: " + rule.length
                    + ", m + 1: " + (m + 1));
        }
        ITNode[] children = new ITNode[2];
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
            children[i] = new ITNode(m, chTuples[i]);
        }
        return children;
    }

    public void writeNode(MutableNode mNode) {
        StringBuilder sb = new StringBuilder();
        for (int t : tuples) {
            sb.append(Tools.toNBitString(t, m)).append("<br/>");
        }
        if (sb.isEmpty()) {
            sb.append(' ');
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
        if (!(o instanceof ITNode that)) {
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
