package ca.nonlinear;

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

public final class ShowProcedureTreeECA {

// public:
	public static void storeImage(String r) throws IOException {
		
		MutableGraph graph = toGraph(r);
		Graphviz.fromGraph(graph).render(Format.PNG).toFile(new File(PATH + r + ".png"));
	}
	
	public static void setPath(String path) {
		PATH = path;
	}
	
// private:
	private static String PATH = "graph/";
	
	private static int count = 0;
	
	public static Map<Integer, int[]> edges;
	
	public static Map<Integer, MutableNode> nodeTable;
	
	public static Set<Integer> edens;
	
	private static MutableGraph toGraph(String r) {
		
		MutableGraph graph = mutGraph("Null " + r).setDirected(true);
		graph.graphAttrs().add(Label.graphName().locate(Location.TOP));
		count = 0;
		edges = new HashMap<>();
		nodeTable = new HashMap<>();
		edens = new HashSet<>();
		draw(r, graph);
		return graph;
	}
	
	public static void draw(final String r, MutableGraph graph) {
		
		int rule = getRule(r, 1 << 3);
		edges = new HashMap<>();
		edens = new HashSet<>();
		Queue<Integer> processList = new ArrayDeque<>();
		int MASK = 5;
		int root = 3;
		processList.offer(root);
		edges.put(root, new int[2]);
		
		// 创建root图节点
		MutableNode rootNode = mutNode((count++) + "");
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < 4; i++) {
			if (((root >> i) & 1) == 1) {
				if (i == 0 || i == 2) {
					buffer.append("<b>&nbsp;");
				}
				buffer.append(toTwoBitString(i));
				if (i == 0 || i == 2) {
					buffer.append("</b>");
				}
				buffer.append("<br/>");
			}
		}
		rootNode.add(Shape.RECTANGLE, Style.ROUNDED, Label.html(buffer.toString()));
		
		nodeTable.put(root, rootNode);
		graph.add(rootNode);
		while (!processList.isEmpty()) {
			int cur = processList.poll();
			MutableNode curNode = nodeTable.get(cur);
			if ((cur & MASK) == 0) {
				edens.add(cur);
				curNode.add(Style.combine(Style.FILLED, Style.ROUNDED), Color.RED);
			}
			int[] childs = edges.get(cur);
			for (int i = 0; i < 4; i++) {
				if (((cur >> i) & 1) == 1) {	
					int head = (i << 1);
					for (int tail = 0; tail < 2; tail++) {
						childs[((rule >> (head + tail)) & 1)] |= (1 << ((head + tail) % 4));
					}
				}
			}
			for (int k = 0; k < 2; k++) {
				MutableNode childNode = null;
				if (!edges.containsKey(childs[k])) {
					processList.offer(childs[k]);
					edges.put(childs[k], new int[2]);
					childNode = mutNode((count++) + "");
					buffer = new StringBuffer();
					for (int i = 0; i < 4; i++) {
						if (((childs[k] >> i) & 1) == 1) {
							if (i == 0 || i == 2) {
								buffer.append("<b>&nbsp;");
							}
							buffer.append(toTwoBitString(i));
							if (i == 0 || i == 2) {
								buffer.append("</b>");
							}
							buffer.append("<br/>");
						}
					}
					childNode.add(Shape.RECTANGLE, Style.ROUNDED, Label.html(buffer.toString()));
					graph.add(childNode);
					nodeTable.put(childs[k], childNode);
				} else {
					childNode = nodeTable.get(childs[k]);
				}
				graph.add(curNode.addLink(to(childNode).add(Label.of((k == 1 ? 1 : 0) + ""))));
			}
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
	
	private static String toTwoBitString(int num) {
		
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < 2; i++) {
			buffer.insert(0, num & 1);
			num >>= 1;
		}
		return buffer.toString();
	}
	
	public static void main(String[] args) throws IOException {
		setPath("graph/test2023.3.6/");
		storeImage("01110100");
	}
	
}
