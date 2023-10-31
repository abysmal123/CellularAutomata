package ca.reflectivef3;

import guru.nidi.graphviz.attribute.Color;
import guru.nidi.graphviz.attribute.Label;
import guru.nidi.graphviz.attribute.Label.Location;
import guru.nidi.graphviz.attribute.Shape;
import guru.nidi.graphviz.attribute.Style;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.model.MutableNode;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static guru.nidi.graphviz.model.Factory.*;

public final class ShowLayerGraphD3 {

// public:
	public static void storeImage(String r, String title) throws IOException {
		
		MutableGraph graph = toGraph(r);
		Graphviz.fromGraph(graph).render(Format.PNG).toFile(new File(PATH + title + ".png"));
	}
	
	public static void setPath(String path) {
		PATH = path;
	}
	
	public static int countNodes(String r) {
		toGraph(r);
		return count;
	}
	
// private:
	private static String PATH = "graph/";
	
	private static int count = 0;
	
	public static Map<Integer, int[]> edges;
	
	public static Map<Integer, MutableNode> nodeTable;
	
	public static Set<Integer> edens;
	
	private static MutableGraph toGraph(String r) {
		
		MutableGraph graph = mutGraph("Reflective " + r).setDirected(true);
		graph.graphAttrs().add(Label.graphName().locate(Location.TOP));
		count = 0;
		edges = new HashMap<>();
		nodeTable = new HashMap<>();
		edens = new HashSet<>();
		draw(r, graph);
		return graph;
	}
	
	private static void draw(final String r, MutableGraph graph) {
		
		int[] RULE = getRule(r);
		edges = new HashMap<>();
		edens = new HashSet<>();
		Queue<Integer> processList = new ArrayDeque<>();
		int MASK = 273;
		int root = 273;
		processList.offer(root);
		edges.put(root, new int[3]);
		
		// 创建root图节点
		MutableNode rootNode = mutNode((count++) + "");
		rootNode.add(Shape.RECTANGLE, Style.ROUNDED, Label.html(0 + ""));
		
		nodeTable.put(root, rootNode);
		graph.add(rootNode);
		int layer = 1;
		while (!processList.isEmpty()) {
			int size = processList.size();
			while (size > 0) {
				int cur = processList.poll();
				MutableNode curNode = nodeTable.get(cur);
				if ((cur & MASK) == 0) {
					edens.add(cur);
					curNode.add(Style.combine(Style.FILLED, Style.ROUNDED), Color.RED);
				}
				int[] children = edges.get(cur);
				for (int i = 0; i < 9; i++) {
					if (((cur >> i) & 1) == 1) {	
						int head = i * 3;
						for (int tail = 0; tail < 3; tail++) {
							children[RULE[head + tail]] |= (1 << ((head + tail) % 9));
						}
					}
				}
				for (int k = 0; k < 3; k++) {
					MutableNode childNode = null;
					if (!edges.containsKey(children[k])) {
						processList.offer(children[k]);
						edges.put(children[k], new int[3]);
						childNode = mutNode((count++) + "");
						childNode.add(Shape.RECTANGLE, Style.ROUNDED, Label.html(layer + ""));
						graph.add(childNode);
						nodeTable.put(children[k], childNode);
					} else {
						childNode = nodeTable.get(children[k]);
					}
					graph.add(curNode.addLink(to(childNode).add(Label.of((k) + ""))));
				}
				size--;
			}
			layer++;
		}
	}
	
	public static int[] getRule(String r) {
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
	
	public static void main(String[] args) throws IOException {
		setPath("graph/test2023.8.29/");
		String r = "222122122001001000110210211";
		storeImage(r, r);
	}
	
}
