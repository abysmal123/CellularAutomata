package ca.periodic;

import guru.nidi.graphviz.attribute.Label;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.model.MutableNode;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static guru.nidi.graphviz.model.Factory.*;

public class PeriodicBucketChain {
    public static void storeImage(String r, int d, String fileName) throws IOException {
        if (r.length() != 1 << d) {
            throw new IllegalArgumentException("规则长度与设定的直径不符。");
        }
        diameter = d;
        MutableGraph graph = toGraph(r);
        Graphviz.fromGraph(graph).render(Format.PNG).toFile(new File(PATH + fileName + ".png"));
    }

    public static void storeImage(String r, String fileName) throws IOException {
        int d = 0, len = r.length();
        while (len > 1) {
            if ((len & 1) == 1) {
                throw new IllegalArgumentException("规则长度不为2的整数幂。");
            }
            d++;
            len >>= 1;
        }
        if (d < 3) {
            throw new IllegalArgumentException("直径至少为3。");
        }
        storeImage(r, d, fileName);
    }

    public static int bucketCountWhenEnds(String r) {
        diameter = checkLength(r);
        boolean[] RULE = getRule(r);
        edges = new HashMap<>();
        Queue<PTNode> processList = new ArrayDeque<>();
        PTNode root = PTNode.getSelfNode(diameter - 1);
        processList.offer(root);
        edges.put(root, root.getChildren(RULE));
        while (!processList.isEmpty()) {
            PTNode cur = processList.poll();
            PTNode[] children = edges.get(cur);
            for (int k = 0; k < 2; k++) {
                if (!edges.containsKey(children[k])) {
                    processList.offer(children[k]);
                    edges.put(children[k], children[k].getChildren(RULE));
                }
            }
        }
        int bucCnt = 0;
        Map<String, Integer> condMap = new HashMap<>();
        Set<PTNode> curBucket = new HashSet<>();
        PTNode empty = PTNode.getEmptyNode(diameter - 1);
        curBucket.add(root);
        while (true) {
            Set<PTNode> nextBucket = new HashSet<>();
            for (PTNode ptn : curBucket) {
                Collections.addAll(nextBucket, edges.get(ptn));
            }
            String curBucHash = bucketHash(curBucket);
            if (condMap.containsKey(curBucHash)) {
                return bucCnt + 1;
            }
            if (curBucket.contains(empty)) {
                return bucCnt + 1;
            }
            condMap.put(curBucHash, bucCnt);
            curBucket = nextBucket;
            bucCnt++;
        }
    }

    public static String reversibilityFunction(String r, int n) {
        diameter = checkLength(r);
        boolean[] RULE = getRule(r);
        edges = new HashMap<>();
        Queue<PTNode> processList = new ArrayDeque<>();
        PTNode empty = PTNode.getEmptyNode(diameter - 1);
        PTNode root = PTNode.getSelfNode(diameter - 1);
        processList.offer(root);
        edges.put(root, root.getChildren(RULE));
        while (!processList.isEmpty()) {
            PTNode cur = processList.poll();
            PTNode[] children = edges.get(cur);
            for (int k = 0; k < 2; k++) {
                if (!edges.containsKey(children[k])) {
                    processList.offer(children[k]);
                    edges.put(children[k], children[k].getChildren(RULE));
                }
            }
        }
        StringBuilder builder = new StringBuilder();
        int bucCnt = 0;
        Set<PTNode> curBucket = new HashSet<>();
        curBucket.add(root);
        while (bucCnt <= n) {
            char reversibility = '1';
            Set<PTNode> nextBucket = new HashSet<>();
            for (PTNode ptn : curBucket) {
                if (ptn.isEden()) {
                    reversibility = '0';
                }
                Collections.addAll(nextBucket, edges.get(ptn));
            }
            curBucket = nextBucket;
            if (bucCnt != 0) {
                builder.append(reversibility);
            }
            bucCnt++;
            if (curBucket.contains(empty)) {
                for (int i = bucCnt; i <= n; i++) {
                    builder.append('0');
                }
                return builder.toString();
            }
        }
        return builder.toString();
    }

