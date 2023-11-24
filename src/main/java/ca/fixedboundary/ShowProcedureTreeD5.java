package ca.fixedboundary;

import static guru.nidi.graphviz.model.Factory.*;

import guru.nidi.graphviz.attribute.Attributes;
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

public final class ShowProcedureTreeD5 {

// public:
	public static void setBoundary(int[] left, int[] right) {
		
		if (left.length != 2) {
			throw new IllegalArgumentException("左边界长度必须为2。"
					+ "Length of left boundary must be 2. Input length: " + left.length);
		}
		if (right.length != 2) {
			throw new IllegalArgumentException("右边界长度必须为2。"
					+ "Length of right boundary must be 2. Input length: " + right.length);
		}
		for (int e : left) {
			if(e != 0 && e != 1) {
				throw new IllegalArgumentException("边界状态必须为0或1 。"
						+ "Boundary states must be 0 or 1.");
			}
		}
		for (int e : right) {
			if(e != 0 && e != 1) {
				throw new IllegalArgumentException("边界状态必须为0或1 。"
						+ "Boundary states must be 0 or 1.");
			}
		}
		System.arraycopy(left, 0, leftBoundary, 0, 2);
		System.arraycopy(right, 0, rightBoundary, 0, 2);
	}
	
	public static void storeImage(String r) throws IOException {
		
		MutableGraph graph = toGraph(r);
		StringBuffer buffer = new StringBuffer(r);
		buffer.append("_");
		for (int e : leftBoundary) {
			buffer.append(e + "");
		}
		buffer.append("_");
		for (int e : rightBoundary) {
			buffer.append(e + "");
		}
		String title = buffer.toString();
		Graphviz.fromGraph(graph).render(Format.PNG).toFile(new File(PATH + title + ".png"));
	}
	
	public static void setPath(String path) {
		PATH = path;
	}
	
// private:
	private static int[] leftBoundary = {0, 0};
	
	private static int[] rightBoundary = {0, 0};
	
	private static String PATH = "graph/";
	
	private static int count = 0;
	
	private static Map<Integer, Integer> procedureTree;
	
	private static Map<Integer, MutableNode> map;
	
	private static Set<Integer> uniqueSet; 
	
	private static Set<Integer> edenSet;
	
	private static MutableGraph toGraph(String r) {
		
		StringBuffer buffer = new StringBuffer("Fixed " + r + "<br/>");
		buffer.append(" left: ");
		for (int e : leftBoundary) {
			buffer.append(e + "");
		}
		buffer.append(" right: ");
		for (int e : rightBoundary) {
			buffer.append(e + "");
		}
		MutableGraph graph = mutGraph().setDirected(true);
		graph.graphAttrs().add(Label.html(buffer.toString()).locate(Location.TOP));
		procedureTree = new HashMap<Integer, Integer>();
		map = new HashMap<Integer, MutableNode>();
		uniqueSet = new HashSet<Integer>();
		edenSet = new HashSet<Integer>();
		count = 0;
		initializeTree(r);
		dfsNode(1, graph);
		return graph;
	}
	
