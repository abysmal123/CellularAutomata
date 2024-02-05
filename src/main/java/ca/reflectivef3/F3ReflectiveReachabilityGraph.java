package ca.reflectivef3;

import guru.nidi.graphviz.attribute.Label;
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

import static guru.nidi.graphviz.model.Factory.*;

public final class F3ReflectiveReachabilityGraph {
    // public:

    public static void storeImage(String r, int d, String fileName) throws IOException {
        if (r.length() != F3RTNode.powOfThree(d)) {
            throw new IllegalArgumentException("规则长度与设定的直径不符。");
        }
        if ((d & 1) == 0) {         // 偶数直径向上转化为奇数直径
            storeImage(r + r + r, d + 1, fileName);
        } else {
            diameter = d;
            MutableGraph graph = toGraph(r);
            Graphviz.fromGraph(graph).render(Format.PNG).toFile(new File(PATH + fileName + ".png"));
        }
    }

    public static void storeImage(String r, String fileName) throws IOException {
        int d = 0, len = r.length();
        while (len > 1) {
            if (len % 3 != 0) {
                throw new IllegalArgumentException("规则长度不为3的整数幂。");
            }
            d++;
            len /= 3;
        }
        if (d < 3) {
            throw new IllegalArgumentException("直径至少为3。");
        }
        storeImage(r, d, fileName);
    }

    public static void setPath(String path) {
        PATH = path;
    }

    // private:
    private static int diameter = 3;

    private static String PATH = "graph/";

    private static int count = 0;

    private static Map<F3RTNode, F3RTNode[]> edges;

    private static Map<F3RTNode, MutableNode> nodeTable;

    private static MutableGraph toGraph(String r) {

        MutableGraph graph = mutGraph("Reflective " + r).setDirected(true);
        graph.graphAttrs().add(Label.graphName().locate(Label.Location.TOP));
        count = 0;
        edges = new HashMap<>();
        nodeTable = new HashMap<>();
        draw(r, graph);
        return graph;
    }

    private static void draw(final String r, MutableGraph graph) {

        int[] RULE = getRule(r);
        Queue<F3RTNode> processList = new ArrayDeque<>();
        F3RTNode root = F3RTNode.getPalindromeNode(diameter - 1);
        processList.offer(root);
        edges.put(root, root.getChildren(RULE));
        MutableNode rootNode = mutNode((count++) + "");
        root.writeNode(rootNode);
        nodeTable.put(root, rootNode);
        graph.add(rootNode);
        while (!processList.isEmpty()) {
            F3RTNode cur = processList.poll();
            MutableNode curNode = nodeTable.get(cur);
            F3RTNode[] children = edges.get(cur);
            for (int k = 0; k < 3; k++) {
                MutableNode childNode;
                if (!edges.containsKey(children[k])) {
                    processList.offer(children[k]);
                    edges.put(children[k], children[k].getChildren(RULE));
                    childNode = mutNode((count++) + "");
                    children[k].writeNode(childNode);
                    graph.add(childNode);
                    nodeTable.put(children[k], childNode);
                } else {
                    childNode = nodeTable.get(children[k]);
                }
                graph.add(curNode.addLink(to(childNode).add(Label.of((k) + ""))));
            }
        }
    }

    private static int[] getRule(String r) {
        int len = r.length();
        int[] rule = new int[len];
        for (int i = 0; i < len; i++) {
            rule[len - i - 1] = r.charAt(i) - '0';
        }
        return rule;
    }

}
