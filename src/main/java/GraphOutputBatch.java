import java.io.IOException;

import ca.eden.GlobalSurjectivity;
import ca.eden.ShowProcedureTree;
import ca.eden.TreeNode;
import eden.*;

public class GraphOutputBatch {
	
	public static void main(String[] args) throws IOException {
		
		for (int num = 0; num < 256; num++) {
			String rules = toEightBitString(num);
			GlobalSurjectivity gs = new GlobalSurjectivity(rules, 3);
			TreeNode root = gs.getRootNode();
			ShowProcedureTree.setPath("graph/Ô­Ê÷/");
			ShowProcedureTree.storeImage(root, rules);
		}
	}
	
	public static String toEightBitString(int num) {
		
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < 8; i++) {
			buffer.insert(0, num & 1);
			num >>= 1;
		}
		return buffer.toString();
	}
	
}
