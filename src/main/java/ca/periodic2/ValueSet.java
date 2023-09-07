package ca.periodic2;

public final class ValueSet {
	
	protected int[] subValues;		/* for ECA: 00, 01, 10, 11.
									   for D4: 000, 001, 010, 011, 100, 101, 110, 111. */
	
	protected ValueSet() {
		
	}
	
	protected ValueSet(int k) {
		this.subValues = new int[k];
	}
	
	protected ValueSet(int[] subValues) {
		
		this.subValues = subValues;
	}

	@Override
	public int hashCode() {
		int ret = 0;
		for (int sv : subValues) {
			ret <<= 7;
			ret ^= sv;
		}
		return ret;
	}
	
	@Override 
	public boolean equals(Object o) {
		if (!(o instanceof ValueSet)) {
			return false;
		}
		if (subValues.length != ((ValueSet)o).subValues.length) {
			return false;
		}
		for (int i = 0; i < subValues.length; i++) {
			if (subValues[i] != ((ValueSet)o).subValues[i]) {
				return false;
			}
		}
		return true;
	}
}
