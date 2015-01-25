package pl.straszewskiRosolak;

public class Main {

	public static void main(String[] args) {
		try {
			Instance ins = Instance.getInstance("data/kroA100.tsp.txt");

			ins.createDistanceMatrix();

			// ZachlannyALg alg = new ZachlannyALg();
			// System.out.println("NN: ");
			// alg.solve(ins);
			//
			// GCDrugiAlg dr = new GCDrugiAlg();
			// System.out.println("GC: ");
			// dr.solve(ins);
			//
			// ins = Instance.getInstance("data/kroA200.tsp");
			//
			// ins.createDistanceMatrix();
			//
			// alg = new ZachlannyALg();
			// System.out.println("NN: ");
			// alg.solve(ins);
			//
			// dr = new GCDrugiAlg();
			// System.out.println("GC: ");
			// dr.solve(ins);
			//
			// Algorithm ran = new RandomAlg();
			// System.out.println("Random: ");
			// ran.solve(ins);

			Algorithm ver = new VertexPairAlg();
			System.out.println("Vertex Pair: " + ver.solve(ins));
			System.out.println("--------------------------------");
			Algorithm edge = new EdgeSwitchAlg();
			System.out.println("Egde switch: " + edge.solve(ins));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
