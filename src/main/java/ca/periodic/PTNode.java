package ca.periodic;

import ca.catools.Tools;
import guru.nidi.graphviz.attribute.Color;
import guru.nidi.graphviz.attribute.Label;
import guru.nidi.graphviz.attribute.Shape;
import guru.nidi.graphviz.attribute.Style;
import guru.nidi.graphviz.model.MutableNode;

import java.util.*;

public class PTNode {
    private static final Map<Integer, Set<Integer>> SelfSets = new HashMap<>();

    protected int m; // m = 直径-1

    protected Integer[] pairs;

    protected PTNode(int _m, Integer[] _pairs) {
        m = _m;
        Arrays.sort(_pairs);
        pairs = _pairs;
    }

    protected PTNode(int _m, Set<Integer> set) {
        Integer[] _pairs = set.toArray(new Integer[0]);
        m = _m;
        Arrays.sort(_pairs);
        pairs = _pairs;
    }

    public static Set<Integer> getSelfSet(int _m) {
        if (!SelfSets.containsKey(_m)) {
            Set<Integer> set = new HashSet<>();
            int max = 1 << _m;
            for (int i = 0; i < max; i++) {
                set.add((i << _m) + i);
            }
            SelfSets.put(_m, set);
        }
        return SelfSets.get(_m);
    }

    public static PTNode getEmptyNode(int _m) {
        return new PTNode(_m, new HashSet<>());
    }

    public static PTNode getSelfNode(int _m) {
        return new PTNode(_m, getSelfSet(_m));
    }

    public boolean isEden() {
        int selfCount = 0;
        for (int p : pairs) {
            if (getSelfSet(m).contains(p)) {
                selfCount++;
            }
        }
        return selfCount != 1;
    }

    public PTNode[] getChildren(boolean[] rule) {
        if (rule.length != (1 << (m + 1))) {
            throw new IllegalArgumentException("规则长度错误。rule.length: " + rule.length
                    + ", m + 1: " + (m + 1));
        }
        PTNode[] children = new PTNode[2];
        Set<Integer>[] chPairs= new Set[2];
        for (int i = 0; i < 2; i++) {
            chPairs[i] = new HashSet<>();
        }
        for (int p : pairs) {
            int lTuple = p >> m, rTuple = p % (1 << m);
            int tt = rTuple << 1;
            chPairs[rule[tt] ? 1 : 0].add(tt % (1 << m) + (lTuple << m));
            chPairs[rule[tt + 1] ? 1 : 0].add((tt + 1) % (1 << m) + (lTuple << m));
        }
        for (int i = 0; i < 2; i++) {
            children[i] = new PTNode(m, chPairs[i]);
        }
        return children;
    }

    public void writeNode(MutableNode mNode) {
        StringBuilder sb = new StringBuilder();
        for (int p : pairs) {
            int lTuple = p >> m, rTuple = p % (1 << m);
            String pStr = "&lt;" + Tools.toNBitString(lTuple, m) + ","
                    + Tools.toNBitString(rTuple, m) + "&gt;";
            if (getSelfSet(m).contains(p)) {
                sb.append("<b>&nbsp;").append(pStr).append("</b>");
            } else {
                sb.append(pStr);
            }
            sb.append("<br/>");
        }
        mNode.add(Shape.RECTANGLE, Style.ROUNDED, Label.html(sb.toString()));
        if (isEden()) {
            mNode.add(Style.combine(Style.FILLED, Style.ROUNDED), Color.RED);
        }
    }

    public void print() {
        for (int p : pairs) {
            System.out.println(Tools.toNBitString(p, m));
        }
    }

    @Override
    public int hashCode() {
        int h = 0, MOD = 1000000007;
        for (int p : pairs) {
            h = (h + p) % MOD;
        }
        return h;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof PTNode that)) {
            return false;
        }
        if (m != that.m || pairs.length != that.pairs.length) {
            return false;
        }
        int len = pairs.length;
        for (int i = 0; i < len; i++) {
            if (!Objects.equals(pairs[i], that.pairs[i])) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner(",","[","]");
        for (int p : pairs) {
            joiner.add(Tools.toNBitString(p, m * 2));
        }
        return joiner.toString();
    }

}
