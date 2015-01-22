package pl.straszewskiRosolak;

import static org.testng.Assert.assertEquals;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import org.testng.annotations.Test;

public class VertexPairTest {

	@Test
	public void pairsGeneratorSimple() {
		VertexPairAlg alg = new VertexPairAlg();

		List<Integer[]> pairs = alg.generatePairs(2);
		assertEquals(pairs.size(), 1, "Invalid pairs count");
		Integer[] pair = pairs.get(0);

		assertEquals(pair[0].intValue(), 0);
		assertEquals(pair[1].intValue(), 1);
	}

	@Test
	public void pairsGenerator() {
		VertexPairAlg alg = new VertexPairAlg();

		List<Integer[]> pairs = alg.generatePairs(4);
		assertEquals(pairs.size(), 6, "Invalid pairs count");
		Integer[] pair = pairs.get(5);
		assertEquals(pair[0].intValue(), 2);
		assertEquals(pair[1].intValue(), 3);
	}

	@Test
	public void prevVertex() throws Exception {

		VertexPairAlg alg = new VertexPairAlg();
		List<Integer> solution = Arrays.asList(3, 0, 1, 2);
		int v = alg.getPrevVertex(0, solution);
		assertEquals(v, 2);
	}

	@Test
	public void solve() throws Exception {

		InputStream is = this.getClass().getClassLoader()
				.getResourceAsStream("test.txt");
		Instance ins = Instance.getInstance(is);
		ins.createDistanceMatrix();
		ins.printDistanceMatrix();

		VertexPairAlg alg = new VertexPairAlg();

		int sum = alg.solve(ins);
		System.out.println(sum);
	}
}
