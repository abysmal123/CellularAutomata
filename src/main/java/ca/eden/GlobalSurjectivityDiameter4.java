package ca.eden;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public final class GlobalSurjectivityDiameter4 {
	
// public:
	public GlobalSurjectivityDiameter4(final String r, int model) {
		
		if (r.length() != 16) {
			throw new IllegalArgumentException("直径为4的规则长度必须为16 。"
					+ "Length of 4 diameters' rules must be 16. Input rules: " + r);
		}
		if (r.charAt(0) != '0' && r.charAt(0) != '1') {
			throw new IllegalArgumentException("规则必须为01串。"
					+ "Input rules must be binary. Input rules: " + r);
		}
		rules = r.charAt(0) == '1' ? 1 : 0;
		for (int i = 1; i < 16; i++) {
			rules <<= 1;
			if (r.charAt(i) == '1') {
				rules++;
			} else if (r.charAt(i) != '0') {
				throw new IllegalArgumentException("规则必须为01串。"
						+ "Input rules must be binary. Input rules: " + r);
			}
		}
		boolean[] visited = new boolean[65536];
		visited[0] = true;
		List<String> edens = new ArrayList<String>();
		Queue<TreeNode> nodeList = new ArrayDeque<TreeNode>();
		if (model == 0) {
			rootNode = new TreeNode(65535 - rules);
		} else if (model == 1) {
			rootNode = new TreeNode(rules);
		} else {
			rootNode = new TreeNode(65535);
		}
		nodeList.add(rootNode);
		while (!nodeList.isEmpty()) {
			TreeNode curr = nodeList.poll();
			if (curr.values == 0) {
				StringBuffer buffer = new StringBuffer();
				while (curr.former != null) {
					buffer.insert(0, curr.type);
					curr = curr.former;
				}
				edens.add(buffer.toString());
			}
			if (visited[curr.values]) {
				continue;
			}
			visited[curr.values] = true;
			int zeroValues = 0;
			int oneValues = 0;
			for (int i = 0; i < 16; i++) {
				if (((curr.values >> i) & 1) == 1) {
					int pos = (i << 1) % 16;
					for (int j : new int[]{0, 1}) {
						if (((rules >> (pos + j)) & 1) == 0) {
							zeroValues |= (1 << (pos + j));
						} else {
							oneValues |= (1 << (pos + j));
						}
					}
				}
			}
			TreeNode nextZeroEdge = new TreeNode(zeroValues);
			nextZeroEdge.former = curr;
			nextZeroEdge.type = 0;
			nodeList.add(nextZeroEdge);
			curr.zeroEdge = nextZeroEdge;
			TreeNode nextOneEdge = new TreeNode(oneValues);
			nextOneEdge.former = curr;
			nextOneEdge.type = 1;
			nodeList.add(nextOneEdge);
			curr.oneEdge = nextOneEdge;
		}
		edenSet = new String[edens.size()];
		for (int i = 0; i < edens.size(); i++) {
			edenSet[i] = edens.get(i);
		}
	}
	
	public int getRules() {
		
		return rules;
	}
	
	public TreeNode getRootNode() {
		
		return rootNode;
	}
	
	public String[] getEdens() {
		
		int len = edenSet.length;
		String[] edens = new String[len];
		for (int i = 0; i < len; i++) {
			edens[i] = new String(edenSet[i]);
		}
		return edens;
	}
	
	
// private:
	private int rules;

	private TreeNode rootNode;
	
	private String[] edenSet;
	
}
