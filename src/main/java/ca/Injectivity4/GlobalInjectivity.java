package ca.Injectivity4;

import java.util.ArrayList;
import java.util.List;

public class GlobalInjectivity {
	
	public GlobalInjectivity(int m) {	// 直径为 m
		
		this.m = m;
		initialize();
	}
	
	private int m;
	private int numOfNodes;
	private List<Long>[][] paths;
	private List<Long> injectiveRules;
	
	@SuppressWarnings("unchecked")
	private void initialize() {
		
		numOfNodes = 1 << (m - 1);
		paths = new List[numOfNodes][numOfNodes];
		for (int i = 0; i < numOfNodes; i++) {
			for (int j = 0; j < numOfNodes; j++) {
				paths[i][j] = new ArrayList<>();
			}
		}
		boolean[] used = new boolean[1 << m];
		for (int i = 0; i < numOfNodes; i++) {
			dfsPaths(i, numOfNodes + i, i, used);
		}
	}
	
	private void dfsPaths(int curNode, long path, int srcNode, boolean[] used) {
		
		for (int i = 0; i < 2; i++) {
			int lastEdge = (curNode << 1) + i;
			if (!used[lastEdge]) {
				used[lastEdge] = true;
				int nextNode = lastEdge % numOfNodes;
				long nextPath = (path << 1) + i;
				paths[srcNode][nextNode].add(nextPath);
				dfsPaths(nextNode, nextPath, srcNode, used);
				used[lastEdge] = false;
			}
		}
	}
	
	public boolean isInjective(long rule) {
		
		return true;
	}
	
	public boolean isInjective(String rule) {
		
		return isInjective(toLongRule(rule));
	}
	
	public void printAllInjectiveRules() {
		
		if (injectiveRules == null) {
			injectiveRules = new ArrayList<>();
			long begin = System.currentTimeMillis();
			long max = 1 << (1 << m);
			for (long rule = 0; rule < max; rule++) {
				if (isInjective(rule)) {
					injectiveRules.add(rule);
				};
			}
			long end = System.currentTimeMillis();
			System.out.println("执行用时：" + String.valueOf(end - begin) + "ms.");
		}
		for (long rule : injectiveRules) {
			System.out.println(toStringRule(rule));
		}
	}
	
	public String toStringRule(long rule) {
		
		int len = 1 << m;
		char[] arr = new char[len];
		for (int i = 0; i < len; i++) {
			arr[len - i - 1] = (char)(((rule >> i) & 1) + '0');
		}
		return String.valueOf(arr);
	}
	
	public long toLongRule(String rule) {
		
		int len = 1 << m;
		long digits = 0;
		for (int i = len - 1; i >= 0; i--) {
			digits <<= 1;
			digits += rule.charAt(i) - '0';
		}
		return digits;
	}
	
// main:
	public static void main(String[] args) {
		
		GlobalInjectivity gi = new GlobalInjectivity(3);
		for (int i = 0; i < gi.numOfNodes; i++) {
			for (int j = 0; j < gi.numOfNodes; j++) {
				System.out.println(i + " -> " + j + ":");
				for (long path : gi.paths[i][j]) {
					System.out.println(path);
				}
			}
		}
		
	}
	
}
