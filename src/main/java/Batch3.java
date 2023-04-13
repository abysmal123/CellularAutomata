import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import ca.eden.GlobalSurjectivityDiameter4;
import eden.*;

public class Batch3 {
	
	public static String PATH = ".\\result\\";
	
	public static void main(String[] args) throws IOException {
		
		File file = new File(PATH + "diameter4.txt");
		if(!file.exists()){
            file.createNewFile();
        }
		FileOutputStream fileOS = new FileOutputStream(file);
		StringBuffer[] lines = new StringBuffer[5];
		lines[0] = new StringBuffer("其他（不满射）: \n");
		lines[1] = new StringBuffer("优先级：\n1 -> 3 -> 2 -> No edens -> others.\n\n第一类互补规则: \n");
		lines[2] = new StringBuffer("第二类互补规则: \n");
		lines[3] = new StringBuffer("第三类互补规则: \n");
		lines[4] = new StringBuffer("其他（满射）: \n");
		for (int i = 0; i < 65536; i++) {
			String rules = toSixteenBitString(i);
			GlobalSurjectivityDiameter4 gs4 = new GlobalSurjectivityDiameter4(rules, 0);
			StringBuffer line;
			if (meetsDiameter4Case1Rules(rules)) {
				line = lines[1];
			} else if (meetsDiameter4Case3Rules(rules)) {
				line = lines[3];
			} else if (meetsDiameter4Case2Rules(rules)) {
				line = lines[2];
			} else if (gs4.getEdens().length == 0) {
				line = lines[4];
			} else {
				line = lines[0];
			}
			line.append(rules + ": ");
			if (gs4.getEdens().length == 0) {
				line.append("No edens");
			} else {
				line.append("{");
				for (String eden : gs4.getEdens()) {
					line.append(eden + ", ");
				}
				line.delete(line.length() - 2, line.length());
				line.append("}");
			}
			line.append("\n");
		}
		lines[0].append("\n");
		lines[1].append("\n");
		lines[2].append("\n");
		lines[3].append("\n");
		lines[4].append("\n");
		fileOS.write(lines[1].toString().getBytes("gbk"));
		fileOS.write(lines[3].toString().getBytes("gbk"));
		fileOS.write(lines[2].toString().getBytes("gbk"));
		fileOS.write(lines[4].toString().getBytes("gbk"));
		fileOS.write(lines[0].toString().getBytes("gbk"));
		fileOS.flush();
		fileOS.close();
		System.out.println("结果已写入项目根目录下的 " + file.getPath());
	}
	
	public static String toSixteenBitString(int num) {
		
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < 16; i++) {
			buffer.insert(0, num & 1);
			num >>= 1;
		}
		return buffer.toString();
	}
	
	public static boolean meetsDiameter4Case1Rules(String rules) {
		
		for (int i = 0; i < 8; i++) {
			if (rules.charAt(2 * i) == rules.charAt(2 * i + 1)) {
				return false;
			}
		}
		return true;
	}
	
	public static boolean meetsDiameter4Case2Rules(String rules) {
		
		Set<Integer> frontThree = new HashSet<Integer>();
		Set<Integer> backThree = new HashSet<Integer>();
		int count = 0;
		for (int i = 0; i < 16; i++) {
			if (rules.charAt(i) == '1') {
				frontThree.add(i >> 1);
				backThree.add(i % 8);
				count++;
			}
		}
		if (count != 8 || frontThree.size() != 4 || backThree.size() != 4) {
			return false;
		}
		return true;
	}
	
	public static boolean meetsDiameter4Case3Rules(String rules) {
		
		for (int i = 0; i < 8; i++) {
			if (rules.charAt(i) == rules.charAt(i + 8)) {
				return false;
			}
		}
		return true;
	}
	
}
