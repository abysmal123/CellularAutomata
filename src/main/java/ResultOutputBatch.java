import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import ca.eden.GlobalSurjectivity;

public class ResultOutputBatch {
	
	public static String PATH = ".\\result\\";
	
	public static void main(String[] args) throws IOException {
		
		File file = new File(PATH + "eca_1.txt");
		if(!file.exists()){
            file.createNewFile();
        }
		FileOutputStream fileOS = new FileOutputStream(file);
		for (int i = 0; i < 256; i++) {
			String rules = toEightBitString(i);
			GlobalSurjectivity gs = new GlobalSurjectivity(rules, 0);
			StringBuffer line = new StringBuffer();
			line.append(rules + ": ");
			if (gs.getEdens().length == 0) {
				line.append("No edens");
			} else {
				line.append("{");
				for (String eden : gs.getEdens()) {
					line.append(eden + ", ");
				}
				line.delete(line.length() - 2, line.length());
				line.append("}");
			}
			line.append("\n");
			fileOS.write(line.toString().getBytes("gbk"));
		}
		fileOS.flush();
		fileOS.close();
		System.out.println("结果已写入项目根目录下的 " + file.getPath());
	}
	
	public static String toEightBitString(int num) {
		
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < 8; i++) {
			buffer.insert(0, num & 1);
			num >>= 1;
		}
		return buffer.toString();
	}
	
}
