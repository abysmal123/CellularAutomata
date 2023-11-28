package ca.linkgraphics;

import guru.nidi.graphviz.attribute.Color;
import guru.nidi.graphviz.attribute.Label;
import guru.nidi.graphviz.attribute.Shape;
import guru.nidi.graphviz.attribute.Style;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.model.MutableNode;

import java.io.File;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

import static guru.nidi.graphviz.model.Factory.mutGraph;
import static guru.nidi.graphviz.model.Factory.mutNode;

public final class LinkedListReflectiveD3F3 {

    public static void storeImage(final String r, final String fileName) throws IOException {

        initializeRule(r);
        Graphviz.fromGraph(toLinkedList()).render(Format.PNG).toFile(new File(PATH + fileName + ".png"));
    }

    public static void setPath(String path) {
        PATH = path;
    }

// private:
    private static String PATH = "graph/";                      // 图片存储路径
    private static String r;                                    // 当前规则
    private static Map<Integer, int[]> edges;                   // 规则图
    private static int nodeCnt;                                 // 供构图使用，节点编号
    private static Map<Integer, MutableNode> nodeTable;         // 节点哈希值和节点对象的对应表

    private static MutableGraph toLinkedList() {

        MutableGraph graph = mutGraph("Null " + r).setDirected(true);
        graph.graphAttrs().add(Label.graphName().locate(Label.Location.TOP));
        draw(graph);
        return graph;
    }

    private static void initializeRule(final String _r) {		// 由规则构造图

        r = _r;
        // 当前规则(Integer)
        int[] RULE = getRule(r);
        nodeCnt = 0;
        edges = new HashMap<>();
        nodeTable = new HashMap<>();
        Queue<Integer> processList = new ArrayDeque<>();
        int root = 273;		// 00(2^0 = 1), 11(2^4 = 16), 22(2^8 = 256)
        processList.offer(root);
        edges.put(root, new int[3]);
        while (!processList.isEmpty()) {
            int cur = processList.poll();
            MutableNode curNode = mutNode(String.valueOf(nodeCnt++));
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < 9; i++) {
                if (((cur >> i) & 1) == 1) {
                    if (i % 4 == 0) {
                        builder.append("<b>&nbsp;").append(toTwoBitTernaryString(i)).append("</b><br/>");
                    } else {
                        builder.append(toTwoBitTernaryString(i)).append("<br/>");
                    }
                }
            }
            curNode.add(Shape.RECTANGLE, Style.ROUNDED, Label.html(builder.toString()));
            if ((cur & root) == 0) {
                curNode.add(Style.combine(Style.FILLED, Style.ROUNDED), Color.RED);
            }
            nodeTable.put(cur, curNode);
            int[] children = edges.get(cur);
            for (int i = 0; i < 9; i++) {
                if (((cur >> i) & 1) == 1) {
                    int head = i * 3;
                    for (int tail = 0; tail < 3; tail++) {
                        children[RULE[head + tail]] |= (1 << ((head + tail) % 9));
                    }
                }
            }
            for (int child : children) {
                if (!edges.containsKey(child)) {
                    processList.offer(child);
                    edges.put(child, new int[3]);
                }
            }
        }
    }

    private static void draw(final MutableGraph g) {

        int sgCnt = 0;
        Map<String, Integer> condMap = new HashMap<>();
        boolean[][] dp = new boolean[2][512];
        int root = 273;
        dp[0][root] = true;
        for (int i = 0;; i++) {
            int now = i & 1, next = (i + 1) & 1;
            MutableGraph sg = mutGraph(String.valueOf(sgCnt++));
            sg.setCluster(true).graphAttrs().add(Label.html(i + ""));
            String cond = hash(dp[now]);
            MutableNode lastNode = null;
            for (int cur = 0; cur < 512; cur++) {
                if (!dp[now][cur]) continue;
                MutableNode curNode = nodeTable.get(cur).copy().setName(String.valueOf(nodeCnt++));
                sg.add(curNode);
                if (lastNode != null) {
                    sg.add(curNode.addLink(lastNode));
                }
                lastNode = curNode;
                int[] children = edges.get(cur);
                for (int child : children) {
                    dp[next][child] = true;
                }
                dp[now][cur] = false;
            }
            g.add(sg);
            if (condMap.containsKey(cond)) {
                sg.graphAttrs().add(Label.html(i + "(" + condMap.get(cond) + ")"));
                return;
            }
            condMap.put(cond, i);
        }
    }

    private static int[] getRule(String r) {
        if (r.length() != 27) {
            throw new IllegalArgumentException("规则长度必须为27。 输入长度：" + r.length());
        }
        int[] rule = new int[27];
        for (int i = 0; i < 27; i++) {
            int b = r.charAt(26 - i) - '0';
            if (b < 0 || b > 2) {
                throw new IllegalArgumentException("规则串必须仅由012组成。");
            }
            rule[i] = b;
        }
        return rule;
    }

    private static String hash(boolean[] distribution) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            long temp = 0;
            for (int j = 0; j < 64; j++) {
                temp <<= 1;
                temp += distribution[i * 64 + j] ? 1 : 0;
            }
            sb.append(temp);
            if (i < 7) sb.append(".");
        }
        return sb.toString();
    }

    private static String toTwoBitTernaryString(int num) {

        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < 2; i++) {
            buffer.insert(0, num % 3);
            num /= 3;
        }
        return buffer.toString();
    }

}
