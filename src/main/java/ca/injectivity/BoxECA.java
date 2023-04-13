package ca.injectivity;

import java.util.HashSet;
import java.util.Set;

public final class BoxECA {
	
	protected int n1, n2;		// n1 <= n2
	
	protected Set<BoxECA> sequentSet;
	
	protected BoxECA(int n1, int n2) {
		
		if (n1 > n2) {
			n1 ^= n2;
			n2 ^= n1;
			n1 ^= n2;
		}
		this.n1 = n1;
		this.n2 = n2;
	}
	
	protected BoxECA(int n1, int n2, int rules) {
		
		if (n1 > n2) {
			n1 ^= n2;
			n2 ^= n1;
			n1 ^= n2;
		}
		this.n1 = n1;
		this.n2 = n2;
		sequentSet = new HashSet<BoxECA>();
		int prefix1 = (n1 << 1) % 8, prefix2 = (n2 << 1) % 8;
		for (int i : new int[] {0, 1}) {
			for (int j : new int[] {0, 1}) {
				if (((rules >> (prefix1 + i)) & 1) == ((rules >> (prefix2 + j)) & 1) 
						&& prefix1 + i != prefix2 + j) {
					sequentSet.add(new BoxECA(prefix1 + i, prefix2 + j));
				}
			}
		}
		if (prefix1 == prefix2 && !sequentSet.isEmpty()) {
			sequentSet.add(new BoxECA(prefix1, prefix2));
		}
	}
	
	@Override
	public int hashCode() {
		
		return (n1 << 3) + n2;
	}
	
	@Override
	public boolean equals(Object o) {
		
		if (o instanceof BoxECA) {
			return ((BoxECA)o).hashCode() == this.hashCode();
		}
		return false;
	}
	
}
