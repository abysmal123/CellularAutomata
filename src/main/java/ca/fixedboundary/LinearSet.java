package ca.fixedboundary;

import java.util.HashSet;
import java.util.Set;

public class LinearSet {
	
// public:
	public LinearSet(int n) {
		
		linearSet = new HashSet<String>();
		int num = (1 << n);
		for (int i = 0; i < num; i++) {
			StringBuffer buffer1 = new StringBuffer();
			StringBuffer buffer2 = new StringBuffer();
			for (int j = 0; j < num; j++) {
				buffer1.insert(0, (Integer.bitCount(i & j) & 1) + "");
				buffer2.insert(0, ((Integer.bitCount(i & j) + 1) & 1) + "");
			}
			linearSet.add(buffer1.toString());
			linearSet.add(buffer2.toString());
		}
	}
	
	public boolean contains(String r) {
		
		return linearSet.contains(r);
	}
	
// private:
	private Set<String> linearSet;
	
}
