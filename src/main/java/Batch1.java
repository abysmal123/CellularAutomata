import java.io.IOException;

import ca.eden.GlobalSurjectivityDiameter4;
import ca.eden.ShowProcedureTreeDiameter4;
import ca.eden.TreeNode;

public class Batch1 {
	
	public static void main(String[] args) throws IOException {
		
		for (int num = 0; num < 256; num++) {
			String rules = toDiameter4Case2Rules(num);
			GlobalSurjectivityDiameter4 gs4 = new GlobalSurjectivityDiameter4(rules, 0);
			TreeNode root = gs4.getRootNode();
			ShowProcedureTreeDiameter4.setPath("graph/test/");
			ShowProcedureTreeDiameter4.storeImage(root, rules);
		}
	}
	
	public static String toDiameter4Case2Rules(int num) {
		
		StringBuffer buffer = new StringBuffer();
		for (int i = 7; i >= 4; i--) {
			buffer.append(1 - (num >> i & 1));
		}
		for (int i = 7; i >= 4; i--) {
			buffer.append(num >> i & 1);
		}
		for (int i = 3; i >= 0; i--) {
			buffer.append(1 - (num >> i & 1));
		}
		for (int i = 3; i >= 0; i--) {
			buffer.append(num >> i & 1);
		}
		return buffer.toString();
	}
	
}
