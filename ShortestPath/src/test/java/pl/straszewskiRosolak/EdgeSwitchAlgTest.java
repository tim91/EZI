package pl.straszewskiRosolak;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class EdgeSwitchAlgTest {

	@Test
	public void pairsGenerator() {

//		EdgeSwitchAlgorithm esa = new EdgeSwitchAlgorithm();
//
//		List<List<Integer[]>> res = esa.generatePairsToSwap(5);
//
//		for (List<Integer[]> list : res) {
//			for (Integer[] ll : list) {
//				System.out.print(" " + Arrays.toString(ll));
//			}
//			System.out.println();
//		}

	}

	@Test
	public void algTest() {

		InputStream is = this.getClass().getClassLoader()
				.getResourceAsStream("test3.txt");
		Instance ins;
		try {
			ins = Instance.getInstance(is);
			ins.createDistanceMatrix();
			ins.printDistanceMatrix();

			EdgeSwitchAlgorithm edge = new EdgeSwitchAlgorithm();

			int circ = edge.solve(ins);
			System.out.println("Best circ: " + circ);
//			edge.printBestSolution();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Test
	public void sortTest() {

		OrderedVertex os = new OrderedVertex();
		os.add(new VertexDistance(3, 0));
		os.add(new VertexDistance(5, 1));
		os.add(new VertexDistance(5, 2));
		os.add(new VertexDistance(1, 3));

		for (VertexDistance vertexDistance : os) {
			System.out.println(vertexDistance.getDistance());
		}
	}
}
