package ca.periodic2;

import static guru.nidi.graphviz.model.Factory.*;

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
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public final class ShowLayerGraphD4 {

// public:
	public static void storeImage(String r, String fileName) throws IOException {
		
		MutableGraph graph = toGraph(r);
		Graphviz.fromGraph(graph).render(Format.PNG).toFile(new File(PATH + fileName + ".png"));
	}
	
	public static void setPath(String path) {
		PATH = path;
	}
	
// private:
	private static String PATH = "graph/";
	
	private static int count = 0;
	
	public static Map<ValueSet, ValueSet[]> edges;
	
	public static Map<ValueSet, MutableNode> nodeTable;
	
	public static Set<ValueSet> edens;
	
	private static MutableGraph toGraph(String r) {
		
		MutableGraph graph = mutGraph("Periodic " + r).setDirected(true);
		graph.graphAttrs().add(Label.graphName().locate(Location.TOP));
		count = 0;
		edges = new HashMap<>();
		nodeTable = new HashMap<>();
		edens = new HashSet<>();
		draw(r, graph);
		return graph;
	}
	
	public static void draw(final String r, MutableGraph graph) {
		
		int rule = getRule(r, 1 << 4);
		edges = new HashMap<>();
		edens = new HashSet<>();
		Queue<ValueSet> processList = new ArrayDeque<>();
		ValueSet root = new ValueSet(new int[]{1, 2, 4, 8, 16, 32, 64, 128});
		processList.offer(root);
		edges.put(root, new ValueSet[]{new ValueSet(8), new ValueSet(8)});
		
		// 创建root图节点
		MutableNode rootNode = mutNode((count++) + "");
		rootNode.add(Shape.RECTANGLE, Style.ROUNDED, Label.html(0 + ""));
		
		nodeTable.put(root, rootNode);
		graph.add(rootNode);
		int layer = 1;
		while (!processList.isEmpty()) {
			int size = processList.size();
			while (size > 0) {
				ValueSet cur = processList.poll();
				MutableNode curNode = nodeTable.get(cur);
				if (isTerrible(cur)) {
					edens.add(cur);
					curNode.add(Style.combine(Style.FILLED, Style.ROUNDED), Color.RED);
				}
				ValueSet[] children = edges.get(cur);
				for (int k = 0; k < 8; k++) {
					for (int i = 0; i < 8; i++) {
						if (((cur.subValues[k] >> i) & 1) == 1) {	
							int head = (i << 1);
							for (int tail = 0; tail < 2; tail++) {
								children[((rule >> (head + tail)) & 1)].subValues[k] |= (1 << ((head + tail) % 8));
							}
						}
					}
				}
				for (int ch = 0; ch < 2; ch++) {
					MutableNode childNode = null;
					if (!edges.containsKey(children[ch])) {
						processList.offer(children[ch]);
						edges.put(children[ch], new ValueSet[]{new ValueSet(8), new ValueSet(8)});
						childNode = mutNode((count++) + "");
						childNode.add(Shape.RECTANGLE, Style.ROUNDED, Label.html(layer + ""));
						graph.add(childNode);
						nodeTable.put(children[ch], childNode);
					} else {
						childNode = nodeTable.get(children[ch]);
					}
					graph.add(curNode.addLink(to(childNode).add(Label.of(ch + ""))));
				}
				size--;
			}
			layer++;
		}
	}
	
	private static int getRule(String r, int len) {
		
		if (r.length() != len) {
			throw new IllegalArgumentException("规则长度必须为" + len + "。 "
					+ "Length of input rule must be " + len + ". Input rule: " + r);
		}
		int rule = 0;
		for (int i = 0; i < len; i++) {
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
	
	private static boolean isTerrible(ValueSet v) {
		for (int i = 0; i < v.subValues.length; i++) {
			if (((v.subValues[i] >> i) & 1) == 1) {
				return false;
			}
		}
		return true;
	}
	
	public static void main(String[] args) throws IOException {
		String r = "1111000011110000";
		setPath("graph/test2023.8.31/periodic/");
		storeImage(r, r + "(layer)");
		//
	}
	
}