	private static void initializeTree(String r) {

		if (r.length() != 32) {
			throw new IllegalArgumentException("规则长度必须为32 。"
					+ "Length of rules must be 32. Input rules: " + r);
		}
		if (r.charAt(0) != '0' && r.charAt(0) != '1') {
			throw new IllegalArgumentException("规则必须为01串。"
					+ "Input rules must be binary. Input rules: " + r);
		}
		int rules = (r.charAt(0) == '1' ? 1 : 0);
		for (int i = 1; i < 32; i++) {
			rules <<= 1;
			if (r.charAt(i) == '1') {
				rules++;
			} else if (r.charAt(i) != '0') {
				throw new IllegalArgumentException("规则必须为01串。"
						+ "Input rules must be binary. Input rules: " + r);
			}
		}
		Set<Integer> visited = new HashSet<Integer>();
		Queue<Integer> nodeList = new ArrayDeque<Integer>();
		int MASK = calMask();
		int root = calRoot();
		procedureTree.put(1, root);
		uniqueSet.add(1);
		visited.add(root);
		visited.add(0);
		nodeList.offer(2);
		nodeList.offer(3);
		while (!nodeList.isEmpty()) {
			int idx = nodeList.poll();
			int father = procedureTree.get(idx / 2);
			int curr = 0;
			for (int i = 0; i < 16; i++) {
				if (((father >> i) & 1) == 1) {	
					int head = (i << 1);
					for (int tail : new int[]{0, 1}) {
						if (((rules >> (head + tail)) & 1) == (idx & 1)) {
							curr |= (1 << ((head + tail) % 16));
						}
					}
				}
			}
			procedureTree.put(idx, curr);
			if ((curr & MASK) == 0) {
				edenSet.add(idx);
			}
			if (visited.contains(curr)) {
				continue;
			}
			visited.add(curr);
			uniqueSet.add(idx);
			nodeList.add(2 * idx);
			nodeList.add(2 * idx + 1);
		}
	}
	
	private static void dfsNode(int idx, MutableGraph graph) {
		
		if (!procedureTree.containsKey(idx)) {
			return;
		}
		int curr = procedureTree.get(idx);
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < 16; i++) {
			if (((curr >> i) & 1) == 1) {
				if (idx != 1 && isSpecialTuple(i)) {
					buffer.append("<b>&nbsp;");
				}
				buffer.append(toFourBitString(i));
				if (idx != 1 && isSpecialTuple(i)) {
					buffer.append("</b>");
				}
				buffer.append("<br/>");
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
	
	private static int calMask() {
		
		int factor = (rightBoundary[0] << 1) + rightBoundary[1];
		return 4369 * (1 << factor);	// 2^0 + 2^4 + 2^8 + 2^12 (0000, 0100, 1000, 1100)
	}
	
	private static int calRoot() {
		
		int factor = (leftBoundary[0] << 3) + (leftBoundary[1] << 2);
		return 15 * (1 << factor);	// 2^0 + 2^1 + 2^2 + 2^3 (0000, 0001, 0010, 0011)
	}
	
	private static boolean isSpecialTuple(int i) {
		int factor = (rightBoundary[0] << 1) + rightBoundary[1];
		if (i == (factor) || i == (4 + factor) || 
			i == (8 + factor) || i == (12 + factor)) {
			return true;
		}
		return false;
	}
	
	private static String toFourBitString(int num) {
		
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < 4; i++) {
			buffer.insert(0, num & 1);
			num >>= 1;
		}
		return buffer.toString();
	}

// main:
	public static void main(String[] args) throws IOException {
		
		setPath("graph/test2022.8.21/fixed/1/");
		String r = "11110000111100001111000010110010";
		run(new int[]{0, 0}, new int[]{0, 0}, r);
		run(new int[]{0, 0}, new int[]{0, 1}, r);
		run(new int[]{0, 0}, new int[]{1, 0}, r);
		run(new int[]{0, 0}, new int[]{1, 1}, r);
		run(new int[]{0, 1}, new int[]{0, 0}, r);
		run(new int[]{0, 1}, new int[]{0, 1}, r);
		run(new int[]{0, 1}, new int[]{1, 0}, r);
		run(new int[]{0, 1}, new int[]{1, 1}, r);
		run(new int[]{1, 0}, new int[]{0, 0}, r);
		run(new int[]{1, 0}, new int[]{0, 1}, r);
		run(new int[]{1, 0}, new int[]{1, 0}, r);
		run(new int[]{1, 0}, new int[]{1, 1}, r);
		run(new int[]{1, 1}, new int[]{0, 0}, r);
		run(new int[]{1, 1}, new int[]{0, 1}, r);
		run(new int[]{1, 1}, new int[]{1, 0}, r);
		run(new int[]{1, 1}, new int[]{1, 1}, r);
		
	}
	
	public static void run(int[] left, int[] right, String r) throws IOException {
		setBoundary(left, right);
		storeImage(r);
	}
}
