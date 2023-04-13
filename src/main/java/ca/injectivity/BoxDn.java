package ca.injectivity;

import java.util.HashSet;
import java.util.Set;
import ca.longbinary.LongBinary;

public final class BoxDn {
	
	protected int d, n1, n2;		// n1 <= n2
	
	protected Set<BoxDn> sequentSet;
	
	protected BoxDn(int d, int n1, int n2) {
		
		this.d = d;
		if (n1 > n2) {
			n1 ^= n2;
			n2 ^= n1;
			n1 ^= n2;
		}
		this.n1 = n1;
		this.n2 = n2;
	}
	
	protected BoxDn(int d, int n1, int n2, LongBinary rules) {
		
		this.d = d;
		if (n1 > n2) {
			n1 ^= n2;
			n2 ^= n1;
			n1 ^= n2;
		}
		this.n1 = n1;
		this.n2 = n2;
		sequentSet = new HashSet<BoxDn>();
		int prefix1 = (n1 << 1) % (1 << d), prefix2 = (n2 << 1) % (1 << d);
		for (int i : new int[] {0, 1}) {
			for (int j : new int[] {0, 1}) {
				if (rules.getPos(prefix1 + i) == rules.getPos(prefix2 + j) && prefix1 + i != prefix2 + j) {
					sequentSet.add(new BoxDn(d, prefix1 + i, prefix2 + j));
				}
			}
		}
		if (prefix1 == prefix2 && !sequentSet.isEmpty()) {
			sequentSet.add(new BoxDn(d, prefix1, prefix2));
		}
	}
	
	@Override
	public int hashCode() {
		
		return (n1 << d) + n2;
	}
	
	@Override
	public boolean equals(Object o) {
		
		if (o instanceof BoxDn) {
			return ((BoxDn)o).hashCode() == this.hashCode();
		}
		return false;
	}
	
}
