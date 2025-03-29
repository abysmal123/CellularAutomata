package ca.periodicf3;

import ca.catools.Tools;
import guru.nidi.graphviz.attribute.Color;
import guru.nidi.graphviz.attribute.Label;
import guru.nidi.graphviz.attribute.Shape;
import guru.nidi.graphviz.attribute.Style;
import guru.nidi.graphviz.model.MutableNode;

import java.util.*;

public class F3PTNode {
    private static final Map<Integer, Set<Integer>> SelfSets = new HashMap<>();

    protected int m;

    protected Integer[] pairs;

    protected int mPowerOfThree;

    protected F3PTNode(int _m, Integer[] _pairs) {
        m = _m;
        Arrays.sort(_pairs);
        pairs = _pairs;
        mPowerOfThree = powOfThree(_m);
    }

    protected F3PTNode(int _m, Set<Integer> set) {
        Integer[] _pairs = set.toArray(new Integer[0]);
        m = _m;
        Arrays.sort(_pairs);
        pairs = _pairs;
        mPowerOfThree = powOfThree(_m);
    }

    public static Set<Integer> getSelfSet(int _m) {
        if (!SelfSets.containsKey(_m)) {
            Set<Integer> set = new HashSet<>();
            int max = powOfThree(_m);
            for (int i = 0; i < max; i++) {
                set.add(i * max + i);
            }
            SelfSets.put(_m, set);
        }
        return SelfSets.get(_m);
    }

    public static F3PTNode getSelfNode(int _m) {
        return new F3PTNode(_m, getSelfSet(_m));
    }

    public boolean isEden() {
        int palindromeCnt = 0;
        for (int p : pairs) {
            if (getSelfSet(m).contains(p)) {
                palindromeCnt++;
            }
        }
        return palindromeCnt != 1;
    }

    public F3PTNode[] getChildren(int[] rule) {
        if (rule.length != (powOfThree(m + 1))) {
            throw new IllegalArgumentException("规则长度错误。rule.length: " + rule.length
                    + ", m + 1: " + (m + 1));
        }
        F3PTNode[] children = new F3PTNode[3];
        Set<Integer>[] chTuples= new Set[3];
        for (int i = 0; i < 3; i++) {
            chTuples[i] = new HashSet<>();
        }
        for (int p : pairs) {
            int lTuple = p / mPowerOfThree, rTuple = p % mPowerOfThree;
            int tt = rTuple * 3;
            for (int i = 0; i < 3; i++) {
                chTuples[rule[tt + i]].add((tt + i) % mPowerOfThree + lTuple * mPowerOfThree);
            }
        }
        for (int i = 0; i < 3; i++) {
            children[i] = new F3PTNode(m, chTuples[i]);
        }
        return children;
    }

    public void writeNode(MutableNode mNode) {
        StringBuilder sb = new StringBuilder();
        for (int p : pairs) {
            int lTuple = p / mPowerOfThree, rTuple = p % mPowerOfThree;
            String pStr = "&lt;" + Tools.toNBitTernaryString(lTuple, m) + ","
                    + Tools.toNBitTernaryString(rTuple, m) + "&gt;";
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
        if (!(o instanceof F3PTNode that)) {
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
