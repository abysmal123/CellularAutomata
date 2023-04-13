import java.io.File;
import java.util.Map;

import ca.nonlinear.SpecificNumberZeroD4L1;

public class Script1113 {

	public static void main(String[] args) {
		
		File[] graphs = (new File("graph/test2022.11.13/all")).listFiles();
		int MASK = 36;
		for (File graph : graphs) {
			String rule = graph.getName().substring(0, 16);
			SpecificNumberZeroD4L1.initializeRule(rule);
			Map<Integer, int[]> edges = SpecificNumberZeroD4L1.edges;
			if (edges.containsKey(MASK) && (edges.get(MASK)[0] == MASK || edges.get(MASK)[1] == MASK)) {
				graph.renameTo(new File("graph/test2022.11.13/(010,101)ring/" + rule + ".png"));
			}
		}
		
	}
}
