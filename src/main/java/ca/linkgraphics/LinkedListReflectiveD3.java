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
import java.util.*;

import static guru.nidi.graphviz.model.Factory.mutGraph;
import static guru.nidi.graphviz.model.Factory.mutNode;

public final class LinkedListReflectiveD3 {

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
        int rule = getRule(r);
        nodeCnt = 0;
        edges = new HashMap<>();
        nodeTable = new HashMap<>();
        Queue<Integer> processList = new ArrayDeque<>();
        int root = 9;
        processList.offer(root);
        edges.put(root, new int[2]);
        while (!processList.isEmpty()) {
            int cur = processList.poll();
            MutableNode curNode = mutNode(String.valueOf(nodeCnt++));
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < 4; i++) {
                if (((cur >> i) & 1) == 1) {
                    if (i % 3 == 0) {
                        builder.append("<b>&nbsp;").append(toTwoBitString(i)).append("</b><br/>");
                    } else {
                        builder.append(toTwoBitString(i)).append("<br/>");
                    }
                }
            }
            curNode.add(Shape.RECTANGLE, Style.ROUNDED, Label.html(builder.toString()));
            if ((cur & root) == 0) {
                curNode.add(Style.combine(Style.FILLED, Style.ROUNDED), Color.RED);
            }
            nodeTable.put(cur, curNode);
            int[] children = edges.get(cur);
            for (int i = 0; i < 4; i++) {
                if (((cur >> i) & 1) == 1) {
                    int head = (i << 1);
                    for (int tail = 0; tail < 2; tail++) {
                        children[((rule >> (head + tail)) & 1)] |= (1 << ((head + tail) % 4));
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
        int[] condMap = new int[1 << 16];
        Arrays.fill(condMap, -1);
        int[] dp = new int[2];
        int root = 9;
        dp[0] = 1 << root;
        for (int i = 0;; i++) {
            int now = i & 1, next = (i + 1) & 1;
            MutableGraph sg = mutGraph(String.valueOf(sgCnt++));
            sg.setCluster(true).graphAttrs().add(Label.html(i + ""));
            MutableNode lastNode = null;
            for (int cur = 0; cur < 16; cur++) {
                if (((dp[now] >> cur) & 1) == 0) continue;
                MutableNode curNode = nodeTable.get(cur).copy().setName(String.valueOf(nodeCnt++));
                sg.add(curNode);
                if (lastNode != null) {
                    sg.add(curNode.addLink(lastNode));
                }
                lastNode = curNode;
                int[] children = edges.get(cur);
                for (int child : children) {
                    dp[next] |= (1 << child);
                }
            }
            g.add(sg);
            if (condMap[dp[now]] != -1) {
                return;
            }
            condMap[dp[now]] = i;
            dp[now] = 0;
        }
    }

    private static int getRule(String r) {

        if (r.length() != 8) {
            throw new IllegalArgumentException("规则长度必须为8。 "
                    + "Length of input rule must be 8. Input rule: " + r);
        }
        int rule = 0;
        for (int i = 0; i < 8; i++) {
            rule <<= 1;
            if (r.charAt(i) == '1') {
                rule |= 1;
            } else if (r.charAt(i) != '0') {
                throw new IllegalArgumentException("规则必须为01串。"
                        + "Input rule must be binary. Input rule: " + r);
            }
        }
        return rule;
    }

    private static String toTwoBitString(int num) {
        return ((num >> 1) & 1) + "" + (num & 1);
    }

}
