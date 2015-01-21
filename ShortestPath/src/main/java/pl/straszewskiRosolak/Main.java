package pl.straszewskiRosolak;

public class Main {

	public static void main(String[] args) {
		try {
			Instance ins = Instance.getInstance("data/kroA100.tsp.txt");

			ins.createDistanceMatrix();

			ZachlannyALg alg = new ZachlannyALg();
			System.out.println("NN: ");
			alg.solve(ins);

			GCDrugiAlg dr = new GCDrugiAlg();
			System.out.println("GC: ");
			dr.solve(ins);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
