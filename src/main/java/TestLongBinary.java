import ca.longbinary.LongBinary;

public class TestLongBinary {
	
	public static void main(String[] args) {
		
//		LongBinary lb1 = new LongBinary("1010010111000011011001101001110000111110000110011100001111100001");
		LongBinary lb1 = new LongBinary("1111111111111111111111111111111111111111111111111111111111111111");
		System.out.println(lb1.toString());
//		for (int i = 63; i >= 0; i--) {
//			System.out.print(lb1.getPos(i));
//		}
		lb1.setPos(62, 0);
		System.out.println(lb1.toString());
		
	}
}
