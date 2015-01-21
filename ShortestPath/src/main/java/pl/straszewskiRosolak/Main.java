package pl.straszewskiRosolak;

public class Main {

	public static void main(String[] args) {
		try {
			Instance ins = Instance.getInstance("data/kroA100.tsp.txt");

			ins.createDistanceMatrix();

			ZachlannyALg alg = new ZachlannyALg();
			int result = alg.solve(ins);
			System.out.println("NN: " + result);

			GCDrugiAlg dr = new GCDrugiAlg();
			result = dr.solve(ins);
			System.out.println("GC: " + result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
