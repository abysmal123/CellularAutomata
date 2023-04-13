package ca.paperinjectivity;

import static guru.nidi.graphviz.model.Factory.*;

import guru.nidi.graphviz.attribute.Attributes;
import guru.nidi.graphviz.attribute.Label;
import guru.nidi.graphviz.attribute.Rank;
import guru.nidi.graphviz.attribute.Rank.RankDir;
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

public final class VisualizationD4 {

// public:
	public static void storeImage(String r) throws IOException {
		
		MutableGraph graph = toGraph(r);
		try {
			Graphviz.fromGraph(graph).render(Format.PNG).toFile(new File(PATH + r + ".png"));
		} catch (RuntimeException e) {
			e.printStackTrace(System.out);
		}
		
	}
	
	public static void setPath(String path) {
		PATH = path;
	}
	
//private:
	private static String PATH = "graph/";
	
	private static int count = 0;
	
	private static Map<Integer, ValueSet> procedureTree;
	
	private static Map<Integer, MutableNode> map;
	
	private static Set<Integer> uniqueSet; 
	
	private static Set<Integer> edenSet;
	
	private static MutableGraph toGraph(String r) {
		
		MutableGraph graph = mutGraph("Injectivity " + r).setDirected(true);
		graph.graphAttrs().add(Rank.dir(RankDir.LEFT_TO_RIGHT));
		procedureTree = new HashMap<Integer, ValueSet>();
		map = new HashMap<Integer, MutableNode>();
		uniqueSet = new HashSet<Integer>();
		edenSet = new HashSet<Integer>();
		count = 0;
		initializeTree(r);
		dfsNode(1, graph);
		return graph;
	}
	
	private static void initializeTree(String r) {

		if (r.length() != 16) {
			throw new IllegalArgumentException("规则长度必须为16 。"
					+ "Length of rules must be 16. Input rules: " + r);
		}
		if (r.charAt(0) != '0' && r.charAt(0) != '1') {
			throw new IllegalArgumentException("规则必须为01串。"
					+ "Input rules must be binary. Input rules: " + r);
		}
		int rules = (r.charAt(0) == '1' ? 1 : 0);
		for (int i = 1; i < 16; i++) {
			rules <<= 1;
			if (r.charAt(i) == '1') {
				rules++;
			} else if (r.charAt(i) != '0') {
				throw new IllegalArgumentException("规则必须为01串。"
						+ "Input rules must be binary. Input rules: " + r);
			}
		}
		Set<String> visited = new HashSet<String>();
		Queue<Integer> nodeList = new ArrayDeque<Integer>();
		int[] rootSubValues = new int[8];
		rootSubValues[0] = 1;
		for (int i = 1; i < 8; i++) {
			rootSubValues[i] = rootSubValues[i - 1] * 2;
		}
		ValueSet root = new ValueSet(rootSubValues);
		procedureTree.put(1, root);
		uniqueSet.add(1);
		visited.add(hash(root));
		visited.add(hash(new ValueSet(new int[8])));
		nodeList.offer(2);
		nodeList.offer(3);
		while (!nodeList.isEmpty()) {
			int idx = nodeList.poll();
			ValueSet father = procedureTree.get(idx / 2);
			int[] subValues = new int[8];
			int tupleCount = 0;
			int periodicCount = 0;
			for (int k = 0; k < 8; k++) {
				for (int i = 0; i < 8; i++) {
					if (((father.subValues[k] >> i) & 1) == 1) {	
						int pos = (i << 1);
						for (int j : new int[]{0, 1}) {
							if (((rules >> (pos + j)) & 1) == (idx & 1)) {
								tupleCount++;
								subValues[k] |= (1 << ((pos + j) % 8));
								if ((pos + j) % 8 == k) {
									periodicCount++;
								}
							}
						}
					}
				}
			}
			ValueSet curr = new ValueSet(subValues);
			procedureTree.put(idx, curr);
			if (tupleCount == 0 || periodicCount > 1) {
				edenSet.add(idx);
			}
			if (visited.contains(hash(curr))) {
				continue;
			}
			visited.add(hash(curr));
			uniqueSet.add(idx);
			nodeList.add(2 * idx);
			nodeList.add(2 * idx + 1);
		}
	}
	
	private static String hash(ValueSet vs) {
		
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < vs.subValues.length; i++) {
			buffer.append(vs.subValues[i] + ".");
		}
		return buffer.toString();
	}
	
	private static void dfsNode(int idx, MutableGraph graph) {
		
		if (!procedureTree.containsKey(idx)) {
			return;
		}
		int[] subValues = procedureTree.get(idx).subValues;
		StringBuffer buffer = new StringBuffer();
		for (int k = 0; k < 8; k++) {
			for (int i = 0; i < 8; i++) {
				if (((subValues[k] >> i) & 1) == 1) {
					if (i == k) {
						buffer.append("<b>");
					}
					buffer.append(toThreeBitString(k) + "&nbsp;&nbsp;" + toThreeBitString(i));
					if (i == k) {
						buffer.append("</b>");
					}
					buffer.append("<br/>");
				}
			}
		}
		if (buffer.length() > 0 && !uniqueSet.contains(idx)) {
			buffer.append("*");
		}
		MutableNode graphNode = mutNode((count++) + "");
		graphNode.add(Shape.RECTANGLE, Style.ROUNDED, Label.html(buffer.toString()));
		graph.add(graphNode);
		map.put(idx, graphNode);
		if (edenSet.contains(idx)) {
			graphNode.add(Attributes.attr("peripheries", 2));
		}
		if (idx > 1) {
			graph.add(map.get(idx / 2).addLink(to(graphNode).add(Label.of(((idx & 1) == 1 ? 1 : 0) + ""))));
		}
		dfsNode(2 * idx, graph);
		dfsNode(2 * idx + 1, graph);
	}
	
	private static String toThreeBitString(int num) {
		
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < 3; i++) {
			buffer.insert(0, num & 1);
			num >>= 1;
		}
		return buffer.toString();
	}
	
// test main:
	public static void main(String[] args) throws IOException {
		String r = "0010110100001111";
		setPath("graph/test2022.11.06/injectivity/");
		storeImage(r);
	}
}
