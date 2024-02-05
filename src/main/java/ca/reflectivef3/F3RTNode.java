package ca.reflectivef3;

import ca.catools.Tools;
import guru.nidi.graphviz.attribute.Color;
import guru.nidi.graphviz.attribute.Label;
import guru.nidi.graphviz.attribute.Shape;
import guru.nidi.graphviz.attribute.Style;
import guru.nidi.graphviz.model.MutableNode;

import java.util.*;

public class F3RTNode {
    private static final Map<Integer, Set<Integer>> palindromeSets = new HashMap<>();

    protected int m;

    protected Integer[] tuples;

    protected F3RTNode(int _m, Integer[] _tuples) {
        m = _m;
        Arrays.sort(_tuples);
        tuples = _tuples;
    }

    protected F3RTNode(int _m, Set<Integer> set) {
        Integer[] _tuples = set.toArray(new Integer[0]);
        m = _m;
        Arrays.sort(_tuples);
        tuples = _tuples;
    }

    public static Set<Integer> getPalindromeSet(int _m) {
        if (!palindromeSets.containsKey(_m)) {
            Set<Integer> set = new HashSet<>();
            int max = powOfThree(_m >> 1), hf = _m >> 1;
            for (int i = 0; i < max; i++) {
                int num = i;
                for (int j = 0; j < hf; j++) {
                    int digit = (i / powOfThree(j)) % 3;
                    num += digit * powOfThree(_m - j - 1);
                }
                set.add(num);
            }
            palindromeSets.put(_m, set);
        }
        return palindromeSets.get(_m);
    }

    public static F3RTNode getPalindromeNode(int _m) {
        return new F3RTNode(_m, getPalindromeSet(_m));
    }

    public boolean isEden() {
        boolean ret = true;
        for (int t : tuples) {
            if (getPalindromeSet(m).contains(t)) {
                ret = false;
                break;
            }
        }
        return ret;
    }

    public F3RTNode[] getChildren(int[] rule) {
        if (rule.length != (powOfThree(m + 1))) {
            throw new IllegalArgumentException("规则长度错误。rule.length: " + rule.length
                    + ", m + 1: " + (m + 1));
        }
        F3RTNode[] children = new F3RTNode[3];
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
            children[i] = new F3RTNode(m, chTuples[i]);
        }
        return children;
    }

    public void writeNode(MutableNode mNode) {
        boolean hasPalidrome = false;
        StringBuilder sb = new StringBuilder();
        for (int t : tuples) {
            if (getPalindromeSet(m).contains(t)) {
                hasPalidrome = true;
                sb.append("<b>&nbsp;").append(Tools.toNBitTernaryString(t, m)).append("</b>");
            } else {
                sb.append(Tools.toNBitTernaryString(t, m));
            }
            sb.append("<br/>");
        }
        mNode.add(Shape.RECTANGLE, Style.ROUNDED, Label.html(sb.toString()));
        if (!hasPalidrome) {
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
        if (!(o instanceof F3RTNode that)) {
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