    public static void setPath(String path) {
        PATH = path;
    }

    // private:
    private static int diameter = 3;

    private static String PATH = "graph/";

    private static int count = 0;

    private static Map<PTNode, PTNode[]> edges;

    private static Map<PTNode, MutableNode> nodeTable;

    private static MutableGraph toGraph(String r) {

        MutableGraph graph = mutGraph("Periodic " + r).setDirected(true);
        graph.graphAttrs().add(Label.graphName().locate(Label.Location.TOP));
        count = 0;
        edges = new HashMap<>();
        nodeTable = new HashMap<>();
        draw(r, graph);
        return graph;
    }

    private static void draw(final String r, MutableGraph g) {

        initialize(r);
        int sgCnt = 0;
        Map<String, Integer> condMap = new HashMap<>();
        Set<PTNode> curBucket = new HashSet<>();
        PTNode empty = PTNode.getEmptyNode(diameter - 1);
        PTNode root = PTNode.getSelfNode(diameter - 1);
        curBucket.add(root);
        while (true) {
            MutableGraph sg = mutGraph(String.valueOf(sgCnt));
            sg.setCluster(true).graphAttrs().add(Label.html(sgCnt + ""));
            MutableNode lastNode = null;
            Set<PTNode> nextBucket = new HashSet<>();
            for (PTNode ptn : curBucket) {
                MutableNode curNode = nodeTable.get(ptn).copy().setName(String.valueOf(count++));
                sg.add(curNode);
                if (lastNode != null) {
                    sg.add(curNode.addLink(lastNode));
                }
                lastNode = curNode;
                Collections.addAll(nextBucket, edges.get(ptn));
            }
            g.add(sg);
            String curBucHash = bucketHash(curBucket);
            if (condMap.containsKey(curBucHash)) {
                sg.graphAttrs().add(Label.html(sgCnt + "(" + condMap.get(curBucHash) + ")"));
                return;
            }
            if (curBucket.contains(empty)) {
                sg.graphAttrs().add(Label.html(sgCnt + "(empty)"));
                return;
            }
            condMap.put(curBucHash, sgCnt);
            curBucket = nextBucket;
            sgCnt++;
        }
    }

    private static void initialize(String r) {
        boolean[] RULE = getRule(r);
        Queue<PTNode> processList = new ArrayDeque<>();
        PTNode root = PTNode.getSelfNode(diameter - 1);
        processList.offer(root);
        edges.put(root, root.getChildren(RULE));
        MutableNode rootNode = mutNode((count++) + "");
        root.writeNode(rootNode);
        nodeTable.put(root, rootNode);
        while (!processList.isEmpty()) {
            PTNode cur = processList.poll();
            PTNode[] children = edges.get(cur);
            for (int k = 0; k < 2; k++) {
                if (!edges.containsKey(children[k])) {
                    processList.offer(children[k]);
                    edges.put(children[k], children[k].getChildren(RULE));
                    MutableNode childNode = mutNode((count++) + "");
                    children[k].writeNode(childNode);
                    nodeTable.put(children[k], childNode);
                }
            }
        }
    }

    private static String bucketHash(Set<PTNode> bucket) {
        String[] nodeStrArray = new String[bucket.size()];
        int i = 0;
        for (PTNode ptn : bucket) {
            nodeStrArray[i++] = ptn.toString();
        }
        Arrays.sort(nodeStrArray);
        return String.join("", nodeStrArray);
    }

    private static int checkLength(String r) {
        int d = 0, len = r.length();
        while (len > 1) {
            if ((len & 1) == 1) {
                throw new IllegalArgumentException("规则长度不为2的整数幂。");
            }
            d++;
            len >>= 1;
        }
        if (d < 3) {
            throw new IllegalArgumentException("直径至少为3。");
        }
        return d;
    }

    private static boolean[] getRule(String r) {
        int len = r.length();
        boolean[] rule = new boolean[len];
        for (int i = 0; i < len; i++) {
            rule[len - i - 1] = (r.charAt(i) == '1');
        }
        return rule;
    }
}
