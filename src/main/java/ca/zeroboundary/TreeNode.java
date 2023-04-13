package ca.zeroboundary;

public final class TreeNode {

	protected final int values;
	
	protected TreeNode zeroEdge;
	
	protected TreeNode oneEdge;
	
	protected int type;		// 1(1 edge),  0(0 edge), -1(root)
		
	protected TreeNode(int values) {
		
		this.values = values;
		zeroEdge = oneEdge = null;
		type = -1;
	}

}
