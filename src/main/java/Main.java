import ca.eden.GlobalSurjectivity;
import java.util.Scanner;

public class Main {
	
	public static void main(String[] args) {
		
		String rules = getConsoleRules();
		GlobalSurjectivity gs = new GlobalSurjectivity(rules, 2);
		if (gs.getEdens().length == 0) {
			System.out.println("No edens");
		} else {
			System.out.println("Edens:");
			for (String eden : gs.getEdens()) {
				System.out.println(eden);
			}
		}
	}
	
	public static String getConsoleRules() {
		
		String rules;
		Scanner scan = new Scanner(System.in);
		while (true) {
			System.out.println("Input rules:");
			rules = scan.next();
			boolean containsWrongChar = false;
			int n = rules.length();
			for (int i = 0; i < n; i++) {
				if (rules.charAt(i) != '0' && rules.charAt(i) != '1') {
					containsWrongChar = true;
					break;
				}
			}
			if (n != 8) {
				System.out.println("规则长度须为8位！");
			} else if (containsWrongChar) {
				System.out.println("规则只能由0或1组成！");
			} else {
				break;
			}
		}
		scan.close();
		return rules;
	}
	
}
