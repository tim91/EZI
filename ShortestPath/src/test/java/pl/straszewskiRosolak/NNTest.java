package pl.straszewskiRosolak;

import java.io.InputStream;

import org.testng.annotations.Test;

public class NNTest {

	@Test
	public void f() throws Exception {
		InputStream is = this.getClass().getClassLoader()
				.getResourceAsStream("test.txt");
		Instance ins = Instance.getInstance(is);
		ins.createDistanceMatrix();
		ins.printDistanceMatrix();

		ZachlannyALg alg = new ZachlannyALg();

		int sum = alg.solve(ins, 0);

		System.out.println(sum);
		sum = alg.solve(ins);
		System.out.println(sum);

	}

	@Test
	public void GCDrugiAlg() throws Exception {
		InputStream is = this.getClass().getClassLoader()
				.getResourceAsStream("test.txt");
		Instance ins = Instance.getInstance(is);
		ins.createDistanceMatrix();
		ins.printDistanceMatrix();

		GCDrugiAlg alg = new GCDrugiAlg();

		int sum = alg.solve(ins, 0);

		System.out.println(sum);
		sum = alg.solve(ins);
		System.out.println(sum);
	}

	@Test
	public void GCDrugiAlg2() throws Exception {
		InputStream is = this.getClass().getClassLoader()
				.getResourceAsStream("test2.txt");
		Instance ins = Instance.getInstance(is);
		ins.createDistanceMatrix();
		ins.printDistanceMatrix();
		System.out.println("--------------");
		GCDrugiAlg alg = new GCDrugiAlg();

		alg.solve(ins);
	}
}
