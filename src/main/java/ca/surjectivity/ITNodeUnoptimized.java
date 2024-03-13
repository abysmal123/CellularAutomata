package ca.surjectivity;

import ca.catools.Tools;
import guru.nidi.graphviz.attribute.Color;
import guru.nidi.graphviz.attribute.Label;
import guru.nidi.graphviz.attribute.Shape;
import guru.nidi.graphviz.attribute.Style;
import guru.nidi.graphviz.model.MutableNode;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class ITNodeUnoptimized {

    protected int m;

    protected Integer[] tuples;

    protected ITNodeUnoptimized(int _m, Integer[] _tuples) {
        m = _m;
        Arrays.sort(_tuples);
        tuples = _tuples;
    }

    protected ITNodeUnoptimized(int _m, Set<Integer> set) {
        Integer[] _tuples = set.toArray(new Integer[0]);
        m = _m;
        Arrays.sort(_tuples);
        tuples = _tuples;
    }

    public static ITNodeUnoptimized getRootNode(int _m, boolean[] rule) {
        int max = rule.length;
        if (max != (1 << _m)) {
            throw new IllegalArgumentException("规则长度错误。rule.length: " + max
                    + ", m: " + _m);
        }
        Set<Integer> _set = new HashSet<>();
        for (int i = 0; i < max; i++) {
            if (!rule[i]) {
                _set.add(i);
            }
        }
        return new ITNodeUnoptimized(_m, _set);
    }

    public boolean isEden() {
        return tuples.length == 0;
    }

    public ITNodeUnoptimized[] getChildren(boolean[] rule) {
        if (rule.length != (1 << m)) {
            throw new IllegalArgumentException("规则长度错误。rule.length: " + rule.length
                    + ", m: " + m);
        }
        ITNodeUnoptimized[] children = new ITNodeUnoptimized[2];
        Set<Integer>[] chTuples= new Set[2];
        for (int i = 0; i < 2; i++) {
            chTuples[i] = new HashSet<>();
        }
        for (int t : tuples) {
            int tt = (t << 1) % (1 << m);
            chTuples[rule[tt] ? 1 : 0].add(tt);
            chTuples[rule[tt + 1] ? 1 : 0].add(tt + 1);
        }
        for (int i = 0; i < 2; i++) {
            children[i] = new ITNodeUnoptimized(m, chTuples[i]);
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
        if (!(o instanceof ITNodeUnoptimized that)) {
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
