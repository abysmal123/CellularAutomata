import java.io.IOException;

import ca.nonlinear.InfiniteConfigurationD4L1;
import ca.nonlinear.ShowProcedureTreeD4L1;
import ca.zeroboundary2.ZeroD4L1FiniteLength;

public class Test1112 {
	
	public static void main(String[] args) throws IOException {
		ShowProcedureTreeD4L1.setPath("graph/test2022.11.12/d4l1/");
		int count = 0;
		for (int i = 43611; i < 65536; i++) {
			String r = toSixteenBitString(i);
			if (ZeroD4L1FiniteLength.hasEden(r) && !InfiniteConfigurationD4L1.hasEden(r)) {
				count++;
				System.out.print(r + " ");
				ShowProcedureTreeD4L1.storeImage(r);
				System.out.println("graph done. " + i);
			}
		}
		System.out.println("total: " + count);;
	}
	
	public static String toSixteenBitString(int num) {
		
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < 16; i++) {
			buffer.insert(0, num & 1);
			num >>= 1;
		}
		return buffer.toString();
	}
	
}
