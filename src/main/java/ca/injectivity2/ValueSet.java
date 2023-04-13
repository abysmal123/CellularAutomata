package ca.injectivity2;

public final class ValueSet {
	
	protected int[] subValues;		/* for ECA: 00, 01, 10, 11.
									   for D4: 000, 001, 010, 011, 100, 101, 110, 111. */
	
	protected ValueSet() {
		
	}
	
	protected ValueSet(int[] subValues) {
		
		this.subValues = subValues;
	}

}
