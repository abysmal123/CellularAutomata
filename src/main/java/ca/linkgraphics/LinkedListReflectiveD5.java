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

public final class LinkedListReflectiveD5 {

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
        int[] RULE = getRule(r);
        nodeCnt = 0;
        edges = new HashMap<>();
        nodeTable = new HashMap<>();
        Queue<Integer> processList = new ArrayDeque<>();
        int root = 33345;           // 2^0 + 2^6 + 2^9 + 2^15 (0000, 0110, 1001, 1111)
        processList.offer(root);
        edges.put(root, new int[2]);
        while (!processList.isEmpty()) {
            int cur = processList.poll();
            MutableNode curNode = mutNode(String.valueOf(nodeCnt++));
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < 16; i++) {
                if (((cur >> i) & 1) == 1) {
                    if (i == 0 || i == 6 || i == 9 || i == 15) {
                        builder.append("<b>&nbsp;").append(toThreeBitString(i)).append("</b><br/>");
                    } else {
                        builder.append(toThreeBitString(i)).append("<br/>");
                    }
                }
            }
            curNode.add(Shape.RECTANGLE, Style.ROUNDED, Label.html(builder.toString()));
            if ((cur & root) == 0) {
                curNode.add(Style.combine(Style.FILLED, Style.ROUNDED), Color.RED);
            }
            nodeTable.put(cur, curNode);
            int[] children = edges.get(cur);
            for (int i = 0; i < 16; i++) {
                if (((cur >> i) & 1) == 1) {
                    int head = (i << 1);
                    for (int tail = 0; tail < 2; tail++) {
                        children[RULE[head + tail]] |= (1 << ((head + tail) % 16));
                    }
                }
            }
            for (int child : children) {
                if (!edges.containsKey(child)) {
                    processList.offer(child);
                    edges.put(child, new int[2]);
                }
            }
        }
    }

    private static void draw(final MutableGraph g) {

        int sgCnt = 0;
        Map<String, Integer> condMap = new HashMap<>();
        boolean[][] dp = new boolean[2][65536];
        int root = 33345;
        dp[0][root] = true;
        for (int i = 0;; i++) {
            int now = i & 1, next = (i + 1) & 1;
            MutableGraph sg = mutGraph(String.valueOf(sgCnt++));
            sg.setCluster(true).graphAttrs().add(Label.html(i + ""));
            String cond = hash(dp[now]);
            MutableNode lastNode = null;
            for (int cur = 0; cur < 65536; cur++) {
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
                return;
            }
            condMap.put(cond, i);
        }
    }

    private static int[] getRule(String r) {
        if (r.length() != 32) {
            throw new IllegalArgumentException("规则长度必须为32。 输入长度：" + r.length());
        }
        int[] rule = new int[32];
        for (int i = 0; i < 32; i++) {
            int b = r.charAt(31 - i) - '0';
            if (b < 0 || b > 1) {
                throw new IllegalArgumentException("规则串必须仅由01组成。");
            }
            rule[i] = b;
        }
        return rule;
    }

    private static String hash(boolean[] distribution) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            int temp = 0;
            for (int j = 0; j < 32; j++) {
                temp <<= 1;
                temp += distribution[i * 32 + j] ? 1 : 0;
            }
            sb.append(temp);
            if (i < 7) sb.append(".");
        }
        return sb.toString();
    }

    private static String toThreeBitString(int num) {
        return ((num >> 3) & 1) + "" + ((num >> 2) & 1) + ((num >> 1) & 1) + (num & 1);
    }

}
