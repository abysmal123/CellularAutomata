import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class WolframPartition {
	
	public static void partition(String[] patterns, String srcFile, String dstFile) throws IOException {
		BufferedReader in = new BufferedReader(new FileReader(srcFile));
		int n = patterns[0].length(), m = patterns.length;
		List<String> wolframs = new ArrayList<>();
		String line;
		while ((line = in.readLine()) != null && (line.charAt(0) == '0' || line.charAt(0) == '1')) {
			line = line.substring(0, n);
			if (line.charAt(n - 1) == '0') {
				wolframs.add(new String(line));
			}
		}
		in.close();
		@SuppressWarnings("unchecked")
		List<String>[] partitions = new List[n];
		for (int i = 0; i < n; i++) {
			partitions[i] = new ArrayList<>();
		}
		for (String wolfram : wolframs) {
			int[] diff = new int[m];
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < m; j++) {
					if (wolfram.charAt(i) != patterns[j].charAt(i)) {
						diff[j]++;
					}
				}
			}
			int minDiff = diff[0];
			for (int j = 1; j < m; j++) {
				minDiff = Math.min(minDiff, diff[j]);
			}
			partitions[minDiff].add(wolfram);
		}
		File file = new File(dstFile);
		if(!file.exists()){
            file.createNewFile();
        }
		FileOutputStream fileOS = new FileOutputStream(file);
		for (int i = 0; i < n; i++) {
			if (partitions[i].size() == 0) {
				continue;
			}
			fileOS.write(new String("Differ by " + i + " bits(" + partitions[i].size() + "):\n").getBytes("gbk"));
			for (String rule : partitions[i]) {
				fileOS.write(rule.getBytes("gbk"));
				fileOS.write(new String("\n").getBytes("gbk"));
			}
			fileOS.write(new String("\n").getBytes("gbk"));
		}
		fileOS.close();
	}
	
// main:
	public static void main(String[] args) throws IOException {
		String[] patterns = {"11110000111100001111000011110000", "00001111000011110000111100001111"};
		String srcFile = "result/zero_d5.txt";
		String dstFile = "result/zero_d5_sorted.txt";
		partition(patterns, srcFile, dstFile);
	}
	
}
