import ca.zeroboundary2.ZeroD4L1FiniteLength;
import ca.zeroboundary2.ZeroD4L2FiniteLength;
import ca.zeroboundary2.ZeroD5FiniteLength;
import ca.specialinjectivity.SpecialInjectivity;;

public class test1022 {
	
	public static void main(String[] args) {

		System.out.println("Ö±¾¶4£º");
		for (String[] pair : SpecialInjectivity.toList(3)) {
			System.out.println(pair[0] + " -> " + pair[1] + 
					(ZeroD4L1FiniteLength.hasEden(pair[0]) || ZeroD4L2FiniteLength.hasEden(pair[0]) ? "" : " false"));
		}
		System.out.println();
		
		System.out.println("Ö±¾¶5£º");
		for (String[] pair : SpecialInjectivity.toList(4)) {
			System.out.println(pair[0] + " -> " + pair[1] + 
					(ZeroD5FiniteLength.hasEden(pair[0]) ? "" : " false"));
		}
		System.out.println();
	}

}
