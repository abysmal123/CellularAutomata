import java.util.Arrays;

//import nonlinear.SpecificNumberZeroD4L1;
import ca.nonlinear.SpecificNumberZeroD5;
import ca.fixedboundary.LinearSet;

public class TestLayerAlgorithm {
	
	public static char[] rulesCharArr;
	
	public static int count;
	
	public static int n;
	
	public static LinearSet ls;
	
	public static void dfs(int oneCount, int pos) throws Exception {
		
		if (oneCount == 16) {
			String r = new String(rulesCharArr);
			SpecificNumberZeroD5.initializeRule(r);
			SpecificNumberZeroD5.reversibilityBefore(n);
			if (SpecificNumberZeroD5.hasIrreversibleLayer && SpecificNumberZeroD5.hasReversibleLayer) {
				count++;
				System.out.println(r + (ls.contains(r) ? "" : " (nonlinear)"));
			}
			return;
		}
		if (16 - oneCount > 32 - pos) {
			return;
		}
		rulesCharArr[pos] = '1';
		dfs(oneCount + 1, pos + 1);
		rulesCharArr[pos] = '0';
		dfs(oneCount, pos + 1);
	}
	
	public static void main(String[] args) throws Exception {
		
		ls = new LinearSet(5);
		
		SpecificNumberZeroD5.setThreshold(6);
		n = 12;
		
		count = 0;
		rulesCharArr = new char[32];
		Arrays.fill(rulesCharArr, '0');
		long start = System.currentTimeMillis();
		dfs(0, 0);
		long end = System.currentTimeMillis();
		System.out.println("total: " + count);
		System.out.println("time: " + (end - start) + "ms.");
	}
	
	public static String toSixteenBitString(int num) {
		
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < 16; i++) {
			buffer.insert(0, num & 1);
			num >>= 1;
		}
		return buffer.toString();
	}
	
	public static String to32BitString(long num) {
		
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < 32; i++) {
			buffer.insert(0, num & 1);
			num >>= 1;
		}
		return buffer.toString();
	}
	
}
